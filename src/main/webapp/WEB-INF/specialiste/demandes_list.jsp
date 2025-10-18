<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Demandes d'Expertise (US7)</title>
    <style>
        .urgent { color: red; font-weight: bold; }
        .normale { color: orange; }
        .terminee { color: green; }
    </style>
</head>
<body>
<h1>Liste des Demandes d'Expertise (US7)</h1>

<a href="${pageContext.request.contextPath}/specialiste/dashboard">Tableau de Bord</a> |
<a href="${pageContext.request.contextPath}/logout">Déconnexion</a>

<c:if test="${not empty requestScope.error}"><p style="color: red;">Erreur: ${requestScope.error}</p></c:if>

<form method="GET" action="${pageContext.request.contextPath}/specialiste/demandes_expertise">
    <label for="statut">Filtrer par statut :</label>
    <select id="statut" name="statut" onchange="this.form.submit()">
        <option value="">-- Tous les statuts --</option>
        <c:forEach var="statut" items="${requestScope.statutsDisponibles}">
            <option value="${statut}"
                    <c:if test="${statut == requestScope.statutSelectionne}">selected</c:if>>
                    ${statut}
            </option>
        </c:forEach>
    </select>
</form>

<c:choose>
    <c:when test="${empty requestScope.demandes}">
        <p>Aucune demande correspondante.</p>
    </c:when>
    <c:otherwise>
        <table>
            <thead>
            <tr>
                <th>ID Demande</th>
                <th>Patient</th>
                <th>Priorité</th>
                <th>Statut</th>
                <th>Date de Demande</th>
                <th>Action</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="demande" items="${requestScope.demandes}">
                <tr class="${demande.priorite == 'URGENTE' ? 'urgent' : (demande.statut == 'TERMINEE' ? 'terminee' : 'normale')}">
                    <td>${demande.id}</td>
                    <td>${demande.consultation.patient.nom} ${demande.consultation.patient.prenom}</td>
                    <td>${demande.priorite}</td>
                    <td>${demande.statut}</td>
                    <td>
                        <fmt:formatDate value="${demande.dateDemandeUtil}" pattern="dd/MM/yy HH:mm"/>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/specialiste/repondre_expertise?demandeId=${demande.id}">
                            Consulter / Répondre
                        </a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>
</body>
</html>