<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>
        <c:choose>
            <c:when test="${not empty requestScope.consultation}">Éditer Consultation #${requestScope.consultation.id}</c:when>
            <c:otherwise>Démarrer Nouvelle Consultation</c:otherwise>
        </c:choose>
    </title>
    <style>
        .acte-list { border: 1px solid #ccc; padding: 10px; margin-bottom: 20px; }
        .acte-list div { margin-bottom: 5px; }
        textarea, input[type="text"] { width: 100%; box-sizing: border-box; }
    </style>
</head>
<body>
<c:set var="patient" value="${requestScope.patient}" />
<c:set var="consultation" value="${requestScope.consultation}" />
<c:set var="actesDisponibles" value="${requestScope.actesDisponibles}" />

<h1>
    Consultation pour ${patient.nom} ${patient.prenom}
    <c:if test="${not empty consultation}">
        (ID #${consultation.id} | Statut: ${consultation.statut})
        <c:if test="${not empty requestScope.message}"><span style="color: green;"> - ${requestScope.message}</span></c:if>
    </c:if>
</h1>
<p>N° Sécu: ${patient.numSecuriteSociale} | Arrivée: ${patient.dateArrivee}</p>

<a href="${pageContext.request.contextPath}/generaliste/liste_patients">Retour à la File d'Attente</a>

<c:if test="${not empty requestScope.error}"><p style="color: red;">Erreur: ${requestScope.error}</p></c:if>

<form method="POST" action="${pageContext.request.contextPath}/generaliste/creer_consultation">
    <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />
    <input type="hidden" name="patientId" value="${patient.id}">

    <c:if test="${not empty consultation}">
        <input type="hidden" name="consultationId" value="${consultation.id}">
    </c:if>

    <h2>1. Observations Générales</h2>
    <label for="motif">Motif de Consultation (Ce que dit le patient):</label><br>
    <textarea id="motif" name="motif" rows="3" required>${consultation.motif}</textarea><br><br>

    <label for="observations">Observations Cliniques (Examen physique):</label><br>
    <textarea id="observations" name="observations" rows="5" required>${consultation.observations}</textarea><br><br>

    <h2>2. Actes Techniques & Examens Complémentaires</h2>
    <div class="acte-list">
        <c:forEach var="acte" items="${actesDisponibles}">
            <div>
                <input type="checkbox"
                       id="acte${acte.id}"
                       name="actes"
                       value="${acte.id}"

                    <%-- Logique pour cocher si l'acte est déjà dans la liste de la consultation --%>
                <c:if test="${not empty consultation.actes}">
                <c:forEach var="consActe" items="${consultation.actes}">
                       <c:if test="${consActe.id == acte.id}">checked</c:if>
                </c:forEach>
                </c:if>
                >
                <label for="acte${acte.id}">
                        ${acte.nom} (Coût: ${acte.cout} DH)
                </label>
            </div>
        </c:forEach>
    </div>
    <p>
        **Note:** La consultation de base est de **150 DH**. Le coût total sera calculé à la clôture (US4).
    </p>

    <hr>

    <h2>3. Décision de Prise en Charge (Scénario A)</h2>

    <label for="diagnostic">Diagnostic établi (Si prise en charge directe):</label><br>
    <input type="text" id="diagnostic" name="diagnostic" value="${consultation.diagnostic}"><br><br>

    <label for="prescription">Prescription / Traitement:</label><br>
    <textarea id="prescription" name="prescription" rows="4">${consultation.prescription}</textarea><br><br>

    <button type="submit" name="action" value="cloturer">Clôturer la Consultation (Scénario A)</button>
    <button type="submit" name="action" value="sauvegarder">Sauvegarder et Continuer (non clôturée)</button>

</form>

</body>
</html>