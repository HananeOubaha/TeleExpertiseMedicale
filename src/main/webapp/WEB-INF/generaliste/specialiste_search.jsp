<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Recherche Spécialiste (US3)</title>
    <style>
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>
<h1>Recherche de Spécialistes</h1>
<p>Consultation ID: ${requestScope.consultationId}</p>

<c:if test="${not empty requestScope.error}"><p style="color: red;">Erreur: ${requestScope.error}</p></c:if>

<form method="GET" action="${pageContext.request.contextPath}/generaliste/rechercher_specialiste">
    <input type="hidden" name="consultationId" value="${requestScope.consultationId}">

    <label for="specialite">Filtrer par Spécialité :</label>
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

<hr>

<c:if test="${not empty requestScope.specialists}">
    <h2>Résultats (Triés par Tarif Croissant)</h2>
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
                    <a href="${pageContext.request.contextPath}/generaliste/selection_creneau?consultationId=${requestScope.consultationId}&specialisteId=${spec.id}">
                        Voir Créneaux
                    </a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>
<c:if test="${empty requestScope.specialists && not empty requestScope.specialiteFiltre}">
    <p>Aucun spécialiste trouvé pour la spécialité : ${requestScope.specialiteFiltre}.</p>
</c:if>

</body>
</html>