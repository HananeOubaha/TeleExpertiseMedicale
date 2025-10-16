<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Configuration Profil - Spécialiste (US5)</title>
</head>
<body>
<h1>Configuration du Profil (US-SPE-1)</h1>

<c:if test="${not empty requestScope.error}"><p style="color: red;">Erreur: ${requestScope.error}</p></c:if>
<c:if test="${not empty param.message}"><p style="color: green;">${param.message}</p></c:if>

<form method="POST" action="${pageContext.request.contextPath}/specialiste/profile">
    <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />

    <h2>Informations du Profil</h2>

    <label for="specialite">Spécialité :</label>
    <select id="specialite" name="specialite" required>
        <c:forEach var="spec" items="${requestScope.specialities}">
            <option value="${spec}"
                    <c:if test="${spec == requestScope.specialist.specialite}">selected</c:if>>
                    ${spec}
            </option>
        </c:forEach>
    </select>
    <br><br>

    <label for="tarif">Tarif de Consultation (DH) :</label>
    <input type="number" id="tarif" name="tarif" required
           value="${requestScope.specialist.tarifConsultation}" min="100">
    <br><br>

    <button type="submit">Sauvegarder et Générer Créneaux (30 min)</button>
</form>

<hr>

<p><a href="${pageContext.request.contextPath}/logout">Déconnexion</a></p>
</body>
</html>