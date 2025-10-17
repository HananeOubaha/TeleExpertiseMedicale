package com.teleexpertise.model.dao;

import com.teleexpertise.model.entities.Consultation;
// AJOUT DES IMPORTS JPA MANQUANTS
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class ConsultationDao {

    /**
     * Recherche une Consultation par son ID et CHARGE le Patient (Eager Fetch).
     */
    public Consultation findById(Long id) {
        // NOTE: JpaUtil est accessible via com.teleexpertise.dao
        EntityManager em = com.teleexpertise.dao.JpaUtil.getEntityManagerFactory().createEntityManager();
        Consultation consultation = null;

        try {
            // JPQL avec JOIN FETCH pour forcer le chargement du Patient (résout LazyInitializationException)
            TypedQuery<Consultation> query = em.createQuery(
                    "SELECT c FROM Consultation c JOIN FETCH c.patient p WHERE c.id = :id", Consultation.class);
            query.setParameter("id", id);

            consultation = query.getSingleResult();

        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            System.err.println("Erreur DAO lors de la recherche de la consultation par ID avec FETCH : " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return consultation;
    }

    /**
     * Persiste ou met à jour une consultation.
     */
    public Consultation save(Consultation consultation) {
        EntityManager em = com.teleexpertise.dao.JpaUtil.getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();

            if (consultation.getId() == null) {
                // Création (garantit la génération de l'ID pour la redirection)
                em.persist(consultation);
                em.flush();
            } else {
                // Mise à jour
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