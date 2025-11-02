package com.teleexpertise.model.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false); // Récupère la session existante, si elle existe

        if (session != null) {
            // 1. Invalide la session (supprime toutes les données utilisateur, y compris le rôle et l'ID)
            session.invalidate();
            System.out.println("DEBUG: Session utilisateur invalidée.");
        }

        // 2. Redirige l'utilisateur vers la page de connexion (Login)
        // La redirection est faite vers l'URL de la Servlet /login
        response.sendRedirect(request.getContextPath() + "/login?message=Deconnexion reussie.");
    }

    // Le POST n'est généralement pas nécessaire pour la déconnexion, mais on peut le chaîner au GET.
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}