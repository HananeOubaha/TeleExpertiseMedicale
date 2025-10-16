package com.teleexpertise.model.dao;

import com.teleexpertise.model.entities.Creneau;
import jakarta.persistence.EntityManager;

public class CreneauDao {

    /**
     * Persiste un nouveau créneau.
     */
    public Creneau save(Creneau creneau) {
        EntityManager em = com.teleexpertise.dao.JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            // Utilise persist pour un nouvel objet
            em.persist(creneau);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Erreur DAO lors de la sauvegarde du créneau : " + e.getMessage());
            throw new RuntimeException("Erreur de persistance du créneau.", e);
        } finally {
            em.close();
        }
        return creneau;
    }

    /**
     * Supprime tous les créneaux futurs pour un spécialiste donné (pour la réinitialisation).
     */
    public void deleteAllBySpecialisteId(Long specialistId) {
        // Cette méthode sera utile si un spécialiste veut réinitialiser ses disponibilités
        // (Implémentation omise ici pour se concentrer sur l'US, mais nécessaire en production).
    }
}