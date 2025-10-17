<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sélection Créneau et Demande d'Expertise</title>
    <style>
        .creneau-list { border: 1px solid #ddd; padding: 10px; margin-top: 15px; }
        .creneau-list div { margin-bottom: 5px; }
        .synchrone { border-left: 5px solid blue; padding-left: 10px; }
        .asynchrone { border-left: 5px solid gray; padding-left: 10px; }
    </style>
</head>
<body>
<c:set var="consultation" value="${requestScope.consultation}" />
<c:set var="specialist" value="${requestScope.specialist}" />

<h1>Demande d'Expertise: Dr. ${specialist.nom} (${specialist.specialite})</h1>
<p>Patient: ${consultation.patient.nom} ${consultation.patient.prenom} | Consultation ID: ${consultation.id}</p>

<c:if test="${not empty requestScope.error}"><p style="color: red;">Erreur: ${requestScope.error}</p></c:if>

<form method="POST" action="${pageContext.request.contextPath}/generaliste/selection_creneau">
    <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />
    <input type="hidden" name="consultationId" value="${consultation.id}">
    <input type="hidden" name="specialisteId" value="${specialist.id}">

    <h2>1. Question et Priorité</h2>
    <label for="priorite">Niveau de Priorité (Étape 5) :</label>
    <select id="priorite" name="priorite" required>
        <c:forEach var="p" items="${requestScope.priorities}">
            <option value="${p}" <c:if test="${p == 'NORMALE'}">selected</c:if>>
                    ${p}
            </option>
        </c:forEach>
    </select>
    <br><br>

    <label for="question">Question Posée au Spécialiste :</label><br>
    <textarea id="question" name="question" rows="5" required></textarea>
    <br>
    <p>*(Vous devez également transmettre des données et analyses pertinentes, non implémenté dans le formulaire HTML pour la concision).*</p>

    <hr>

    <h2>2. Modalité d'Échange (Synchrone ou Asynchrone)</h2>

    <div class="creneau-list synchrone">
        <h3>Télé-expertise Synchrone (Visioconférence)</h3>
        <c:choose>
            <c:when test="${empty requestScope.creneauxDisponibles}">
                <p>Aucun créneau disponible pour ce spécialiste dans les 7 prochains jours.</p>
            </c:when>
            <c:otherwise>
                <label>Sélectionner un Créneau (Facultatif) :</label>
                <select name="creneau">
                    <option value="">-- Asynchrone (Réponse sous 24-48h) --</option>
                    <c:forEach var="creneau" items="${requestScope.creneauxDisponibles}">
                        <option value="${creneau.id}">
                            <fmt:formatDate value="${creneau.debutDate}" pattern="dd MMM à HH:mm"/> - (Tarif: ${specialist.tarifConsultation} DH)
                        </option>
                    </c:forEach>
                </select>
            </c:otherwise>
        </c:choose>
    </div>

    <hr>

    <button type="submit" name="action" value="soumettre_demande">Soumettre la Demande d'Expertise</button>
</form>

<p><a href="${pageContext.request.contextPath}/generaliste/rechercher_specialiste?consultationId=${consultation.id}">Retour à la Recherche Spécialiste</a></p>

</body>
</html>