<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Patients en Attente - Généraliste</title>
    <style>
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>
<h1>Patients en Attente de Consultation (US-GEN-1)</h1>

<a href="${pageContext.request.contextPath}/generaliste/dashboard">Tableau de Bord</a> |
<a href="${pageContext.request.contextPath}/logout">Déconnexion</a>

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
                <th>Dernière Temp.</th>
                <th>Action</th>
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
                    <td>
                        <c:set var="svListSize" value="${patient.signesVitauxList.size()}" />
                        <c:if test="${svListSize > 0}">
                            <c:set var="latestSv" value="${patient.signesVitauxList[svListSize - 1]}" />
                            ${latestSv.temperature}°C
                        </c:if>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/generaliste/creer_consultation?patientId=${patient.id}">
                            Démarrer Consultation
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