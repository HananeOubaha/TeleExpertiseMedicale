<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>File d'Attente des Patients (US2)</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f7f9; padding: 20px; color: #333; }
        .container { max-width: 1200px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1); border-top: 5px solid #17a2b8; /* Cyan Thématique */ }
        h1 { color: #17a2b8; font-size: 1.8em; margin-bottom: 20px; }

        /* Styles du Tableau */
        table { border-collapse: collapse; width: 100%; margin-top: 20px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05); }
        th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
        th { background-color: #e9ecef; color: #333; font-weight: bold; }
        tr:nth-child(even) { background-color: #f8f9fa; }

        /* Styles conditionnels */
        .status-danger { background-color: #f8d7da; color: #721c24; font-weight: bold; } /* Rouge pour Alerte */
        .status-normal { color: #155724; } /* Vert pour Normal */

        /* Boutons de Navigation */
        .nav-links a { margin-right: 15px; text-decoration: none; color: #007bff; font-weight: bold; }
        .nav-links a:hover { text-decoration: underline; }
    </style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
<div class="container">
    <h1><i class="fas fa-stream"></i> File d'Attente des Patients (US2)</h1>

    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/infirmier/accueil"><i class="fas fa-user-plus"></i> Retour à l'Accueil</a>
        <a href="${pageContext.request.contextPath}/logout" style="color: #dc3545;"><i class="fas fa-sign-out-alt"></i> Déconnexion</a>
    </div>

    <c:if test="${not empty requestScope.message}">
        <p style="color: green; margin-top: 15px;"><i class="fas fa-check-circle"></i> ${requestScope.message}</p>
    </c:if>

    <c:choose>
        <c:when test="${empty requestScope.patients}">
            <p style="margin-top: 20px;"><i class="fas fa-info-circle"></i> Aucun patient dans la file d'attente pour le moment.</p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                <tr>
                    <th><i class="fas fa-user"></i> Nom & Prénom</th>
                    <th>N° Sécurité Sociale</th>
                    <th><i class="fas fa-clock"></i> Heure d'Arrivée</th>
                    <th>Tension (Dernière)</th>
                    <th>FC (Dernière)</th>
                    <th><i class="fas fa-thermometer-half"></i> Température (Dernière)</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="patient" items="${requestScope.patients}">
                    <c:set var="svListSize" value="${patient.signesVitauxList.size()}" />
                    <c:set var="latestSv" value="${svListSize > 0 ? patient.signesVitauxList[svListSize - 1] : null}" />

                    <c:set var="temp" value="${latestSv.temperature}" />

                    <%-- Détermination de la classe CSS pour la température --%>
                    <c:set var="tempClass" value="${temp > 37.5 ? 'status-danger' : 'status-normal'}" />

                    <tr>
                        <td>${patient.nom} ${patient.prenom}</td>
                        <td>${patient.numSecuriteSociale}</td>
                        <td>
                            <fmt:formatDate value="${patient.dateArrivee}" pattern="HH:mm:ss"/>
                        </td>

                        <c:choose>
                            <c:when test="${latestSv != null}">
                                <td>${latestSv.tensionArterielle}</td>
                                <td>${latestSv.frequenceCardiaque}</td>
                                <td class="${tempClass}">
                                        ${latestSv.temperature}°C
                                    <c:if test="${temp > 37.5}"><i class="fas fa-exclamation-circle"></i></c:if>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td>N/A</td>
                                <td>N/A</td>
                                <td>N/A</td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>