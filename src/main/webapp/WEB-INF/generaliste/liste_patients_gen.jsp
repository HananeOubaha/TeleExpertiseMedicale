<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>File d'Attente - Généraliste</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f7f9; padding: 20px; color: #333; }
        .container { max-width: 1200px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1); border-top: 5px solid #007bff; }
        h1 { color: #007bff; font-size: 1.8em; margin-bottom: 20px; }

        /* Styles du Tableau */
        table { border-collapse: collapse; width: 100%; margin-top: 20px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05); }
        th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
        th { background-color: #e9ecef; color: #333; font-weight: bold; }
        tr:nth-child(even) { background-color: #f8f9fa; }

        /* Styles spécifiques aux données */
        .status-danger { color: #dc3545; font-weight: bold; } /* Température élevée */
        .status-normal { color: #28aa46; font-weight: bold; } /* Température normale */

        /* Boutons d'Action */
        .btn-action {
            display: inline-block;
            background-color: #007bff;
            color: white;
            padding: 8px 15px;
            border-radius: 5px;
            text-decoration: none;
            font-size: 0.9em;
            transition: background-color 0.3s;
        }
        .btn-action:hover { background-color: #0056b3; }
        .nav-links a { margin-right: 15px; text-decoration: none; color: #007bff; font-weight: bold; }
        .nav-links a:hover { text-decoration: underline; }
    </style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
<div class="container">
    <h1><i class="fas fa-list-alt"></i> Patients en Attente de Consultation (US-GEN-1)</h1>

    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/generaliste/dashboard"><i class="fas fa-tachometer-alt"></i> Tableau de Bord</a>
        <a href="${pageContext.request.contextPath}/logout" style="color: #dc3545;"><i class="fas fa-sign-out-alt"></i> Déconnexion</a>
    </div>

    <c:if test="${not empty param.message}">
        <p style="color: green;"><i class="fas fa-check-circle"></i> ${param.message}</p>
    </c:if>

    <c:choose>
        <c:when test="${empty requestScope.patients}">
            <p style="margin-top: 20px;"><i class="fas fa-info-circle"></i> Aucun patient dans la file d'attente pour le moment.</p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                <tr>
                    <th>Nom & Prénom</th>
                    <th>N° Sécurité Sociale</th>
                    <th><i class="fas fa-clock"></i> Heure d'Arrivée</th>
                    <th><i class="fas fa-thermometer-half"></i> Dernière Temp.</th>
                    <th><i class="fas fa-cogs"></i> Action</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="patient" items="${requestScope.patients}">
                    <c:set var="svListSize" value="${patient.signesVitauxList.size()}" />
                    <c:set var="latestSv" value="${svListSize > 0 ? patient.signesVitauxList[svListSize - 1] : null}" />

                    <%-- Détermination de la classe CSS pour la température --%>
                    <c:set var="tempStatus" value="normal" />
                    <c:if test="${latestSv != null && latestSv.temperature > 37.5}">
                        <c:set var="tempStatus" value="danger" />
                    </c:if>

                    <tr>
                        <td>${patient.nom} ${patient.prenom}</td>
                        <td>${patient.numSecuriteSociale}</td>
                        <td>
                            <fmt:formatDate value="${patient.dateArrivee}" pattern="HH:mm:ss"/>
                        </td>
                        <td class="${'status-'}${tempStatus}">
                            <c:if test="${latestSv != null}">
                                ${latestSv.temperature}°C
                            </c:if>
                            <c:if test="${latestSv == null}">N/A</c:if>
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/generaliste/creer_consultation?patientId=${patient.id}" class="btn-action">
                                <i class="fas fa-file-medical"></i> Démarrer Consultation
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