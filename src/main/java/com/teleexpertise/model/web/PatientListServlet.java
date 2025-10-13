package com.teleexpertise.model.web;

import com.teleexpertise.model.entities.Patient;
import com.teleexpertise.model.enums.RoleEnum;
import com.teleexpertise.model.service.InfirmierService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

// Mappe l'URL vers laquelle la Servlet d'Accueil redirige
@WebServlet("/infirmier/patients_du_jour")
public class PatientListServlet extends HttpServlet {

    private final InfirmierService infirmierService = new InfirmierService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Vérification de sécurité (Infirmier seulement)
        if (!RoleEnum.INFIRMIER.equals(request.getSession().getAttribute("utilisateurRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès réservé à l'Infirmier.");
            return;
        }

        // 2. Récupération des patients (US2)
        List<Patient> patients = infirmierService.getPatientsDuJour();

        // 3. Passage des données à la JSP
        request.setAttribute("patients", patients);

        // Affiche un message de succès (après un enregistrement)
        String message = request.getParameter("message");
        if (message != null) {
            request.setAttribute("message", message);
        }

        // 4. Redirection vers la vue
        request.getRequestDispatcher("/WEB-INF/infirmier/liste_patients.jsp").forward(request, response);
    }
}