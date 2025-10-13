<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Accueil Patient - Infirmier</title>
</head>
<body>
<c:set var="patient" value="${requestScope.patient}" />

<h1>Accueil Patient (US1)</h1>
<a href="${pageContext.request.contextPath}/logout">Déconnexion</a> |
<a href="${pageContext.request.contextPath}/infirmier/patients_du_jour">File d'Attente (US2)</a>

<c:if test="${not empty requestScope.error}"><p style="color: red;">Erreur: ${requestScope.error}</p></c:if>
<c:if test="${not empty requestScope.message}"><p style="color: green;">${requestScope.message}</p></c:if>

<h2>1. Recherche du Patient</h2>
<form method="POST" action="${pageContext.request.contextPath}/infirmier/accueil">
    <input type="hidden" name="action" value="search">
    <label for="numSecuSearch">Numéro de Sécurité Sociale:</label>
    <input type="text" id="numSecuSearch" name="numSecu" required value="${not empty requestScope.numSecu ? requestScope.numSecu : ''}">
    <button type="submit">Rechercher</button>
</form>

<hr>

<h2>2. Enregistrement / Saisie des Signes Vitaux</h2>

<form method="POST" action="${pageContext.request.contextPath}/infirmier/accueil">
    <input type="hidden" name="action" value="save">

    <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />

    <input type="hidden" name="patientId" value="${patient.id}">
    <input type="hidden" name="numSecu" value="${patient.numSecuriteSociale}">


    <c:choose>
        <c:when test="${patient.id == null}">
            <h3>Nouveau Patient (Entrée des données complètes)</h3>
            <label>Nom:</label><input type="text" name="nom" required><br>
            <label>Prénom:</label><input type="text" name="prenom" required><br>
            <label>Date Naissance:</label><input type="date" name="dateNaissance"><br>
            <label>Num Sécu:</label><input type="text" name="numSecu" required value="${requestScope.numSecu}"><br>
        </c:when>
        <c:otherwise>
            <h3>Patient Existant: ${patient.nom} ${patient.prenom} (NS: ${patient.numSecuriteSociale})</h3>
            <p>Anciens SV enregistrés: ${patient.signesVitauxList.size()}</p>
        </c:otherwise>
    </c:choose>

    <hr>

    <h3>Signes Vitaux (Nouveaux)</h3>
    <label>Tension Artérielle (mmHg):</label><input type="text" name="tension" required><br>
    <label>Fréquence Cardiaque (bpm):</label><input type="number" name="fc" required><br>
    <label>Température (°C):</label><input type="text" name="temp" required><br>
    <label>Fréquence Respiratoire (cycles/min):</label><input type="number" name="fr" required><br>
    <label>Poids (kg):</label><input type="text" name="poids"><br>
    <label>Taille (cm):</label><input type="text" name="taille"><br>

    <button type="submit">Enregistrer et Mettre en File d'Attente</button>
</form>

</body>
</html>