<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Répondre à l'Expertise (US8)</title>
    <style>
        .patient-info, .dossier-details { border: 1px solid #ccc; padding: 15px; margin-bottom: 20px; }
        .alert-urgent { color: red; font-weight: bold; }
    </style>
</head>
<body>
<c:set var="demande" value="${requestScope.demande}" />
<c:set var="patient" value="${demande.consultation.patient}" />

<h1>Réponse à la Demande #${demande.id}</h1>

<c:if test="${not empty requestScope.error}"><p style="color: red;">Erreur: ${requestScope.error}</p></c:if>

<p class="${demande.priorite == 'URGENTE' ? 'alert-urgent' : ''}">
    Priorité : ${demande.priorite} | Statut : ${demande.statut}
</p>

<div class="patient-info">
    <h2>Dossier Patient : ${patient.nom} ${patient.prenom}</h2>
    <p>N° Sécu : ${patient.numSecuriteSociale} | Né(e) : ${patient.dateNaissance}</p>

    <h3>Derniers Signes Vitaux :</h3>
    <c:set var="svListSize" value="${patient.signesVitauxList.size()}" />
    <c:if test="${svListSize > 0}">
        <c:set var="latestSv" value="${patient.signesVitauxList[svListSize - 1]}" />
        <p>Tension: ${latestSv.tensionArterielle} | FC: ${latestSv.frequenceCardiaque} | Temp: ${latestSv.temperature}°C</p>
    </c:if>
</div>

<div class="dossier-details">
    <h3>Motif de Consultation (Généraliste)</h3>
    <p>Motif: ${demande.consultation.motif}</p>
    <p>Observations: ${demande.consultation.observations}</p>

    <h3>Question Posée (Étape 5)</h3>
    <p><strong>Question:</strong> ${demande.questionPosee}</p>
</div>

<hr>

<c:choose>
    <c:when test="${demande.statut == 'TERMINEE'}">
        <h2>Avis Déjà Fourni :</h2>
        <p><strong>Avis :</strong> ${demande.avisSpecialiste}</p>
        <p><strong>Recommandations :</strong> ${demande.recommandations}</p>
    </c:when>
    <c:otherwise>
        <form method="POST" action="${pageContext.request.contextPath}/specialiste/repondre_expertise">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />
            <input type="hidden" name="demandeId" value="${demande.id}">

            <h2>4. Fournir l'Avis d'Expert (US8)</h2>
            <label for="avis">Avis Médical / Diagnostic :</label><br>
            <textarea id="avis" name="avis" rows="5" required></textarea><br><br>

            <label for="recommandations">Recommandations (Traitement / Suivi) :</label><br>
            <textarea id="recommandations" name="recommandations" rows="4"></textarea><br><br>

            <button type="submit">Enregistrer l'Avis et Clôturer la Demande</button>
        </form>
    </c:otherwise>
</c:choose>

<p><a href="${pageContext.request.contextPath}/specialiste/demandes_expertise">Retour à la Liste des Demandes</a></p>

</body>
</html>