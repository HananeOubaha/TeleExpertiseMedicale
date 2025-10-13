package com.teleexpertise.model.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// Affiche le formulaire de connexion
@WebServlet(name = "LoginDisplayServlet", urlPatterns = {"/login"})
public class LoginDisplayServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // C'est cette Servlet qui gère l'affichage de la page
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}