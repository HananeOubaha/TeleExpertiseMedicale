package com.teleexpertise.model.dao;

import com.teleexpertise.model.entities.Consultation;
import jakarta.persistence.EntityManager;

public class ConsultationDao {

    /**
     * Persiste une nouvelle consultation ou met à jour une consultation existante.
     * Utilise persist/flush pour la création afin de garantir que l'ID est généré
     * et disponible immédiatement pour la redirection.
     * * @param consultation L'objet Consultation à sauvegarder.
     * @return La Consultation sauvegardée avec son ID mis à jour.
     */
    public Consultation save(Consultation consultation) {
        // NOTE: JpaUtil est accessible via com.teleexpertise.dao
        EntityManager em = com.teleexpertise.dao.JpaUtil.getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();

            if (consultation.getId() == null) {
                // Création d'une nouvelle entité (garantit la génération de l'ID)
                em.persist(consultation);
                em.flush(); // Force l'exécution de l'INSERT et la récupération de l'ID
            } else {
                // Mise à jour d'une entité existante
                consultation = em.merge(consultation);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Erreur DAO lors de la sauvegarde de la consultation : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur de persistance lors de la sauvegarde de la consultation.", e);
        } finally {
            em.close();
        }
        return consultation;
    }
}