<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Créer Consultation - Généraliste</title>
</head>
<body>
<c:set var="patient" value="${requestScope.patient}" />

<h1>Démarrer Consultation pour ${patient.nom} ${patient.prenom}</h1>
<p>N° Sécu: ${patient.numSecuriteSociale} | Arrivée: ${patient.dateArrivee}</p>

<a href="${pageContext.request.contextPath}/generaliste/liste_patients">Retour à la File d'Attente</a>

<c:if test="${not empty requestScope.error}"><p style="color: red;">Erreur: ${requestScope.error}</p></c:if>

<form method="POST" action="${pageContext.request.contextPath}/generaliste/creer_consultation">
    <input type="hidden" name="patientId" value="${patient.id}">
    <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />

    <h2>1. Observations Générales</h2>
    <label for="motif">Motif de Consultation (Ce que dit le patient):</label><br>
    <textarea id="motif" name="motif" rows="3" required></textarea><br><br>

    <label for="observations">Observations Cliniques (Examen physique):</label><br>
    <textarea id="observations" name="observations" rows="5" required></textarea><br><br>

    <h2>2. Actes Techniques & Examens Complémentaires</h2>
    <p>Ajouter des actes (Radiographie, Analyse de sang, etc.) - Sera implémenté dans un formulaire dédié.</p>

    <hr>

    <h2>3. Décision de Prise en Charge (Scénario A)</h2>

    <label for="diagnostic">Diagnostic établi (Si prise en charge directe):</label><br>
    <input type="text" id="diagnostic" name="diagnostic"><br><br>

    <label for="prescription">Prescription / Traitement:</label><br>
    <textarea id="prescription" name="prescription" rows="4"></textarea><br><br>

    <button type="submit" name="action" value="cloturer">Clôturer la Consultation (Scénario A)</button>
    <button type="submit" name="action" value="sauvegarder">Sauvegarder et Continuer (non clôturée)</button>

</form>

</body>
</html>