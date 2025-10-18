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
        body { font-family: Arial, sans-serif; background-color: #f4f7f9; padding: 20px; color: #333; }
        .container { max-width: 900px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1); }
        h1 { color: #007bff; font-size: 2em; border-bottom: 2px solid #007bff; padding-bottom: 10px; margin-bottom: 10px; }
        h2 { color: #555; border-bottom: 1px solid #ddd; padding-bottom: 5px; margin-top: 25px; font-size: 1.4em; }
        p { font-size: 0.95em; color: #6c757d; }
        label { display: block; margin-top: 10px; font-weight: bold; color: #333; }
        textarea, input[type="text"], select { width: 100%; padding: 10px; margin-top: 5px; margin-bottom: 15px; border: 1px solid #ced4da; border-radius: 4px; box-sizing: border-box; }
        .acte-list { border: 1px solid #e9ecef; background-color: #f8f9fa; padding: 15px; margin-bottom: 20px; border-radius: 6px; }
        .acte-list div { margin-bottom: 5px; }

        /* Boutons d'Action */
        .btn-group button {
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-weight: bold;
            margin-right: 15px;
            transition: opacity 0.2s;
        }
        .btn-group button:hover { opacity: 0.9; }

        .btn-cloturer { background-color: #dc3545; color: white; } /* Rouge pour Clôturer */
        .btn-sauvegarder { background-color: #ffc107; color: #333; } /* Jaune pour Sauvegarder */
        .btn-rechercher { background-color: #17a2b8; color: white; } /* Cyan pour Recherche Spécialiste */

        .error { color: #dc3545; border: 1px solid #f5c6cb; background-color: #f8d7da; padding: 10px; border-radius: 5px; margin-bottom: 15px; }
        .info-message { color: green; font-weight: bold; }
    </style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
<div class="container">
    <c:set var="patient" value="${requestScope.patient}" />
    <c:set var="consultation" value="${requestScope.consultation}" />
    <c:set var="actesDisponibles" value="${requestScope.actesDisponibles}" />

    <h1>
        <i class="fas fa-file-medical"></i> Consultation pour ${patient.nom} ${patient.prenom}
        <c:if test="${not empty consultation}">
            <span style="font-size: 0.7em;">(ID #${consultation.id} | Statut: ${consultation.statut})</span>
            <c:if test="${not empty requestScope.message}"><span class="info-message"> - <i class="fas fa-check"></i> ${requestScope.message}</span></c:if>
        </c:if>
    </h1>
    <p>N° Sécu: ${patient.numSecuriteSociale} | Arrivée: ${patient.dateArrivee}</p>

    <p><a href="${pageContext.request.contextPath}/generaliste/liste_patients"><i class="fas fa-arrow-left"></i> Retour à la File d'Attente</a></p>

    <c:if test="${not empty requestScope.error}"><p class="error"><i class="fas fa-exclamation-triangle"></i> Erreur: ${requestScope.error}</p></c:if>

    <form method="POST" action="${pageContext.request.contextPath}/generaliste/creer_consultation">
        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />
        <input type="hidden" name="patientId" value="${patient.id}">

        <c:if test="${not empty consultation}">
            <input type="hidden" name="consultationId" value="${consultation.id}">
        </c:if>

        <h2>1. <i class="fas fa-clipboard-list"></i> Observations Générales</h2>
        <label for="motif">Motif de Consultation (Ce que dit le patient):</label>
        <textarea id="motif" name="motif" rows="3" required>${consultation.motif}</textarea>

        <label for="observations">Observations Cliniques (Examen physique):</label>
        <textarea id="observations" name="observations" rows="5" required>${consultation.observations}</textarea>

        <h2>2. <i class="fas fa-microscope"></i> Actes Techniques & Examens Complémentaires</h2>
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

        <h2>3. <i class="fas fa-tag"></i> Décision de Prise en Charge (Scénario A)</h2>

        <label for="diagnostic">Diagnostic établi (Si prise en charge directe):</label>
        <input type="text" id="diagnostic" name="diagnostic" value="${consultation.diagnostic}">

        <label for="prescription">Prescription / Traitement:</label>
        <textarea id="prescription" name="prescription" rows="4">${consultation.prescription}</textarea>

        <div class="btn-group" style="margin-top: 25px;">
            <button type="submit" name="action" value="cloturer" class="btn-cloturer">
                <i class="fas fa-times-circle"></i> Clôturer la Consultation (Scénario A)
            </button>
            <button type="submit" name="action" value="sauvegarder" class="btn-sauvegarder">
                <i class="fas fa-save"></i> Sauvegarder et Continuer (Brouillon)
            </button>
        </div>
    </form>

    <hr style="margin-top: 30px;">

    <h2>4. <i class="fas fa-user-friends"></i> Demande d'Expertise (Scénario B)</h2>

    <c:if test="${not empty consultation.id}">
        <p>Si la situation nécessite l'avis d'un spécialiste pour confirmer le diagnostic ou la prise en charge :</p>

        <form method="GET" action="${pageContext.request.contextPath}/generaliste/rechercher_specialiste">
            <input type="hidden" name="consultationId" value="${consultation.id}">

            <label for="specialite_requise">Sélectionner la Spécialité requise :</label>
            <select id="specialite_requise" name="specialite" required>
                <option value="">-- Choisir une Spécialité --</option>
                    <%-- US-GEN-3: Liste des spécialités (Devrait être chargée dynamiquement par la Servlet pour être complet) --%>
                <option value="CARDIOLOGUE">Cardiologue</option>
                <option value="PNEUMOLOGUE">Pneumologue</option>
                <option value="DERMATOLOGUE">Dermatologue</option>
            </select>

            <button type="submit" name="action" value="chercher_specialiste" class="btn-rechercher">
                <i class="fas fa-search"></i> Rechercher Spécialiste
            </button>
        </form>
    </c:if>
    <c:if test="${empty consultation.id}">
        <p style="color: #6c757d;"><i class="fas fa-info-circle"></i> Veuillez sauvegarder la consultation (Brouillon) au moins une fois pour initier une demande d'avis spécialiste.</p>
    </c:if>
</div>
</body>
</html>