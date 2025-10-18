<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Mes Créneaux - Spécialiste (US6)</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f7f9; padding: 20px; color: #333; }
        .container { max-width: 900px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1); border-top: 5px solid #17a2b8; /* Cyan Thématique */ }
        h1 { color: #17a2b8; font-size: 1.8em; border-bottom: 2px solid #e9ecef; padding-bottom: 10px; margin-bottom: 20px; }

        /* Styles du Tableau */
        table { border-collapse: collapse; width: 100%; margin-top: 20px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05); }
        th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
        th { background-color: #e9ecef; color: #333; font-weight: bold; }
        tr:nth-child(even) { background-color: #f8f9fa; }

        /* Codes couleurs pour les statuts */
        .dispo { background-color: #d4edda; color: #155724; font-weight: bold; } /* Vert */
        .reserve { background-color: #fff3cd; color: #856404; font-weight: bold; } /* Jaune/Orange */
        .passe { color: #888; text-decoration: line-through; } /* Gris */
        .nav-links a { margin-right: 15px; text-decoration: none; color: #007bff; font-weight: bold; }
        .nav-links a:hover { text-decoration: underline; }
    </style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
<div class="container">
    <h1><i class="fas fa-calendar-check"></i> Mes Créneaux Horaires (US6)</h1>

    <div class="nav-links" style="margin-bottom: 20px;">
        <a href="${pageContext.request.contextPath}/specialiste/dashboard"><i class="fas fa-tachometer-alt"></i> Tableau de Bord</a>
        <a href="${pageContext.request.contextPath}/specialiste/profile"><i class="fas fa-cog"></i> Modifier Profil / Générer</a>
        <a href="${pageContext.request.contextPath}/logout" style="color: #dc3545;"><i class="fas fa-sign-out-alt"></i> Déconnexion</a>
    </div>

    <c:if test="${not empty requestScope.error}"><p class="error" style="color: red;"><i class="fas fa-exclamation-triangle"></i> Erreur: ${requestScope.error}</p></c:if>

    <c:choose>
        <c:when test="${empty requestScope.creneaux}">
            <p style="margin-top: 20px;"><i class="fas fa-info-circle"></i> Aucun créneau n'est défini. Veuillez aller dans "Modifier mon Profil" pour les générer.</p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                <tr>
                    <th><i class="fas fa-calendar-day"></i> Date</th>
                    <th><i class="fas fa-clock"></i> Heure</th>
                    <th>Statut</th>
                    <th>Consultation (Réservée)</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="creneau" items="${requestScope.creneaux}">

                    <%-- Utilisation des classes conditionnelles pour les statuts --%>
                    <tr class="${creneau.estPasse() ? 'passe' : (creneau.estDisponible ? 'dispo' : 'reserve')}">

                        <td>
                            <fmt:formatDate value="${creneau.debutDate}" pattern="E, dd MMM"/>
                        </td>
                        <td>
                            <fmt:formatDate value="${creneau.debutDate}" pattern="HH:mm"/>
                            -
                            <fmt:formatDate value="${creneau.finDate}" pattern="HH:mm"/>
                        </td>

                        <td>
                            <c:choose>
                                <c:when test="${creneau.estPasse()}">Archivé</c:when>
                                <c:when test="${creneau.estDisponible}"><i class="fas fa-check"></i> Disponible</c:when>
                                <c:otherwise><i class="fas fa-lock"></i> Réservé</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:if test="${!creneau.estDisponible && !creneau.estPasse()}">
                                Réservé par le Généraliste (Voir Demandes US7)
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>