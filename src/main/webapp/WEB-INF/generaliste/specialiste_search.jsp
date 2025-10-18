<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Recherche Spécialiste (US3)</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f7f9; padding: 20px; color: #333; }
        .container { max-width: 1000px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1); border-top: 5px solid #007bff; }
        h1 { color: #007bff; font-size: 1.8em; border-bottom: 2px solid #e9ecef; padding-bottom: 10px; margin-bottom: 20px; }
        h2 { color: #555; font-size: 1.4em; margin-top: 25px; margin-bottom: 15px; }

        /* Styles du Filtre */
        .filter-box { background-color: #e9f0f7; padding: 15px; border-radius: 6px; margin-bottom: 20px; }
        label { font-weight: bold; margin-right: 10px; color: #333; }
        select { padding: 8px; border: 1px solid #ced4da; border-radius: 4px; }

        /* Styles du Tableau de Résultats */
        table { border-collapse: collapse; width: 100%; margin-top: 15px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05); }
        th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
        th { background-color: #007bff; color: white; font-weight: bold; }
        tr:nth-child(even) { background-color: #f8f9fa; }

        /* Boutons d'Action */
        .btn-creneau {
            display: inline-block;
            background-color: #28a745; /* Vert pour l'action de sélection */
            color: white;
            padding: 5px 12px;
            border-radius: 4px;
            text-decoration: none;
            font-size: 0.9em;
            transition: background-color 0.3s;
        }
        .btn-creneau:hover { background-color: #218838; }
        .error { color: #dc3545; font-weight: bold; margin-top: 15px; }
    </style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
<div class="container">
    <h1><i class="fas fa-search-plus"></i> Recherche de Spécialistes</h1>
    <p style="color: #007bff;">Consultation en cours ID: <strong>${requestScope.consultationId}</strong></p>

    <a href="${pageContext.request.contextPath}/generaliste/creer_consultation?consultationId=${requestScope.consultationId}" style="color: #6c757d; text-decoration: none;">
        <i class="fas fa-arrow-left"></i> Retour à la Consultation
    </a>

    <c:if test="${not empty requestScope.error}"><p class="error"><i class="fas fa-exclamation-triangle"></i> Erreur: ${requestScope.error}</p></c:if>

    <div class="filter-box">
        <form method="GET" action="${pageContext.request.contextPath}/generaliste/rechercher_specialiste">
            <input type="hidden" name="consultationId" value="${requestScope.consultationId}">

            <label for="specialite"><i class="fas fa-filter"></i> Filtrer par Spécialité :</label>
            <select id="specialite" name="specialite" onchange="this.form.submit()">
                <option value="">-- Sélectionner une Spécialité --</option>
                <c:forEach var="spec" items="${requestScope.specialities}">
                    <option value="${spec}"
                            <c:if test="${spec == requestScope.specialiteFiltre}">selected</c:if>>
                            ${spec}
                    </option>
                </c:forEach>
            </select>
        </form>
    </div>

    <c:if test="${not empty requestScope.specialists}">
        <h2><i class="fas fa-users-md"></i> Spécialistes Disponibles (Triés par Tarif Croissant)</h2>
        <table>
            <thead>
            <tr>
                <th>Nom du Spécialiste</th>
                <th>Spécialité</th>
                <th>Tarif de Consultation (DH)</th>
                <th>Action</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="spec" items="${requestScope.specialists}">
                <tr>
                    <td>Dr. ${spec.nom} ${spec.prenom}</td>
                    <td>${spec.specialite}</td>
                    <td>${spec.tarifConsultation} DH</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/generaliste/selection_creneau?consultationId=${requestScope.consultationId}&specialisteId=${spec.id}" class="btn-creneau">
                            <i class="fas fa-calendar-alt"></i> Voir Créneaux
                        </a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
    <c:if test="${empty requestScope.specialists && not empty requestScope.specialiteFiltre}">
        <p style="margin-top: 20px;"><i class="fas fa-info-circle"></i> Aucun spécialiste trouvé pour la spécialité : **${requestScope.specialiteFiltre}**.</p>
    </c:if>
</div>
</body>
</html>