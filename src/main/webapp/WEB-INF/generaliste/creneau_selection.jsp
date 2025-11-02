<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Soumettre Demande d'Expertise</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f7f9; padding: 20px; color: #333; }
        .container { max-width: 800px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1); }
        h1 { color: #007bff; font-size: 1.8em; border-bottom: 2px solid #007bff; padding-bottom: 10px; margin-bottom: 20px; }
        h2 { color: #555; font-size: 1.3em; margin-top: 20px; margin-bottom: 15px; }
        h3 { color: #007bff; font-size: 1.1em; margin-top: 5px; }

        label { display: block; margin-top: 10px; font-weight: bold; color: #333; }
        textarea, select { width: 100%; padding: 10px; margin-top: 5px; margin-bottom: 15px; border: 1px solid #ced4da; border-radius: 4px; box-sizing: border-box; }

        /* Styles spécifiques aux modalités */
        .creneau-list { border: 1px solid #e9ecef; background-color: #f8f9fa; padding: 15px; margin-top: 15px; border-radius: 6px; }
        .synchrone { border-left: 5px solid #17a2b8; } /* Cyan pour synchrone */

        /* Bouton de Soumission */
        .btn-submit {
            display: block;
            width: 100%;
            background-color: #28a745; /* Vert pour l'action principale */
            color: white;
            padding: 12px 15px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 1.1em;
            font-weight: bold;
            margin-top: 20px;
            transition: background-color 0.3s;
        }
        .btn-submit:hover { background-color: #218838; }
        .error { color: #dc3545; font-weight: bold; }
    </style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
<div class="container">
    <c:set var="consultation" value="${requestScope.consultation}" />
    <c:set var="specialist" value="${requestScope.specialist}" />

    <h1><i class="fas fa-user-tie"></i> Demande d'Expertise: Dr. ${specialist.nom} (${specialist.specialite})</h1>
    <p>Patient: <strong>${consultation.patient.nom} ${consultation.patient.prenom}</strong> | Consultation ID: ${consultation.id}</p>

    <c:if test="${not empty requestScope.error}"><p class="error"><i class="fas fa-exclamation-triangle"></i> Erreur: ${requestScope.error}</p></c:if>

    <form method="POST" action="${pageContext.request.contextPath}/generaliste/selection_creneau">
        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />
        <input type="hidden" name="consultationId" value="${consultation.id}">
        <input type="hidden" name="specialisteId" value="${specialist.id}">

        <h2>1. <i class="fas fa-question-circle"></i> Question et Priorité</h2>
        <label for="priorite">Niveau de Priorité (Étape 5) :</label>
        <select id="priorite" name="priorite" required>
            <c:forEach var="p" items="${requestScope.priorities}">
                <option value="${p}"
                        <c:if test="${p == 'NORMALE'}">selected</c:if>
                        <c:if test="${p == 'URGENTE'}">style="color: red;"</c:if>>
                        ${p}
                </option>
            </c:forEach>
        </select>
        <br>

        <label for="question">Question Posée au Spécialiste :</label>
        <textarea id="question" name="question" rows="5" required placeholder="Ex: Confirmez-vous le diagnostic de pneumonie ou suspectez-vous autre chose ?"></textarea>

        <p style="font-size: 0.9em; color: #6c757d;">*(Inclure les données d'analyse est sous-entendu par le contenu du dossier patient.)*</p>

        <hr>

        <h2>2. <i class="fas fa-calendar-alt"></i> Modalité d'Échange</h2>

        <div class="creneau-list synchrone">
            <h3>Télé-expertise Synchrone (Visioconférence)</h3>
            <c:choose>
                <c:when test="${empty requestScope.creneauxDisponibles}">
                    <p class="error">🔴 Aucun créneau disponible pour ce spécialiste.</p>
                </c:when>
                <c:otherwise>
                    <label>Sélectionner un Créneau (Recommandé pour Urgence) :</label>
                    <select name="creneau">
                        <option value="">-- Asynchrone (Réponse sous 24-48h) --</option>
                        <c:forEach var="creneau" items="${requestScope.creneauxDisponibles}">
                            <option value="${creneau.id}">
                                <fmt:formatDate value="${creneau.debutDate}" pattern="dd MMM à HH:mm"/> - (Tarif: ${specialist.tarifConsultation} DH)
                            </option>
                        </c:forEach>
                    </select>
                    <p style="margin-top: 10px; font-size: 0.9em;">*Si vous ne sélectionnez pas de créneau, la demande est traitée en asynchrone.</p>
                </c:otherwise>
            </c:choose>
        </div>

        <button type="submit" name="action" value="soumettre_demande" class="btn-submit">
            <i class="fas fa-paper-plane"></i> Soumettre la Demande d'Expertise
        </button>
    </form>

    <p style="margin-top: 20px;">
        <a href="${pageContext.request.contextPath}/generaliste/rechercher_specialiste?consultationId=${consultation.id}" style="color: #6c757d;">
            <i class="fas fa-undo"></i> Retour à la Recherche Spécialiste
        </a>
    </p>
</div>
</body>
</html>