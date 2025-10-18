<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Tableau de Bord Généraliste</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa; /* Fond léger */
            padding: 20px;
            color: #333;
        }
        .container {
            max-width: 900px;
            margin: 0 auto;
            background-color: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            border-top: 5px solid #007bff; /* Bleu Thématique */
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 2px solid #e9ecef;
            padding-bottom: 15px;
            margin-bottom: 30px;
        }
        h1 {
            color: #007bff;
            font-size: 1.8em;
        }
        .user-info {
            font-size: 0.9em;
            color: #6c757d;
            text-align: right;
        }
        .user-role {
            font-weight: bold;
            color: #28a745;
        }
        .action-area {
            margin-top: 30px;
        }
        .action-link {
            display: inline-block;
            background-color: #007bff; /* Bleu pour l'action principale */
            color: white;
            padding: 15px 30px;
            border-radius: 8px;
            text-decoration: none;
            font-size: 1.2em;
            font-weight: bold;
            transition: background-color 0.3s, transform 0.2s;
        }
        .action-link:hover {
            background-color: #0056b3;
            transform: translateY(-2px);
        }
        .message {
            padding: 10px;
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .logout {
            color: #dc3545;
            text-decoration: none;
            margin-top: 20px;
            display: inline-block;
        }
    </style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
<div class="container">
    <div class="header">
        <h1><i class="fas fa-stethoscope"></i> Tableau de Bord Généraliste</h1>
        <div class="user-info">
            <p>Authentification réussie en tant que : <span class="user-role"><i class="fas fa-user-md"></i> <%= session.getAttribute("utilisateurRole") %></span></p>
        </div>
    </div>

    <c:if test="${not empty param.message}">
        <p class="message"><i class="fas fa-check-circle"></i> ${param.message}</p>
    </c:if>

    <h2>Bienvenue, Dr. Martin (ou votre nom)</h2>
    <p>Votre rôle dans le système est de gérer les consultations et d'initier les demandes de télé-expertise.</p>

    <div class="action-area">
        <a href="${pageContext.request.contextPath}/generaliste/liste_patients" class="action-link">
            <i class="fas fa-users"></i> Gérer la File d'Attente et Démarrer Consultation (US-GEN-1)
        </a>
    </div>

    <a href="${pageContext.request.contextPath}/logout" class="logout">
        <i class="fas fa-sign-out-alt"></i> Se Déconnecter
    </a>
</div>
</body>
</html>