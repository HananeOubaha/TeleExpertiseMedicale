<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bienvenue au Système de Télé-expertise Médicale</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #e9f0f7; /* Bleu très clair */
            color: #333;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            text-align: center;
        }
        .home-container {
            background-color: white;
            padding: 40px 60px;
            border-radius: 12px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            max-width: 600px;
            animation: fadeIn 1s ease-out;
        }
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }
        h1 {
            color: #007bff; /* Bleu Thématique */
            font-size: 2.5em;
            margin-bottom: 10px;
        }
        h1 i {
            margin-right: 15px;
        }
        p {
            font-size: 1.1em;
            color: #555;
            margin-bottom: 30px;
        }
        .access-button {
            display: inline-block;
            background-color: #28a745; /* Vert pour l'action principale */
            color: white;
            padding: 15px 30px;
            text-decoration: none;
            border-radius: 8px;
            font-size: 1.2em;
            font-weight: bold;
            transition: background-color 0.3s, transform 0.2s;
        }
        .access-button:hover {
            background-color: #218838;
            transform: translateY(-2px);
        }
        .footer-note {
            margin-top: 30px;
            font-size: 0.9em;
            color: #999;
        }
    </style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<body>
<div class="home-container">
    <h1><i class="fa fa-hospital-o"></i> Télé-expertise Médicale</h1>
    <p>
        Bienvenue sur la plateforme dédiée à l'optimisation du parcours patient et à la coordination inter-médecins.
        Facilitez la demande et la fourniture d'avis spécialisés à distance.
    </p>

    <a href="${pageContext.request.contextPath}/login" class="access-button">
        <i class="fa fa-sign-in"></i> Accès Professionnel (Login)
    </a>

    <p class="footer-note">
        La configuration du serveur est validée. Système opérationnel.
    </p>
</div>
</body>
</html>