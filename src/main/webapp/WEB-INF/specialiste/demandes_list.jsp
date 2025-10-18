<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Demandes d'Expertise (US7)</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f7f9; padding: 20px; color: #333; }
        .container { max-width: 1200px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1); border-top: 5px solid #17a2b8; /* Cyan Thématique */ }
        h1 { color: #17a2b8; font-size: 1.8em; border-bottom: 2px solid #e9ecef; padding-bottom: 10px; margin-bottom: 20px; }

        /* Styles du Tableau */
        table { border-collapse: collapse; width: 100%; margin-top: 15px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05); }
        th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
        th { background-color: #e9ecef; color: #333; font-weight: bold; }
        tr:nth-child(even) { background-color: #f8f9fa; }

        /* Couleurs de Statut (Priority & Status) */
        .status-header { background-color: #f4f4f4; }
        .urgent { background-color: #f8d7da; color: #721c24; font-weight: bold; } /* Rouge Alerte */
        .normale { background-color: #fff3cd; color: #856404; } /* Jaune Avertissement */
        .terminee { background-color: #d4edda; color: #155724; } /* Vert Succès */

        /* Styles de Navigation et Filtre */
        .nav-links a { margin-right: 15px; text-decoration: none; color: #007bff; font-weight: bold; }
        .filter-box { margin-bottom: 20px; padding: 15px; border: 1px solid #ddd; border-radius: 5px; }
        .btn-action { background-color: #007bff; color: white; padding: 6px 12px; border-radius: 4px; text-decoration: none; font-size: 0.9em; }
        .btn-action:hover { background-color: #0056b3; }
    </style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
<div class="container">
    <h1><i class="fas fa-inbox"></i> Liste des Demandes d'Expertise (US7)</h1>

    <div class="nav-links" style="margin-bottom: 20px;">
        <a href="${pageContext.request.contextPath}/specialiste/dashboard"><i class="fas fa-tachometer-alt"></i> Tableau de Bord</a>
        <a href="${pageContext.request.contextPath}/logout" style="color: #dc3545;"><i class="fas fa-sign-out-alt"></i> Déconnexion</a>
    </div>

    <c:if test="${not empty requestScope.error}"><p style="color: red;"><i class="fas fa-exclamation-triangle"></i> Erreur: ${requestScope.error}</p></c:if>

    <div class="filter-box">
        <form method="GET" action="${pageContext.request.contextPath}/specialiste/demandes_expertise">
            <label for="statut"><i class="fas fa-filter"></i> Filtrer par statut :</label>
            <select id="statut" name="statut" onchange="this.form.submit()">
                <option value="">-- Tous les statuts --</option>
                <c:forEach var="statut" items="${requestScope.statutsDisponibles}">
                    <option value="${statut}"
                            <c:if test="${statut == requestScope.statutSelectionne}">selected</c:if>>
                            ${statut}
                    </option>
                </c:forEach>
            </select>
        </form>
    </div>

    <c:choose>
        <c:when test="${empty requestScope.demandes}">
            <p style="margin-top: 20px;"><i class="fas fa-info-circle"></i> Aucune demande correspondante.</p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                <tr class="status-header">
                    <th><i class="fas fa-hashtag"></i> ID Demande</th>
                    <th><i class="fas fa-user"></i> Patient</th>
                    <th><i class="fas fa-exclamation"></i> Priorité</th>
                    <th>Statut</th>
                    <th>Date de Demande</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="demande" items="${requestScope.demandes}">
                    <c:set var="statusClass"
                           value="${demande.priorite == 'URGENTE' ? 'urgent' : (demande.statut == 'TERMINEE' ? 'terminee' : 'normale')}" />

                    <tr class="${statusClass}">
                        <td>${demande.id}</td>
                        <td>${demande.consultation.patient.nom} ${demande.consultation.patient.prenom}</td>
                        <td>
                            <c:if test="${demande.priorite == 'URGENTE'}"><i class="fas fa-fire-extinguisher"></i></c:if>
                                ${demande.priorite}
                        </td>
                        <td>
                            <c:if test="${demande.statut == 'TERMINEE'}"><i class="fas fa-check-circle"></i></c:if>
                                ${demande.statut}
                        </td>
                        <td>
                            <fmt:formatDate value="${demande.dateDemandeUtil}" pattern="dd/MM/yy HH:mm"/>
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/specialiste/repondre_expertise?demandeId=${demande.id}" class="btn-action">
                                <i class="fas fa-comment-medical"></i> Consulter / Répondre
                            </a>
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