package com.teleexpertise.model.dao;

import com.teleexpertise.model.entities.Creneau;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery; // Ajouté pour la requête SELECT
import jakarta.persistence.Query; // Ajouté pour la requête DELETE
import java.util.List;

public class CreneauDao {

    /**
     * Persiste un nouveau créneau.
     */
    public Creneau save(Creneau creneau) {
        EntityManager em = com.teleexpertise.dao.JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
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
     * US-SPE-1: Supprime tous les créneaux existants pour un spécialiste donné.
     */
    public void deleteAllBySpecialisteId(Long specialistId) {
        EntityManager em = com.teleexpertise.dao.JpaUtil.getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();

            Query query = em.createQuery(
                    "DELETE FROM Creneau c WHERE c.specialiste.id = :specialistId");
            query.setParameter("specialistId", specialistId);
            query.executeUpdate();

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Erreur DAO lors de la suppression des créneaux : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la réinitialisation des disponibilités.", e);
        } finally {
            em.close();
        }
    }

    /**
     * US6: Récupère la liste des créneaux pour un spécialiste, triés par date/heure.
     * @param specialistId L'ID du spécialiste.
     * @return Liste de tous les créneaux.
     */
    public List<Creneau> findCreneauxBySpecialisteId(Long specialistId) {
        EntityManager em = com.teleexpertise.dao.JpaUtil.getEntityManagerFactory().createEntityManager();
        List<Creneau> creneaux = null;

        try {
            // JPQL: Jointure FETCH pour charger la consultation associée (si réservé)
            TypedQuery<Creneau> query = em.createQuery(
                    "SELECT c FROM Creneau c " +
                            "LEFT JOIN FETCH c.consultationAssociee ca " + // Charge la consultation si elle existe
                            "WHERE c.specialiste.id = :specialistId " +
                            "ORDER BY c.heureDebut ASC", Creneau.class);

            query.setParameter("specialistId", specialistId);
            creneaux = query.getResultList();
        } catch (Exception e) {
            System.err.println("Erreur DAO lors de la récupération des créneaux : " + e.getMessage());
        } finally {
            em.close();
        }
        return creneaux;
    }
}