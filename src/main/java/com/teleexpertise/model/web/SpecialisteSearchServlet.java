package com.teleexpertise.model.web;

import com.teleexpertise.model.entities.MedecinSpecialiste;
import com.teleexpertise.model.enums.RoleEnum;
import com.teleexpertise.model.enums.SpecialiteEnum;
import com.teleexpertise.model.service.GeneralisteService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/generaliste/rechercher_specialiste")
public class SpecialisteSearchServlet extends HttpServlet {

    private final GeneralisteService generalisteService = new GeneralisteService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Sécurité
        if (!RoleEnum.GENERALISTE.equals(request.getSession().getAttribute("utilisateurRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            Long consultationId = Long.parseLong(request.getParameter("consultationId"));

            // Récupérer le filtre de spécialité sélectionné par l'utilisateur
            String selectedSpec = request.getParameter("specialite");
            SpecialiteEnum specialiteFiltre = null;
            List<MedecinSpecialiste> specialists = null;

            if (selectedSpec != null && !selectedSpec.isEmpty()) {
                specialiteFiltre = SpecialiteEnum.valueOf(selectedSpec.toUpperCase());

                // US3: Exécuter la recherche et le tri
                specialists = generalisteService.findSpecialistes(specialiteFiltre);
            }

            // Passer les données à la JSP
            request.setAttribute("consultationId", consultationId);
            request.setAttribute("specialities", Arrays.asList(SpecialiteEnum.values()));
            request.setAttribute("specialists", specialists);
            request.setAttribute("specialiteFiltre", specialiteFiltre);

            request.getRequestDispatcher("/WEB-INF/generaliste/specialiste_search.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "Erreur lors du chargement des spécialistes: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/generaliste/consultation_form.jsp").forward(request, response);
        }
    }
}