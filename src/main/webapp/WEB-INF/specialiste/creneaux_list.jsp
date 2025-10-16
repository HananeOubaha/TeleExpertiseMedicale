<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Mes Créneaux - Spécialiste (US6)</title>
    <style>
        .dispo { color: green; font-weight: bold; }
        .reserve { color: orange; font-weight: bold; }
        .passe { color: gray; text-decoration: line-through; }
    </style>
</head>
<body>
<h1>Mes Créneaux Horaires (US6)</h1>

<a href="${pageContext.request.contextPath}/specialiste/dashboard">Tableau de Bord</a> |
<a href="${pageContext.request.contextPath}/logout">Déconnexion</a>

<c:if test="${not empty requestScope.error}"><p style="color: red;">Erreur: ${requestScope.error}</p></c:if>

<c:choose>
    <c:when test="${empty requestScope.creneaux}">
        <p>Aucun créneau n'est défini. Veuillez aller dans "Modifier mon Profil" pour les générer.</p>
    </c:when>
    <c:otherwise>
        <table>
            <thead>
            <tr>
                <th>Date</th>
                <th>Heure</th>
                <th>Statut</th>
                <th>Consultation</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="creneau" items="${requestScope.creneaux}">

                <tr class="${creneau.estPasse() ? 'passe' : (creneau.estDisponible ? 'dispo' : 'reserve')}">

                    <td>
                        <fmt:formatDate value="${creneau.debutDate}" pattern="E, dd MMM"/>
                    </td>
                    <td>
                        <fmt:formatDate value="${creneau.debutDate}" pattern="HH:mm"/>
                        -
                        <fmt:formatDate value="${creneau.finDate}" pattern="HH:mm"/>
                    </td>

                    <td>
                        <c:choose>
                            <c:when test="${creneau.estPasse()}">Archivé</c:when>
                            <c:when test="${creneau.estDisponible}">Disponible ✓</c:when>
                            <c:otherwise>Réservé (En Attente)</c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:if test="${!creneau.estDisponible && !creneau.estPasse()}">
                            Réservé par le Généraliste
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>
</body>
</html>