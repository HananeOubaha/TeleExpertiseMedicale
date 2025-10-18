<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Connexion - Télé-expertise Médicale</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f7f9; /* Fond très clair */
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .login-container {
            background-color: white;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
            width: 380px;
            text-align: center;
            border-top: 5px solid #007bff; /* Bordure thématique */
        }
        h1 {
            color: #007bff; /* Bleu Thématique */
            margin-bottom: 30px;
            font-size: 2em;
        }
        label {
            display: block;
            text-align: left;
            margin-bottom: 5px;
            color: #495057;
            font-weight: 600;
            font-size: 0.95em;
        }
        input[type="email"], input[type="password"] {
            width: 100%;
            padding: 12px;
            margin-bottom: 20px;
            border: 1px solid #ced4da;
            border-radius: 6px;
            box-sizing: border-box;
            transition: border-color 0.3s;
        }
        input[type="email"]:focus, input[type="password"]:focus {
            border-color: #007bff;
            outline: none;
        }
        button {
            background-color: #28a745; /* Vert pour l'action principale */
            color: white;
            padding: 12px 15px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            width: 100%;
            font-size: 1.1em;
            font-weight: bold;
            transition: background-color 0.3s;
        }
        button:hover {
            background-color: #218838;
        }
        .error {
            color: #dc3545;
            margin-bottom: 15px;
            font-weight: bold;
            border: 1px solid #f5c6cb;
            padding: 10px;
            background-color: #f8d7da;
            border-radius: 4px;
        }
        /* AJOUT DU STYLE POUR LE MESSAGE DE SUCCÈS */
        .message-success {
            color: #155724;
            background-color: #d4edda;
            border: 1px solid #c3e6cb;
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 15px;
            font-weight: bold;
        }
        .icon {
            margin-right: 8px;
        }
    </style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
<div class="login-container">
    <h1><i class="fas fa-user-lock icon"></i> Accès Professionnel</h1>

    <%
        // Récupération des messages
        String successMessage = request.getParameter("message");
        String errorMessage = (String) request.getAttribute("error");

        // 1. Affichage du message de succès (après déconnexion)
        if (successMessage != null) {
    %>
    <p class="message-success"><i class="fas fa-check-circle icon"></i> <%= successMessage %></p>
    <%
        }

        // 2. Affichage de l'erreur (après échec de login)
        if (errorMessage != null) {
    %>
    <p class="error"><i class="fas fa-exclamation-triangle icon"></i> ERREUR : <%= errorMessage %></p>
    <%
        }
    %>

    <form method="POST" action="doLogin">
        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />

        <div>
            <label for="email"><i class="fas fa-envelope"></i> Email :</label>
            <input type="email" id="email" name="email" required placeholder="nom.prenom@hopital.com">
        </div>
        <div>
            <label for="password"><i class="fas fa-lock"></i> Mot de passe :</label>
            <input type="password" id="password" name="password" required>
        </div>
        <button type="submit"><i class="fas fa-sign-in-alt icon"></i> Se connecter</button>
    </form>

    <p style="margin-top: 25px; font-size: 0.9em;">
        <a href="${pageContext.request.contextPath}/" style="color: #007bff; text-decoration: none;">
            <i class="fas fa-home icon"></i> Retour à l'accueil
        </a>
    </p>
</div>
</body>
</html>