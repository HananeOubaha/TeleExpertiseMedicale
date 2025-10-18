package com.teleexpertise.model.web;

import com.teleexpertise.model.dao.DemandeExpertiseDao;
import com.teleexpertise.model.entities.DemandeExpertise;
import com.teleexpertise.model.enums.ConsultationStatutEnum;
import com.teleexpertise.model.enums.RoleEnum;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/specialiste/repondre_expertise")
public class ReponseExpertiseServlet extends HttpServlet {

    private final DemandeExpertiseDao demandeDao = new DemandeExpertiseDao();

    // GET : Afficher le détail de la demande et le formulaire de réponse
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!RoleEnum.SPECIALISTE.equals(request.getSession().getAttribute("utilisateurRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            Long demandeId = Long.parseLong(request.getParameter("demandeId"));

            // Le findById chargé avec FETCH va lever une exception ici si le mapping échoue
            DemandeExpertise demande = demandeDao.findById(demandeId);

            if (demande == null) {
                // Rediriger vers la liste si l'ID est valide mais la demande est introuvable
                response.sendRedirect(request.getContextPath() + "/specialiste/demandes_expertise?error=Demande introuvable.");
                return;
            }

            request.setAttribute("demande", demande);

            // Passer les signes vitaux pour l'affichage (Chargement forcé nécessaire pour la LAZY LIST)
            // Comme le Patient est déjà chargé, on le passe à la JSP

            request.getRequestDispatcher("/WEB-INF/specialiste/reponse_form.jsp").forward(request, response);

        } catch (Exception e) {
            // Log l'erreur et redirige vers la liste
            System.err.println("ERREUR LORS DU CHARGEMENT DE LA DEMANDE D'EXPERTISE : " + e.getMessage());
            e.printStackTrace();

            String encodedError = java.net.URLEncoder.encode("Erreur lors du chargement de la demande: " + e.getMessage(), java.nio.charset.StandardCharsets.UTF_8.toString());
            response.sendRedirect(request.getContextPath() + "/specialiste/demandes_expertise?error=" + encodedError);
            return;
        }
    }

    // POST : Traitement de la réponse (Saisir avis et Marquer comme terminée)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!RoleEnum.SPECIALISTE.equals(request.getSession().getAttribute("utilisateurRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            Long demandeId = Long.parseLong(request.getParameter("demandeId"));
            String avis = request.getParameter("avis");
            String recommandations = request.getParameter("recommandations");

            if (avis.trim().isEmpty()) throw new IllegalArgumentException("L'avis médical est obligatoire.");

            // 1. Charger et mettre à jour la demande
            DemandeExpertise demande = demandeDao.findById(demandeId);
            if (demande == null) throw new RuntimeException("Demande introuvable.");

            demande.setAvisSpecialiste(avis);
            demande.setRecommandations(recommandations);
            demande.setStatut(ConsultationStatutEnum.TERMINEE); // Marquer comme terminée (US8)

            // 2. Mettre à jour la Consultation (la Consultation est aussi TERMINEE)
            demande.getConsultation().setStatut(ConsultationStatutEnum.TERMINEE);

            // 3. Sauvegarde
            demandeDao.save(demande);

            response.sendRedirect(request.getContextPath() + "/specialiste/demandes_expertise?message=Réponse enregistrée et consultation clôturée.");

        } catch (Exception e) {
            // En cas d'échec de POST, on log et on redirige pour revenir au formulaire avec l'ID
            String encodedError = java.net.URLEncoder.encode("Erreur lors de la soumission: " + e.getMessage(), java.nio.charset.StandardCharsets.UTF_8.toString());
            e.printStackTrace();

            response.sendRedirect(request.getContextPath() + "/specialiste/repondre_expertise?demandeId=" + request.getParameter("demandeId") + "&error=" + encodedError);
        }
    }
}