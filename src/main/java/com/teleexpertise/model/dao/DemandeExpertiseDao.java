package com.teleexpertise.model.dao;

import com.teleexpertise.model.entities.DemandeExpertise;
import com.teleexpertise.model.enums.ConsultationStatutEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class DemandeExpertiseDao {

    /**
     * US7: Récupère les demandes d'expertise pour un spécialiste spécifique.
     * @param specialistId L'ID du spécialiste.
     * @param statut Le statut pour filtrer (EN_ATTENTE_AVIS_SPECIALISTE, TERMINEE, ou null pour tous).
     * @return Liste des demandes triées par priorité et date.
     */
    public List<DemandeExpertise> findBySpecialisteAndStatut(Long specialistId, ConsultationStatutEnum statut) {
        EntityManager em = com.teleexpertise.dao.JpaUtil.getEntityManagerFactory().createEntityManager();
        List<DemandeExpertise> demandes = null;

        // Requête JPQL
        String jpql = "SELECT d FROM DemandeExpertise d " +
                "LEFT JOIN FETCH d.consultation c " + // Charger la consultation et le patient
                "LEFT JOIN FETCH c.patient p " +
                "WHERE d.specialisteDemande.id = :specialistId ";

        if (statut != null) {
            jpql += "AND d.statut = :statut ";
        }

        // Tri par Priorité (URGENTE en premier) et Date de Demande
        jpql += "ORDER BY d.priorite ASC, d.dateDemande ASC";

        try {
            TypedQuery<DemandeExpertise> query = em.createQuery(jpql, DemandeExpertise.class);
            query.setParameter("specialistId", specialistId);

            if (statut != null) {
                query.setParameter("statut", statut);
            }

            demandes = query.getResultList();
        } catch (Exception e) {
            System.err.println("Erreur DAO lors de la récupération des demandes d'expertise : " + e.getMessage());
        } finally {
            em.close();
        }
        return demandes;
    }

    public DemandeExpertise save(DemandeExpertise demande) {
        EntityManager em = com.teleexpertise.dao.JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();

            // Persist pour garantir la génération de l'ID
            if (demande.getId() == null) {
                em.persist(demande);
                em.flush(); // Force l'insertion pour obtenir l'ID
            } else {
                demande = em.merge(demande);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Erreur DAO lors de la sauvegarde de la demande : " + e.getMessage());
            throw new RuntimeException("Erreur de persistance de la demande d'expertise.", e);
        } finally {
            em.close();
        }
        return demande;
    }
}