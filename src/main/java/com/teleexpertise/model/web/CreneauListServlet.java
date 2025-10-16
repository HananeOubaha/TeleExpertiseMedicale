package com.teleexpertise.model.web;

import com.teleexpertise.model.dao.CreneauDao;
import com.teleexpertise.model.entities.Creneau;
import com.teleexpertise.model.enums.RoleEnum;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/specialiste/creneaux")
public class CreneauListServlet extends HttpServlet {

    private final CreneauDao creneauDao = new CreneauDao();

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

            // US6: Récupérer la liste des créneaux
            List<Creneau> creneaux = creneauDao.findCreneauxBySpecialisteId(specialistId);

            request.setAttribute("creneaux", creneaux);

            // LIGNE DE DISPATCH NORMALE (doit afficher la JSP de la liste)
            request.getRequestDispatcher("/WEB-INF/specialiste/creneaux_list.jsp").forward(request, response);

        } catch (Exception e) {
            // CORRECTION CRITIQUE : Afficher l'erreur dans la console et renvoyer
            // l'utilisateur vers le tableau de bord ou la page d'erreur.
            // On s'assure de ne pas tomber dans une boucle et de ne pas cacher l'erreur
            System.err.println("ERREUR LORS DU CHARGEMENT DES CRÉNEAUX: " + e.getMessage());
            e.printStackTrace(); // Affiche la stack trace complète dans le log Tomcat

            request.setAttribute("error", "Impossible de charger la liste des créneaux: " + e.getMessage());

            // RETOUR AU DASHBOARD EN CAS D'ÉCHEC GRAVE
            request.getRequestDispatcher("/WEB-INF/specialiste/dashboard_spec.jsp").forward(request, response);
        }
    }
}