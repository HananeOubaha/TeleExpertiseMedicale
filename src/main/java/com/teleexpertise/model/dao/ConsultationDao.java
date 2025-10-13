package com.teleexpertise.model.dao;

import com.teleexpertise.model.entities.Consultation;
import jakarta.persistence.EntityManager;

public class ConsultationDao {

    /**
     * Persiste une nouvelle consultation ou met à jour une consultation existante.
     * @param consultation L'objet Consultation à sauvegarder.
     * @return La Consultation sauvegardée avec son ID.
     */
    public Consultation save(Consultation consultation) {
        EntityManager em = com.teleexpertise.dao.JpaUtil.getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();
            consultation = em.merge(consultation);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Erreur DAO lors de la sauvegarde de la consultation : " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return consultation;
    }
}