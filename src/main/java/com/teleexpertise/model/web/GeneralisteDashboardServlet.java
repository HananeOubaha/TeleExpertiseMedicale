package com.teleexpertise.model.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/generaliste/dashboard")
public class GeneralisteDashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Assurez-vous que l'utilisateur est connecté et est Généraliste
        // (La logique de filtre sera ajoutée plus tard pour la sécurité)

        request.setAttribute("user", request.getSession().getAttribute("utilisateurRole"));

        // Redirige vers une page simple de confirmation
        request.getRequestDispatcher("/dashboard_generaliste.jsp").forward(request, response);
    }
}