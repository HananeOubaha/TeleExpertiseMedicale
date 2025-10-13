package com.teleexpertise.model.web;

import com.teleexpertise.model.entities.Patient;
import com.teleexpertise.model.entities.SignesVitaux;
import com.teleexpertise.model.enums.RoleEnum;
import com.teleexpertise.model.service.InfirmierService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/infirmier/accueil", "/infirmier/dashboard"})
public class InfirmierAccueilServlet extends HttpServlet {

    private final InfirmierService infirmierService = new InfirmierService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // (Sécurité simple - sera améliorée avec un Filter)
        if (!RoleEnum.INFIRMIER.equals(request.getSession().getAttribute("utilisateurRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès réservé à l'Infirmier.");
            return;
        }

        // Afficher la page d'accueil (avec recherche)
        request.getRequestDispatcher("/WEB-INF/infirmier/accueil_patient.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String numSecu = request.getParameter("numSecu");

        // 🚨 VÉRIFICATION DE SÉCURITÉ CRITIQUE : Bloquer les chaînes vides 🚨
        if (numSecu == null || numSecu.trim().isEmpty()) {
            request.setAttribute("error", "Le Numéro de Sécurité Sociale est obligatoire.");
            request.getRequestDispatcher("/WEB-INF/infirmier/accueil_patient.jsp").forward(request, response);
            return;
        }

        Patient patient = null;

        try {
            if ("search".equals(action)) {
                // US1 - Recherche
                patient = infirmierService.rechercherPatient(numSecu.trim()); // Utiliser trim() pour nettoyer

                if (patient != null) {
                    request.setAttribute("patient", patient);
                    request.setAttribute("message", "Patient trouvé. Saisissez les nouveaux signes vitaux.");
                } else {
                    request.setAttribute("message", "Nouveau patient.");
                    request.setAttribute("numSecu", numSecu.trim());
                }

            } else if ("save".equals(action)) {
                // US1 - Sauvegarde/Mise à jour

                String existingId = request.getParameter("patientId");

                // 1. Récupérer ou créer l'entité Patient
                if (existingId != null && !existingId.isEmpty()) {
                    // Cas 1: Patient existant (recherché par le formulaire précédent)
                    patient = infirmierService.rechercherPatient(numSecu.trim());
                    // Vérification de sécurité supplémentaire (très important !)
                    if (patient == null) {
                        throw new RuntimeException("Patient introuvable pour l'ID de mise à jour.");
                    }
                } else {
                    // Cas 2: Nouveau patient
                    patient = new Patient();
                    patient.setNom(request.getParameter("nom"));
                    patient.setPrenom(request.getParameter("prenom"));
                    patient.setDateNaissance(request.getParameter("dateNaissance"));
                    patient.setNumSecuriteSociale(numSecu.trim()); // Sauvegarde la valeur nettoyée
                    patient.setTelephone(request.getParameter("telephone"));
                    patient.setAdresse(request.getParameter("adresse"));
                }

                // 2. Création et liaison des Signes Vitaux
                SignesVitaux sv = new SignesVitaux();
                sv.setTensionArterielle(request.getParameter("tension"));
                sv.setFrequenceCardiaque(parseInteger(request.getParameter("fc")));
                sv.setTemperature(parseDouble(request.getParameter("temp")));
                sv.setFrequenceRespiratoire(parseInteger(request.getParameter("fr")));
                sv.setPoids(parseDouble(request.getParameter("poids")));
                sv.setTaille(parseDouble(request.getParameter("taille")));

                // 3. Sauvegarde (persiste Patient et Signes Vitaux)
                patient = infirmierService.accueillirPatient(patient, sv);

                // Redirection vers la liste d'attente (US2)
                response.sendRedirect(request.getContextPath() + "/infirmier/patients_du_jour?message=Patient " + patient.getNom() + " ajouté à la file d'attente.");
                return;
            }
        } catch (Exception e) {
            // En cas d'erreur de base de données (comme une contrainte d'unicité)
            request.setAttribute("error", "Erreur d'enregistrement : " + e.getMessage());
            e.printStackTrace();
        }

        // Revenir à la vue d'accueil en cas d'erreur ou après la recherche
        request.getRequestDispatcher("/WEB-INF/infirmier/accueil_patient.jsp").forward(request, response);
    }

    // Utilitaires de conversion simples
    private Integer parseInteger(String s) {
        return (s != null && !s.trim().isEmpty()) ? Integer.parseInt(s.trim()) : null;
    }
    private Double parseDouble(String s) {
        return (s != null && !s.trim().isEmpty()) ? Double.parseDouble(s.trim()) : null;
    }
}