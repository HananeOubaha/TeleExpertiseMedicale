package com.teleexpertise.model.web;

import com.teleexpertise.model.dao.MedecinSpecialisteDao; // Changement
import com.teleexpertise.model.entities.MedecinSpecialiste;
import com.teleexpertise.model.enums.RoleEnum;
import com.teleexpertise.model.enums.SpecialiteEnum;
import com.teleexpertise.model.service.SpecialisteService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;

@WebServlet("/specialiste/profile")
public class SpecialisteProfileServlet extends HttpServlet {

    private final SpecialisteService specialistService = new SpecialisteService();
    private final MedecinSpecialisteDao specialistDao = new MedecinSpecialisteDao(); // Utilisation du DAO Spécialiste

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
            // Utilisation de MedecinSpecialisteDao.findById()
            MedecinSpecialiste specialist = specialistDao.findById(specialistId);

            request.setAttribute("specialist", specialist);
            request.setAttribute("specialities", Arrays.asList(SpecialiteEnum.values()));

            request.getRequestDispatcher("/WEB-INF/specialiste/profile_form.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "Erreur de chargement du profil: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/specialiste/profile_form.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        if (!RoleEnum.SPECIALISTE.equals(session.getAttribute("utilisateurRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            Long specialistId = (Long) session.getAttribute("utilisateurId");
            // Utilisation de MedecinSpecialisteDao.findById()
            MedecinSpecialiste specialist = specialistDao.findById(specialistId);

            if (specialist == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Spécialiste introuvable.");
                return;
            }

            // Récupération des données du formulaire
            double tarif = Double.parseDouble(request.getParameter("tarif"));
            SpecialiteEnum specialite = SpecialiteEnum.valueOf(request.getParameter("specialite"));

            // US-SPE-1: Mise à jour du profil et génération des créneaux
            specialistService.updateProfile(specialist, specialite, tarif);
            specialistService.genererCreneaux(specialist);

            response.sendRedirect(request.getContextPath() + "/specialiste/profile?message=Profil mis à jour et créneaux générés.");

        } catch (Exception e) {
            request.setAttribute("error", "Erreur lors de la sauvegarde du profil: " + e.getMessage());
            doGet(request, response); // Réafficher le formulaire avec l'erreur
        }
    }
}