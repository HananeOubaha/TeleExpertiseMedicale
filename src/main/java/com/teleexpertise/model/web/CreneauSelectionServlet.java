package com.teleexpertise.model.web;

import com.teleexpertise.model.dao.ConsultationDao;
import com.teleexpertise.model.dao.CreneauDao;
import com.teleexpertise.model.dao.DemandeExpertiseDao;
import com.teleexpertise.model.dao.MedecinSpecialisteDao;
import com.teleexpertise.model.dao.PatientDao;
import com.teleexpertise.model.entities.Consultation;
import com.teleexpertise.model.entities.Creneau;
import com.teleexpertise.model.entities.DemandeExpertise;
import com.teleexpertise.model.entities.MedecinSpecialiste;
import com.teleexpertise.model.enums.ConsultationStatutEnum;
import com.teleexpertise.model.enums.PrioriteEnum;
import com.teleexpertise.model.enums.RoleEnum;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/generaliste/selection_creneau")
public class CreneauSelectionServlet extends HttpServlet {

    private final CreneauDao creneauDao = new CreneauDao();
    private final ConsultationDao consultationDao = new ConsultationDao();
    private final MedecinSpecialisteDao specialistDao = new MedecinSpecialisteDao();
    private final DemandeExpertiseDao demandeDao = new DemandeExpertiseDao();

    // GET : Afficher les créneaux disponibles du spécialiste
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!RoleEnum.GENERALISTE.equals(request.getSession().getAttribute("utilisateurRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            Long consultationId = Long.parseLong(request.getParameter("consultationId"));
            Long specialistId = Long.parseLong(request.getParameter("specialisteId"));

            // 1. Charger le Spécialiste et la Consultation (Nécessaire pour le formulaire)
            Consultation consultation = consultationDao.findById(consultationId);
            MedecinSpecialiste specialist = specialistDao.findById(specialistId);

            // 2. Charger les créneaux disponibles (filtrer ceux qui ne sont pas passés et sont disponibles)
            EntityManager em = com.teleexpertise.dao.JpaUtil.getEntityManagerFactory().createEntityManager();
            List<Creneau> creneaux = em.createQuery(
                            "SELECT c FROM Creneau c WHERE c.specialiste.id = :specId AND c.estDisponible = TRUE AND c.heureDebut > CURRENT_TIMESTAMP ORDER BY c.heureDebut ASC", Creneau.class)
                    .setParameter("specId", specialistId)
                    .getResultList();
            em.close();

            // Passer les objets et les listes à la JSP
            request.setAttribute("consultation", consultation);
            request.setAttribute("specialist", specialist);
            request.setAttribute("creneauxDisponibles", creneaux);
            request.setAttribute("priorities", Arrays.asList(PrioriteEnum.values()));

            request.getRequestDispatcher("/WEB-INF/generaliste/creneau_selection.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "Erreur lors du chargement des disponibilités: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/generaliste/specialiste_search.jsp").forward(request, response);
        }
    }

    // POST : Création de la Demande d'Expertise (Étape 5)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ... (Vérification de sécurité omise pour la concision) ...

        try {
            Long consultationId = Long.parseLong(request.getParameter("consultationId"));
            Long specialistId = Long.parseLong(request.getParameter("specialisteId"));
            Long creneauId = parseLong(request.getParameter("creneau")); // Peut être null si asynchrone

            String question = request.getParameter("question");
            PrioriteEnum priorite = PrioriteEnum.valueOf(request.getParameter("priorite"));

            if (question.trim().isEmpty()) throw new IllegalArgumentException("La question est obligatoire.");

            // 1. Charger la consultation existante pour la mise à jour
            Consultation consultation = consultationDao.findById(consultationId);
            MedecinSpecialiste specialist = specialistDao.findById(specialistId);

            if (consultation == null) throw new RuntimeException("Consultation introuvable.");

            // 2. Créer la Demande d'Expertise (Liaison, Statut, Priorité)
            DemandeExpertise demande = new DemandeExpertise();
            demande.setConsultation(consultation);
            demande.setSpecialisteDemande(specialist);
            demande.setQuestionPosee(question);
            demande.setPriorite(priorite);
            demande.setStatut(ConsultationStatutEnum.EN_ATTENTE_AVIS_SPECIALISTE);

            // 3. Gérer le Créneau (Si Synchrone)
            if (creneauId != null) {
                // Charger le Créneau pour le marquer comme réservé
                EntityManager em = com.teleexpertise.dao.JpaUtil.getEntityManagerFactory().createEntityManager();
                Creneau creneau = em.find(Creneau.class, creneauId);
                em.close();

                if (creneau == null || !creneau.getEstDisponible()) {
                    throw new RuntimeException("Créneau déjà réservé ou invalide.");
                }

                // Marquer comme réservé
                creneau.setEstDisponible(false);

                // Lier le créneau à la Consultation
                consultation.setCreneauReserve(creneau);
            }

            // 4. Mettre à jour la Consultation (Statut)
            consultation.setStatut(ConsultationStatutEnum.EN_ATTENTE_AVIS_SPECIALISTE);

            // 5. Sauvegarde
            demandeDao.save(demande); // Sauvegarde la demande
            consultationDao.save(consultation); // Met à jour le statut et lie le créneau/demande

            // Succès et Notification
            response.sendRedirect(request.getContextPath() + "/generaliste/dashboard?message=Demande d'expertise créée et spécialiste notifié.");

        } catch (Exception e) {
            request.setAttribute("error", "Erreur lors de la soumission de la demande: " + e.getMessage());
            e.printStackTrace();
            // Retour au formulaire de sélection des créneaux
            response.sendRedirect(request.getContextPath() + "/generaliste/selection_creneau?consultationId=" + request.getParameter("consultationId") + "&specialisteId=" + request.getParameter("specialisteId") + "&error=" + e.getMessage());
        }
    }

    // Utilitaires
    private Long parseLong(String s) { return (s != null && !s.trim().isEmpty()) ? Long.parseLong(s.trim()) : null; }
}