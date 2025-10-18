<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Accueil Patient - Infirmier (US1)</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f7f9; padding: 20px; color: #333; }
        .container { max-width: 800px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1); border-top: 5px solid #17a2b8; /* Cyan pour l'Infirmier */ }
        h1 { color: #17a2b8; font-size: 1.8em; border-bottom: 2px solid #e9ecef; padding-bottom: 10px; margin-bottom: 20px; }
        h2 { color: #555; font-size: 1.4em; margin-top: 25px; margin-bottom: 15px; border-bottom: 1px solid #eee; padding-bottom: 5px; }
        h3 { color: #007bff; font-size: 1.1em; margin-top: 15px; }

        /* Formulaires */
        label { display: block; margin-top: 10px; font-weight: bold; color: #333; }
        input[type="text"], input[type="number"], input[type="date"] { width: 100%; padding: 10px; margin-top: 5px; margin-bottom: 15px; border: 1px solid #ced4da; border-radius: 4px; box-sizing: border-box; }

        /* Boutons */
        .btn-search { background-color: #007bff; color: white; padding: 10px 20px; border: none; border-radius: 4px; cursor: pointer; font-weight: bold; transition: background-color 0.3s; }
        .btn-search:hover { background-color: #0056b3; }
        .btn-submit { background-color: #28a745; color: white; padding: 12px 25px; border: none; border-radius: 6px; cursor: pointer; font-size: 1.1em; font-weight: bold; margin-top: 20px; width: 100%; }
        .btn-submit:hover { background-color: #218838; }

        /* Messages */
        .msg-error { color: #dc3545; background-color: #f8d7da; border: 1px solid #f5c6cb; padding: 10px; border-radius: 5px; margin-bottom: 15px; font-weight: bold; }
        .msg-success { color: #155724; background-color: #d4edda; border: 1px solid #c3e6cb; padding: 10px; border-radius: 5px; margin-bottom: 15px; font-weight: bold; }
        .nav-links a { margin-right: 15px; text-decoration: none; color: #007bff; font-weight: bold; }
    </style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
<div class="container">
    <c:set var="patient" value="${requestScope.patient}" />

    <h1><i class="fas fa-user-plus"></i> Accueil Patient (US1)</h1>

    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/infirmier/patients_du_jour"><i class="fas fa-list-alt"></i> File d'Attente (US2)</a>
        <a href="${pageContext.request.contextPath}/logout" style="color: #dc3545;"><i class="fas fa-sign-out-alt"></i> Déconnexion</a>
    </div>

    <c:if test="${not empty requestScope.error}"><p class="msg-error"><i class="fas fa-exclamation-triangle"></i> Erreur: ${requestScope.error}</p></c:if>
    <c:if test="${not empty requestScope.message}"><p class="msg-success"><i class="fas fa-info-circle"></i> ${requestScope.message}</p></c:if>

    <h2>1. Recherche du Patient</h2>
    <form method="POST" action="${pageContext.request.contextPath}/infirmier/accueil">
        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />
        <input type="hidden" name="action" value="search">
        <label for="numSecuSearch"><i class="fas fa-id-card"></i> Numéro de Sécurité Sociale:</label>
        <input type="text" id="numSecuSearch" name="numSecu" required placeholder="Saisir le N° Sécu" value="${not empty requestScope.numSecu ? requestScope.numSecu : ''}">
        <button type="submit" class="btn-search"><i class="fas fa-search"></i> Rechercher</button>
    </form>

    <hr>

    <c:if test="${not empty patient.id || not empty requestScope.numSecu || not empty requestScope.error}">
    <h2>2. Enregistrement / Saisie des Signes Vitaux</h2>

    <form method="POST" action="${pageContext.request.contextPath}/infirmier/accueil">
        <input type="hidden" name="action" value="save">
        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />

        <input type="hidden" name="patientId" value="${patient.id}">

        <c:choose>
            <c:when test="${patient.id == null}">
                <h3><i class="fas fa-address-card"></i> Nouveau Patient (Données Administratives)</h3>
                <label for="nom">Nom:</label><input type="text" id="nom" name="nom" required placeholder="Nom du patient">
                <label for="prenom">Prénom:</label><input type="text" id="prenom" name="prenom" required placeholder="Prénom du patient">
                <label for="dateNaissance">Date de Naissance:</label><input type="date" id="dateNaissance" name="dateNaissance">
                <label for="numSecu">Numéro de Sécurité Sociale:</label>
                <input type="text" id="numSecu" name="numSecu" required value="${requestScope.numSecu}">
            </c:when>
            <c:otherwise>
                <h3><i class="fas fa-user-check"></i> Patient Existant</h3>
                <p>Dossier: <strong>${patient.nom} ${patient.prenom}</strong> (NS: ${patient.numSecuriteSociale})</p>
                <p>Mesures précédentes: ${patient.signesVitauxList.size()} enregistrées</p>
                <input type="hidden" name="numSecu" value="${patient.numSecuriteSociale}">
            </c:otherwise>
        </c:choose>

        <hr>

        <h3><i class="fas fa-heartbeat"></i> Signes Vitaux (Nouveaux)</h3>
        <label for="tension">Tension Artérielle (mmHg):</label><input type="text" id="tension" name="tension" required placeholder="Ex: 12/8">
        <label for="fc">Fréquence Cardiaque (bpm):</label><input type="number" id="fc" name="fc" required placeholder="Ex: 75">
        <label for="temp">Température (°C):</label><input type="text" id="temp" name="temp" required placeholder="Ex: 37.0">
        <label for="fr">Fréquence Respiratoire (cycles/min):</label><input type="number" id="fr" name="fr" required placeholder="Ex: 16">
        <label for="poids">Poids (kg) (Optionnel):</label><input type="text" id="poids" name="poids">
        <label for="taille">Taille (cm) (Optionnel):</label><input type="text" id="taille" name="taille">

        <button type="submit" class="btn-submit"><i class="fas fa-save"></i> Enregistrer et Mettre en File d'Attente</button>
    </form>
    </c:if>

    <c:if test="${empty patient.id && empty requestScope.numSecu && empty requestScope.error}">
    <p style="margin-top: