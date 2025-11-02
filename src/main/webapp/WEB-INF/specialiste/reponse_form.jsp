<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Répondre à la Demande #${demande.id} (US8)</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f7f9; padding: 20px; color: #333; }
        .container { max-width: 900px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1); border-top: 5px solid #17a2b8; /* Cyan Thématique */ }
        h1 { color: #17a2b8; font-size: 1.8em; border-bottom: 2px solid #e9ecef; padding-bottom: 10px; margin-bottom: 20px; }
        h2 { color: #555; font-size: 1.4em; margin-top: 20px; border-bottom: 1px solid #ddd; padding-bottom: 5px; }
        h3 { color: #007bff; font-size: 1.1em; margin-top: 15px; }

        /* Conteneurs d'information */
        .patient-info { background-color: #f8f9fa; border: 1px solid #ced4da; padding: 15px; border-radius: 6px; margin-bottom: 20px; }
        .dossier-details { background-color: #fff; border: 1px solid #e9ecef; padding: 15px; border-radius: 6px; margin-bottom: 20px; }

        /* Statuts */
        .alert-urgent { color: white; background-color: #dc3545; padding: 5px 10px; border-radius: 4px; display: inline-block; font-weight: bold; }
        .status-terminee { color: #155724; background-color: #d4edda; padding: 5px 10px; border-radius: 4px; display: inline-block; font-weight: bold; }

        /* Formulaire de Réponse */
        textarea { width: 100%; padding: 10px; margin-top: 5px; margin-bottom: 15px; border: 1px solid #ced4da; border-radius: 4px; box-sizing: border-box; }
        .btn-submit {
            background-color: #007bff; /* Bleu pour l'action Spécialiste */
            color: white;
            padding: 12px 25px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 1.1em;
            font-weight: bold;
            margin-top: 15px;
            transition: background-color 0.3s;
        }
        .btn-submit:hover { background-color: #0056b3; }
        .error { color: #dc3545; font-weight: bold; }
    </style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
<div class="container">
    <c:set var="demande" value="${requestScope.demande}" />
    <c:set var="patient" value="${demande.consultation.patient}" />

    <h1><i class="fas fa-file-signature"></i> Réponse à la Demande #${demande.id}</h1>

    <c:if test="${not empty requestScope.error}"><p class="error"><i class="fas fa-exclamation-triangle"></i> Erreur: ${requestScope.error}</p></c:if>

    <p>
        Priorité :
        <span class="${demande.priorite == 'URGENTE' ? 'alert-urgent' : ''}">
                <i class="fas fa-exclamation-circle"></i> ${demande.priorite}
            </span> |
        Statut :
        <c:choose>
            <c:when test="${demande.statut == 'TERMINEE'}"><span class="status-terminee"><i class="fas fa-check"></i> TERMINÉE</span></c:when>
            <c:otherwise>EN ATTENTE</c:otherwise>
        </c:choose>
    </p>

    <h2><i class="fas fa-user"></i> Dossier Patient</h2>
    <div class="patient-info">
        <h3>Données Administratives</h3>
        <p>Nom: <strong>${patient.nom} ${patient.prenom}</strong> | N° Sécu: ${patient.numSecuriteSociale}</p>
        <p>Né(e) : ${patient.dateNaissance}</p>

        <h3>Derniers Signes Vitaux (Infirmier) :</h3>
        <c:set var="svListSize" value="${patient.signesVitauxList.size()}" />
        <c:if test="${svListSize > 0}">
            <c:set var="latestSv" value="${patient.signesVitauxList[svListSize - 1]}" />
            <p><i class="fas fa-heartbeat"></i> Tension: ${latestSv.tensionArterielle} | FC: ${latestSv.frequenceCardiaque} | Temp: ${latestSv.temperature}°C</p>
        </c:if>
        <c:if test="${svListSize == 0}">
            <p>Aucun signe vital récent enregistré.</p>
        </c:if>
    </div>

    <h2><i class="fas fa-comment-medical"></i> Détails de la Demande</h2>
    <div class="dossier-details">
        <h3>Motif Principal du Généraliste</h3>
        <p><i class="fas fa-file-medical-alt"></i> Motif: ${demande.consultation.motif}</p>
        <p>Observations: ${demande.consultation.observations}</p>

        <h3>Question Posée (Étape 5)</h3>
        <p><strong>Question:</strong> ${demande.questionPosee}</p>
    </div>

    <hr>

    <c:choose>
        <c:when test="${demande.statut == 'TERMINEE'}">
            <h2><i class="fas fa-check-double"></i> Avis Final Fourni :</h2>
            <div class="msg-success" style="background-color: #e6f7ff; color: #007bff; border-color: #b3d7ff;">
                <p><strong>Avis Médical :</strong> ${demande.avisSpecialiste}</p>
                <p><strong>Recommandations :</strong> ${demande.recommandations}</p>
            </div>
        </c:when>
        <c:otherwise>
            <form method="POST" action="${pageContext.request.contextPath}/specialiste/repondre_expertise">
                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />
                <input type="hidden" name="demandeId" value="${demande.id}">

                <h2>4. Fournir l'Avis d'Expert (US8)</h2>
                <label for="avis"><i class="fas fa-syringe"></i> Avis Médical / Diagnostic :</label><br>
                <textarea id="avis" name="avis" rows="5" required placeholder="Confirmez/Orientez le diagnostic et l'analyse..."></textarea><br><br>

                <label for="recommandations"><i class="fas fa-prescription"></i> Recommandations (Traitement / Suivi) :</label><br>
                <textarea id="recommandations" name="recommandations" rows="4" placeholder="Ex: Prescrire Antibiotique X, revoir dans 7 jours, ou référer en consultation Y..."></textarea><br><br>

                <button type="submit" class="btn-submit"><i class="fas fa-lock"></i> Enregistrer l'Avis et Clôturer la Demande</button>
            </form>
        </c:otherwise>
    </c:choose>

    <p style="margin-top: 25px;">
        <a href="${pageContext.request.contextPath}/specialiste/demandes_expertise" style="color: #6c757d;">
            <i class="fas fa-arrow-left"></i> Retour à la Liste des Demandes
        </a>
    </p>
</div>
</body>
</html>