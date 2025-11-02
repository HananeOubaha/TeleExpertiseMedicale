<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Configuration Profil - Spécialiste (US5)</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f7f9;
            padding: 20px;
            color: #333;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            background-color: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            border-top: 5px solid #17a2b8; /* Cyan Thématique */
        }
        h1 {
            color: #17a2b8;
            font-size: 1.8em;
            border-bottom: 2px solid #e9ecef;
            padding-bottom: 10px;
            margin-bottom: 20px;
        }
        h2 {
            color: #555;
            font-size: 1.4em;
            margin-top: 20px;
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-top: 15px;
            font-weight: bold;
            color: #333;
            margin-bottom: 5px;
        }
        input[type="number"], select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ced4da;
            border-radius: 4px;
            box-sizing: border-box;
        }

        /* Bouton d'Action Principal */
        .btn-submit {
            background-color: #28a745; /* Vert pour l'action principale */
            color: white;
            padding: 12px 25px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 1.1em;
            font-weight: bold;
            margin-top: 30px;
            width: 100%;
            transition: background-color 0.3s;
        }
        .btn-submit:hover {
            background-color: #218838;
        }

        /* Messages */
        .msg-error { color: #dc3545; background-color: #f8d7da; border: 1px solid #f5c6cb; padding: 10px; border-radius: 5px; margin-bottom: 15px; font-weight: bold; }
        .msg-success { color: #155724; background-color: #d4edda; border: 1px solid #c3e6cb; padding: 10px; border-radius: 5px; margin-bottom: 15px; font-weight: bold; }

        .logout-link {
            color: #dc3545;
            text-decoration: none;
            font-weight: bold;
            margin-top: 20px;
            display: inline-block;
        }
    </style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
<div class="container">
    <h1><i class="fas fa-cog"></i> Configuration du Profil Spécialiste</h1>

    <c:if test="${not empty requestScope.error}"><p class="msg-error"><i class="fas fa-exclamation-triangle"></i> Erreur: ${requestScope.error}</p></c:if>
    <c:if test="${not empty param.message}"><p class="msg-success"><i class="fas fa-check-circle"></i> ${param.message}</p></c:if>

    <form method="POST" action="${pageContext.request.contextPath}/specialiste/profile">
        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />

        <h2><i class="fas fa-info-circle"></i> Informations du Profil (US5)</h2>

        <label for="specialite"><i class="fas fa-clipboard-list"></i> Spécialité :</label>
        <select id="specialite" name="specialite" required>
            <c:forEach var="spec" items="${requestScope.specialities}">
                <option value="${spec}"
                        <c:if test="${spec == requestScope.specialist.specialite}">selected</c:if>>
                        ${spec}
                </option>
            </c:forEach>
        </select>

        <label for="tarif"><i class="fas fa-euro-sign"></i> Tarif de Consultation (DH) :</label>
        <input type="number" id="tarif" name="tarif" required
               value="${requestScope.specialist.tarifConsultation}" min="100">

        <p style="font-size: 0.9em; color: #6c757d; margin-top: 5px;">
            *La durée moyenne de consultation est fixée à 30 minutes.
        </p>

        <button type="submit" class="btn-submit">
            <i class="fas fa-calendar-alt"></i> Sauvegarder et Générer Créneaux
        </button>
    </form>

    <hr style="margin-top: 30px;">

    <a href="${pageContext.request.contextPath}/logout" class="logout-link">
        <i class="fas fa-sign-out-alt"></i> Se Déconnecter
    </a>
</div>
</body>
</html>