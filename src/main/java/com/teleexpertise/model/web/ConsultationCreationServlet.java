package com.teleexpertise.model.web;

import com.teleexpertise.model.dao.PatientDao;
import com.teleexpertise.model.dao.UtilisateurDao;
import com.teleexpertise.model.entities.Consultation;
import com.teleexpertise.model.entities.Patient;
import com.teleexpertise.model.entities.MedecinGeneraliste;
import com.teleexpertise.model.entities.ActeTechnique;
import com.teleexpertise.model.dao.ConsultationDao;
import com.teleexpertise.model.enums.ConsultationStatutEnum;
import com.teleexpertise.model.enums.RoleEnum;
// import com.teleexpertise.dao.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Collections; // Pour éviter les NullPointerException sur les listes vides

@WebServlet("/generaliste/creer_consultation")
public class ConsultationCreationServlet extends HttpServlet {

    private final PatientDao patientDao = new PatientDao();
    private final ConsultationDao consultationDao = new ConsultationDao();
    private final UtilisateurDao utilisateurDao = new UtilisateurDao(); // Pour charger le Généraliste

    // GET : Affichage du formulaire initial
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Sécurité: Vérifier le rôle
        if (!RoleEnum.GENERALISTE.equals(session.getAttribute("utilisateurRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès réservé au Généraliste.");
            return;
        }

        try {
            Long patientId = Long.parseLong(request.getParameter("patientId"));

            // Chercher le patient pour afficher ses infos (Nécessite findById dans PatientDao ou EntityManager.find)
            // Pour l'instant, on utilise une recherche simple (à adapter si besoin de lazy fetch)
            EntityManager em = com.teleexpertise.dao.JpaUtil.getEntityManagerFactory().createEntityManager();
            Patient patient = em.find(Patient.class, patientId);
            em.close();

            if (patient == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Patient introuvable.");
                return;
            }

            // Charger la liste des Actes Techniques pour le formulaire (Nécessite ActeTechniqueDao)
            // Pour le moment, nous allons simuler ou nécessiter un DAO ActeTechnique

            request.setAttribute("patient", patient);
            // La page de consultation sera la même pour la création et la gestion
            request.getRequestDispatcher("/WEB-INF/generaliste/consultation_form.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID Patient invalide.");
        } catch (Exception e) {
            request.setAttribute("error", "Erreur lors du chargement: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/generaliste/liste_patients_gen.jsp").forward(request, response);
        }
    }

    // POST : Traitement du formulaire (Création ou Clôture Scénario A)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Sécurité CSRF et Rôle déjà gérés par les filtres (à implémenter)

        try {
            Long patientId = Long.parseLong(request.getParameter("patientId"));
            Long generalisteId = (Long) request.getSession().getAttribute("utilisateurId");
            String action = request.getParameter("action"); // 'save' ou 'cloturer'

            // 1. Charger les entités
            EntityManager em = com.teleexpertise.dao.JpaUtil.getEntityManagerFactory().createEntityManager();
            Patient patient = em.find(Patient.class, patientId);
            MedecinGeneraliste generaliste = em.find(MedecinGeneraliste.class, generalisteId);
            em.close();

            if (patient == null || generaliste == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Ressources non trouvées.");
                return;
            }

            // 2. Création initiale de la consultation
            Consultation consultation = new Consultation();
            consultation.setPatient(patient);
            consultation.setGeneraliste(generaliste);
            consultation.setMotif(request.getParameter("motif"));
            consultation.setObservations(request.getParameter("observations"));

            // 3. Gestion des Actes Techniques (Simplifié - Nécessite un ActeTechniqueDao.findAll())
            // Actes non gérés dans ce POST initial, seront ajoutés plus tard.
            consultation.setActes(Collections.emptyList());

            // 4. Clôture (Scénario A)
            if ("cloturer".equals(action)) {
                consultation.setDiagnostic(request.getParameter("diagnostic"));
                consultation.setPrescription(request.getParameter("prescription"));
                consultation.setStatut(ConsultationStatutEnum.TERMINEE);
                // Le coût sera calculé dans le DAO ou Service après liaison des actes

                consultationDao.save(consultation);

                // Succès: Retour au tableau de bord
                response.sendRedirect(request.getContextPath() + "/generaliste/dashboard?message=Consultation clôturée avec succès (Scénario A).");
                return;

            } else if ("save".equals(action)) {
                // Sauvegarde simple (statut EN_COURS_DE_TRAITEMENT)
                consultation.setStatut(ConsultationStatutEnum.EN_COURS_DE_TRAITEMENT);
                consultationDao.save(consultation);

                // Redirection vers l'édition de la consultation sauvée
                response.sendRedirect(request.getContextPath() + "/generaliste/editer_consultation?consultationId=" + consultation.getId());
                return;
            }

        } catch (Exception e) {
            request.setAttribute("error", "Erreur lors de la gestion de la consultation: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/generaliste/consultation_form.jsp").forward(request, response);
        }
    }
}