package com.teleexpertise.model.web;

import com.teleexpertise.model.dao.UtilisateurDao;
import com.teleexpertise.model.entities.Utilisateur;
import com.teleexpertise.model.enums.RoleEnum;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// Traite la soumission du formulaire
@WebServlet(name = "LoginProcessingServlet", urlPatterns = {"/doLogin"})
public class LoginProcessingServlet extends HttpServlet {

    private UtilisateurDao utilisateurDao = new UtilisateurDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // --- (La logique de validation CSRF sera ici, quand elle sera implémentée) ---

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Utilisateur utilisateur = utilisateurDao.authentifier(email, password);

        if (utilisateur != null) {
            // Authentification réussie
            HttpSession session = request.getSession(true);
            session.setAttribute("utilisateurId", utilisateur.getId());
            session.setAttribute("utilisateurRole", utilisateur.getRole());

            redirectBasedOnRole(utilisateur.getRole(), response, request);

        } else {
            // Échec d'authentification
            request.setAttribute("error", "Email ou mot de passe incorrect.");
            // Renvoie vers la SERVLET d'affichage (LoginDisplayServlet)
            request.getRequestDispatcher("/login").forward(request, response);
        }
    }

    private void redirectBasedOnRole(RoleEnum role, HttpServletResponse response, HttpServletRequest request) throws IOException {
        String url;
        // La même logique de redirection que précédemment
        switch (role) {
            case INFIRMIER:
                url = request.getContextPath() + "/infirmier/dashboard";
                break;
            case GENERALISTE:
                url = request.getContextPath() + "/generaliste/dashboard";
                break;
            case SPECIALISTE:
                url = request.getContextPath() + "/specialiste/dashboard";
                break;
            default:
                url = request.getContextPath() + "/error";
                break;
        }
        response.sendRedirect(url);
    }
}