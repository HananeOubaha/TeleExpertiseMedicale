package com.teleexpertise.model.web;

import com.teleexpertise.model.dao.ConsultationDao;
import com.teleexpertise.model.entities.Consultation;
import com.teleexpertise.model.entities.MedecinGeneraliste;
import com.teleexpertise.model.entities.Patient;
import com.teleexpertise.model.enums.ConsultationStatutEnum;
import com.teleexpertise.model.enums.RoleEnum;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/generaliste/creer_consultation")
public class ConsultationCreationServlet extends HttpServlet {

    private final ConsultationDao consultationDao = new ConsultationDao();

    // GET : Affichage du formulaire (Création ou Edition)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // 1. Sécurité: Vérifier le rôle
        if (!RoleEnum.GENERALISTE.equals(session.getAttribute("utilisateurRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès réservé au Généraliste.");
            return;
        }

        EntityManager em = com.teleexpertise.dao.JpaUtil.getEntityManagerFactory().createEntityManager();
        Patient patient = null;
        Consultation consultation = null;

        try {
            String consultationIdParam = request.getParameter("consultationId");
            String patientIdParam = request.getParameter("patientId");

            if (consultationIdParam != null && !consultationIdParam.isEmpty()) {
                // SCÉNARIO 1: ÉDITION d'une consultation existante
                Long consultationId = Long.parseLong(consultationIdParam);
                consultation = em.find(Consultation.class, consultationId);

                if (consultation != null) {
                    patient = consultation.getPatient();
                }
            } else if (patientIdParam != null && !patientIdParam.isEmpty()) {
                // SCÉNARIO 2: CRÉATION d'une nouvelle consultation
                Long patientId = Long.parseLong(patientIdParam);
                patient = em.find(Patient.class, patientId);
            }

            if (patient == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Patient ou Consultation introuvable.");
                return;
            }

            // Passer l'erreur possiblement envoyée par le POST raté
            String errorMsg = request.getParameter("error");
            if (errorMsg != null) {
                request.setAttribute("error", errorMsg);
            }

            // Passage des objets à la JSP
            request.setAttribute("patient", patient);
            request.setAttribute("consultation", consultation);

            request.getRequestDispatcher("/WEB-INF/generaliste/consultation_form.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID Patient/Consultation invalide.");
        } catch (Exception e) {
            request.setAttribute("error", "Erreur lors du chargement: " + e.getMessage());
            e.printStackTrace();
            request.getRequestDispatcher("/WEB-INF/generaliste/liste_patients_gen.jsp").forward(request, response);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    // POST : Traitement du formulaire (Création, Sauvegarde ou Clôture)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String patientIdParam = request.getParameter("patientId");
        Long patientId = null;

        try {
            if (patientIdParam != null && !patientIdParam.isEmpty()) {
                patientId = Long.parseLong(patientIdParam);
            }

            Long generalisteId = (Long) request.getSession().getAttribute("utilisateurId");
            String action = request.getParameter("action");
            String consultationIdParam = request.getParameter("consultationId");

            // 1. Charger/Initialiser la consultation
            EntityManager em = com.teleexpertise.dao.JpaUtil.getEntityManagerFactory().createEntityManager();
            Consultation consultation;

            if (consultationIdParam != null && !consultationIdParam.isEmpty()) {
                // Cas 1: Édition
                consultation = em.find(Consultation.class, Long.parseLong(consultationIdParam));
                if (consultation == null) throw new RuntimeException("Consultation introuvable.");
            } else {
                // Cas 2: Nouvelle création
                consultation = new Consultation();
                Patient patient = em.find(Patient.class, patientId);
                MedecinGeneraliste generaliste = em.find(MedecinGeneraliste.class, generalisteId);

                if (patient == null || generaliste == null) {
                    throw new RuntimeException("Ressources Patient/Généraliste non trouvées.");
                }
                consultation.setPatient(patient);
                consultation.setGeneraliste(generaliste);
            }
            // IMPORTANT : NE PAS FERMER EM ICI si les entités doivent être utilisées.
            // Le DAO ouvrira et fermera sa propre transaction.
            em.close();


            // 2. Mettre à jour les données du formulaire
            consultation.setMotif(request.getParameter("motif"));
            consultation.setObservations(request.getParameter("observations"));
            consultation.setActes(Collections.emptyList());

            // 3. Gestion des Actions (Sauvegarde ou Clôture)
            if ("cloturer".equals(action)) {
                // Scénario A: Clôture
                consultation.setDiagnostic(request.getParameter("diagnostic"));
                consultation.setPrescription(request.getParameter("prescription"));
                consultation.setStatut(ConsultationStatutEnum.TERMINEE);

                consultation = consultationDao.save(consultation); // Mise à jour de l'objet
                response.sendRedirect(request.getContextPath() + "/generaliste/dashboard?message=Consultation clôturée (TERMINEE).");
                return;

            } else if ("sauvegarder".equals(action)) {
                // Sauvegarde simple (mode brouillon/édition)
                consultation.setStatut(ConsultationStatutEnum.EN_COURS_DE_TRAITEMENT);
                consultation = consultationDao.save(consultation); // Mise à jour de l'objet avec l'ID généré

                // CORRECTION : Redirection vers la même Servlet en mode ÉDITION
                response.sendRedirect(request.getContextPath() + "/generaliste/creer_consultation?consultationId=" + consultation.getId());
                return;
            }

        } catch (Exception e) {
            // Afficher l'erreur et revenir au formulaire
            String encodedError = URLEncoder.encode("Erreur critique lors de l'action: " + e.getMessage(), StandardCharsets.UTF_8.toString());
            e.printStackTrace();

            // Rediriger vers l'URL correcte en cas d'échec
            if (patientId != null) {
                // Si on a l'ID patient, on retourne à la page de création/édition
                response.sendRedirect(request.getContextPath() + "/generaliste/creer_consultation?patientId=" + patientId + "&error=" + encodedError);
            } else {
                // Sinon, on retourne à la liste
                response.sendRedirect(request.getContextPath() + "/generaliste/liste_patients?error=" + encodedError);
            }
            return;
        }
    }
}