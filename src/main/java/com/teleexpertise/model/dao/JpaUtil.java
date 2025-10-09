package com.teleexpertise.dao;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {

    private static EntityManagerFactory factory = null;

    public static EntityManagerFactory getEntityManagerFactory() {
        if (factory == null) {
            System.out.println("TENTATIVE D'INITIALISATION JPA..."); // AJOUT DE LOG CRITIQUE
            try {
                // Le nom "TeleExpertisePU" DOIT correspondre au nom dans persistence.xml
                factory = Persistence.createEntityManagerFactory("TeleExpertisePU");
                System.out.println("INITIALISATION JPA RÉUSSIE.");
            } catch (Exception e) {
                System.err.println("ERREUR LORS DE L'INITIALISATION JPA: Vérifiez la connexion DB et persistence.xml");
                e.printStackTrace();
            }
        }
        return factory;
    }

    public static void shutdown() {
        if (factory != null) {
            factory.close();
            factory = null;
        }
    }
}