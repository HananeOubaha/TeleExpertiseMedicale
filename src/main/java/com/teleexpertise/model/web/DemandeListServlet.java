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
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;

@WebServlet("/specialiste/demandes_expertise")
public class DemandeListServlet extends HttpServlet {

    private final DemandeExpertiseDao demandeDao = new DemandeExpertiseDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        if (!RoleEnum.SPECIALISTE.equals(session.getAttribute("utilisateurRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            Long specialistId = (Long) session.getAttribute("utilisateurId");
            String statutParam = request.getParameter("statut");

            ConsultationStatutEnum statut = null;
            if (statutParam != null && !statutParam.isEmpty()) {
                // Tente de convertir le paramètre en Enum pour le filtre
                try {
                    statut = ConsultationStatutEnum.valueOf(statutParam.toUpperCase());
                } catch (IllegalArgumentException ignored) {
                    // Laisser statut à null si la valeur est invalide
                }
            }

            // US7: Récupérer les demandes filtrées
            List<DemandeExpertise> demandes = demandeDao.findBySpecialisteAndStatut(specialistId, statut);

            // US7 (Exigence Stream API): Filtrer par priorité (déjà fait dans la requête JPQL pour l'efficacité)
            // Nous pouvons exposer les statuts pour le filtre de la JSP
            request.setAttribute("statutsDisponibles", Arrays.asList(ConsultationStatutEnum.values()));
            request.setAttribute("demandes", demandes);
            request.setAttribute("statutSelectionne", statut);

            request.getRequestDispatcher("/WEB-INF/specialiste/demandes_list.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "Erreur lors du chargement des demandes: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/specialiste/dashboard_spec.jsp").forward(request, response);
        }
    }
}