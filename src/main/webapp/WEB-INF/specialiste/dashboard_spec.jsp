<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Tableau de Bord - Spécialiste</title>
</head>
<body>
<c:set var="specialist" value="${requestScope.specialist}" />

<h1>Bienvenue, Dr. ${specialist.nom} (${specialist.specialite})</h1>
<p>Tarif de consultation : ${specialist.tarifConsultation} DH (Durée moyenne : 30 min)</p>

<hr>

<h2>Module Télé-expertise (TICKET-002)</h2>

<div>
    <a href="${pageContext.request.contextPath}/specialiste/demandes_expertise">
        Consulter les Demandes d'Expertise (US7)
    </a>
</div>

<div>
    <a href="${pageContext.request.contextPath}/specialiste/creneaux">
        Voir mes Créneaux Horaires (US6)
    </a>
</div>

<div>
    <a href="${pageContext.request.contextPath}/specialiste/profile">
        Modifier mon Profil et Créneaux (US5)
    </a>
</div>

<br>
<p><a href="${pageContext.request.contextPath}/logout">Se Déconnecter</a></p>
</body>
</html>