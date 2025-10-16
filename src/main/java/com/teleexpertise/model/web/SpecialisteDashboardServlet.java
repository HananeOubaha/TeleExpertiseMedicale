package com.teleexpertise.model.web;

import com.teleexpertise.model.dao.MedecinSpecialisteDao;
import com.teleexpertise.model.entities.MedecinSpecialiste;
import com.teleexpertise.model.enums.RoleEnum;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/specialiste/dashboard")
public class SpecialisteDashboardServlet extends HttpServlet {

    private final MedecinSpecialisteDao specialistDao = new MedecinSpecialisteDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Long specialistId = (Long) session.getAttribute("utilisateurId");

        // 1. Sécurité et Rôle
        if (!RoleEnum.SPECIALISTE.equals(session.getAttribute("utilisateurRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        MedecinSpecialiste specialist = specialistDao.findById(specialistId);

        if (specialist == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Profil spécialiste introuvable.");
            return;
        }

        // 2. Vérification de la configuration (US-SPE-1)
        // Si la spécialité n'est pas définie (null par défaut lors de la création d'utilisateur)
        if (specialist.getSpecialite() == null) {
            // Rediriger vers la page de configuration
            response.sendRedirect(request.getContextPath() + "/specialiste/profile?message=Veuillez configurer votre profil pour commencer.");
            return;
        }

        // 3. Affichage du tableau de bord principal
        request.setAttribute("specialist", specialist);
        request.getRequestDispatcher("/WEB-INF/specialiste/dashboard_spec.jsp").forward(request, response);
    }
}