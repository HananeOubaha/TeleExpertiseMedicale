<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>File d'Attente - US2</title>
    <style>
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>
<h1>File d'Attente des Patients (US2)</h1>

<a href="${pageContext.request.contextPath}/infirmier/accueil">Retour à l'Accueil</a> |
<a href="${pageContext.request.contextPath}/logout">Déconnexion</a>

<c:if test="${not empty requestScope.message}">
    <p style="color: green;">${requestScope.message}</p>
</c:if>

<c:choose>
    <c:when test="${empty requestScope.patients}">
        <p>Aucun patient dans la file d'attente pour le moment.</p>
    </c:when>
    <c:otherwise>
        <table>
            <thead>
            <tr>
                <th>Nom & Prénom</th>
                <th>N° Sécurité Sociale</th>
                <th>Heure d'Arrivée</th>
                <th>Tension (Dernière)</th>
                <th>FC (Dernière)</th>
                <th>Température (Dernière)</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="patient" items="${requestScope.patients}">
                <tr>
                    <td>${patient.nom} ${patient.prenom}</td>
                    <td>${patient.numSecuriteSociale}</td>
                    <td>
                        <fmt:formatDate value="${patient.dateArrivee}" pattern="HH:mm:ss"/>
                    </td>

                    <c:set var="svListSize" value="${patient.signesVitauxList.size()}" />

                    <c:choose>
                        <c:when test="${svListSize > 0}">
                            <c:set var="latestSv" value="${patient.signesVitauxList[svListSize - 1]}" />
                            <td>${latestSv.tensionArterielle}</td>
                            <td>${latestSv.frequenceCardiaque}</td>
                            <td>${latestSv.temperature}°C</td>
                        </c:when>
                        <c:otherwise>
                            <td>N/A</td>
                            <td>N/A</td>
                            <td>N/A</td>
                        </c:otherwise>
                    </c:choose>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>

</body>
</html>