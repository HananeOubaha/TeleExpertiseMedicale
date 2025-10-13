package com.teleexpertise.model.web;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

// Applique ce filtre à toutes les URLs pour intercepter toutes les soumissions de formulaire
@WebFilter(filterName = "CsrfFilter", urlPatterns = "/*")
public class CsrfFilter implements Filter {

    public static final String CSRF_TOKEN = "csrfToken";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(true);

        // Les requêtes POST doivent être vérifiées
        if ("POST".equalsIgnoreCase(httpRequest.getMethod())) {

            String sessionToken = (String) session.getAttribute(CSRF_TOKEN);
            String requestToken = httpRequest.getParameter(CSRF_TOKEN);

            // Ne pas vérifier la connexion initiale si le token est manquant (première fois)
            // C'est une simplification pour l'exemple, mais la vérification stricte est préférable
            boolean isLogin = httpRequest.getRequestURI().endsWith("/doLogin");

            if (!isLogin && (sessionToken == null || !sessionToken.equals(requestToken))) {
                // Si le token est invalide ou manquant et que ce n'est pas le login initial
                System.err.println("ERREUR CSRF : Jeton invalide ou manquant pour " + httpRequest.getRequestURI());
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Jeton CSRF invalide ou manquant.");
                return;
            }

            // Re-générer le token après un POST réussi pour la prochaine requête
            session.setAttribute(CSRF_TOKEN, UUID.randomUUID().toString());
        }

        // 1. Pour toutes les requêtes (GET ou après vérification POST), s'assurer qu'un token est dans la session
        if (session.getAttribute(CSRF_TOKEN) == null) {
            session.setAttribute(CSRF_TOKEN, UUID.randomUUID().toString());
        }

        chain.doFilter(request, response);
    }
}