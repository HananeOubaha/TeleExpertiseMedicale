package com.teleexpertise.model.web;

import com.teleexpertise.model.entities.Patient;
import com.teleexpertise.model.enums.RoleEnum;
import com.teleexpertise.model.service.InfirmierService; // Réutilisation du service de liste
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/generaliste/liste_patients")
public class GeneralistePatientListServlet extends HttpServlet {

    // Réutilisation du service qui récupère et trie les patients
    private final InfirmierService infirmierService = new InfirmierService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Vérification de sécurité (Généraliste seulement)
        if (!RoleEnum.GENERALISTE.equals(request.getSession().getAttribute("utilisateurRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès réservé au Médecin Généraliste.");
            return;
        }

        // 2. Récupération des patients en file d'attente
        List<Patient> patients = infirmierService.getPatientsDuJour();

        // 3. Passage des données à la JSP
        request.setAttribute("patients", patients);

        // 4. Redirection vers la vue
        request.getRequestDispatcher("/WEB-INF/generaliste/liste_patients_gen.jsp").forward(request, response);
    }
}