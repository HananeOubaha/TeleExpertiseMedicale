package com.teleexpertise.web;

import com.teleexpertise.dao.JpaUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

// L'annotation @WebListener permet à Tomcat de trouver cette classe automatiquement.
@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("--- Démarrage de l'Application: Appel du Listener JPA ---");
        // Forcer le chargement de la Factory, ce qui déclenche la lecture du persistence.xml
        // et l'exécution de hbm2ddl.auto=create.
        JpaUtil.getEntityManagerFactory();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("--- Arrêt de l'Application: Fermeture de la Factory JPA ---");
        JpaUtil.shutdown();
    }
}