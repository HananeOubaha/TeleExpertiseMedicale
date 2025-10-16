package com.teleexpertise.model.dao;

import com.teleexpertise.model.entities.MedecinSpecialiste;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class MedecinSpecialisteDao {

    /**
     * Recherche un Médecin Spécialiste par son ID.
     * Cette méthode est essentielle pour charger le profil (US-SPE-1).
     * @param id L'ID de l'utilisateur (issu de la session).
     * @return L'objet MedecinSpecialiste ou null si non trouvé.
     */
    public MedecinSpecialiste findById(Long id) {
        // NOTE : JpaUtil est accessible via com.teleexpertise.dao
        EntityManager em = com.teleexpertise.dao.JpaUtil.getEntityManagerFactory().createEntityManager();
        MedecinSpecialiste specialist = em.find(MedecinSpecialiste.class, id);
        em.close();
        return specialist;
    }

    /**
     * US3: Récupère la liste de tous les Spécialistes pour le Généraliste.
     * Cette méthode charge les entités concrètes.
     * @return Liste de tous les Spécialistes.
     */
    public List<MedecinSpecialiste> findAll() {
        EntityManager em = com.teleexpertise.dao.JpaUtil.getEntityManagerFactory().createEntityManager();
        List<MedecinSpecialiste> specialists = null;

        try {
            // JPQL: Récupère toutes les entités MedecinSpecialiste
            TypedQuery<MedecinSpecialiste> query = em.createQuery(
                    "SELECT s FROM MedecinSpecialiste s", MedecinSpecialiste.class);
            specialists = query.getResultList();
        } catch (Exception e) {
            System.err.println("Erreur DAO lors de la récupération des spécialistes : " + e.getMessage());
        } finally {
            em.close();
        }
        return specialists;
    }

    /**
     * US-SPE-1: Persiste les modifications apportées au profil (Tarif, Spécialité).
     * @param specialist L'objet MedecinSpecialiste à mettre à jour.
     * @return L'objet mis à jour.
     */
    public MedecinSpecialiste save(MedecinSpecialiste specialist) {
        EntityManager em = com.teleexpertise.dao.JpaUtil.getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();
            // Merge est essentiel ici car l'objet vient de l'extérieur (Servlet)
            specialist = em.merge(specialist);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Erreur DAO lors de la sauvegarde du spécialiste : " + e.getMessage());
            throw new RuntimeException("Erreur de persistance du spécialiste.", e);
        } finally {
            em.close();
        }
        return specialist;
    }
}