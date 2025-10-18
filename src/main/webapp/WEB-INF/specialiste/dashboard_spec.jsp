<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Tableau de Bord - Spécialiste</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f8f9fa; padding: 20px; color: #333; }
        .container { max-width: 900px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1); border-top: 5px solid #17a2b8; /* Cyan Thématique */ }
        .header { display: flex; justify-content: space-between; align-items: center; border-bottom: 2px solid #e9ecef; padding-bottom: 15px; margin-bottom: 20px; }
        h1 { color: #17a2b8; font-size: 1.8em; }
        h2 { color: #555; font-size: 1.4em; margin-top: 20px; margin-bottom: 15px; }
        .user-info { font-size: 1em; color: #6c757d; text-align: right; }
        .stats-box { background-color: #e9f0f7; padding: 15px; border-radius: 6px; margin-bottom: 25px; }

        /* Action Links Styling */
        .action-link-group { display: flex; flex-direction: column; gap: 10px; margin-top: 20px; }
        .action-link {
            display: flex; /* Utilise flexbox pour aligner l'icône */
            align-items: center;
            padding: 15px;
            border-radius: 6px;
            text-decoration: none;
            font-weight: bold;
            transition: background-color 0.3s;
        }
        .action-link i { margin-right: 15px; font-size: 1.2em; }

        .link-demandes { background-color: #ffc107; color: #333; border: 1px solid #ffaa00; } /* Jaune/Orange pour Demandes (Action) */
        .link-demandes:hover { background-color: #e0a800; }

        .link-creneaux { background-color: #007bff; color: white; } /* Bleu pour Créneaux */
        .link-creneaux:hover { background-color: #0056b3; }

        .link-profile { background-color: #6c757d; color: white; } /* Gris pour Profil */
        .link-profile:hover { background-color: #5a6268; }

        .logout { color: #dc3545; text-decoration: none; margin-top: 25px; display: inline-block; font-weight: bold; }
    </style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
<div class="container">
    <c:set var="specialist" value="${requestScope.specialist}" />

    <div class="header">
        <h1><i class="fas fa-user-tie"></i> Espace Spécialiste</h1>
        <div class="user-info">
            <p>Authentification réussie : <strong>Dr. ${specialist.prenom}</strong></p>
        </div>
    </div>

    <h2>Bienvenue, Dr. ${specialist.nom}</h2>

    <div class="stats-box">
        <p>Spécialité actuelle : <strong>${specialist.specialite}</strong></p>
        <p>Tarif de consultation : <strong>${specialist.tarifConsultation} DH</strong> (Durée moyenne : 30 min)</p>
    </div>

    <hr>

    <h2>Module Télé-expertise (TICKET-002)</h2>

    <div class="action-link-group">

        <a href="${pageContext.request.contextPath}/specialiste/demandes_expertise" class="action-link link-demandes">
            <i class="fas fa-bell"></i> Consulter les Demandes d'Expertise (US7)
        </a>

        <a href="${pageContext.request.contextPath}/specialiste/creneaux" class="action-link link-creneaux">
            <i class="fas fa-calendar-alt"></i> Voir mes Créneaux Horaires (US6)
        </a>

        <a href="${pageContext.request.contextPath}/specialiste/profile" class="action-link link-profile">
            <i class="fas fa-cog"></i> Modifier mon Profil et Créneaux (US5)
        </a>
    </div>

    <p><a href="${pageContext.request.contextPath}/logout" class="logout"><i class="fas fa-sign-out-alt"></i> Se Déconnecter</a></p>
</div>
</body>
</html>