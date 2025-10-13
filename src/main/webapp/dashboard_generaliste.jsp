<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Tableau de Bord Généraliste</title>
</head>
<body>
<h1>Bienvenue, Médecin Généraliste !</h1>
<p>Authentification réussie en tant que : <%= session.getAttribute("utilisateurRole") %></p>

<a href="${pageContext.request.contextPath}/generaliste/liste_patients">Voir la file d'attente (US-GEN-1)</a>

<br><br>
<a href="${pageContext.request.contextPath}/logout">Se Déconnecter</a>
</body>
</html>