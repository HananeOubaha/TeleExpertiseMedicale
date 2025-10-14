package com.teleexpertise.model.dao;

import com.teleexpertise.model.entities.ActeTechnique;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ActeTechniqueDao {

    /**
     * Récupère tous les Actes Techniques disponibles (pour le formulaire).
     * @return Liste de tous les Actes Techniques.
     */
    public List<ActeTechnique> findAll() {
        EntityManager em = com.teleexpertise.dao.JpaUtil.getEntityManagerFactory().createEntityManager();
        List<ActeTechnique> actes = null;

        try {
            TypedQuery<ActeTechnique> query = em.createQuery(
                    "SELECT a FROM ActeTechnique a ORDER BY a.nom ASC", ActeTechnique.class);
            actes = query.getResultList();
        } catch (Exception e) {
            System.err.println("Erreur DAO lors de la récupération des actes : " + e.getMessage());
        } finally {
            em.close();
        }
        return actes;
    }

    /**
     * Récupère un Acte Technique par son ID.
     */
    public ActeTechnique findById(Long id) {
        EntityManager em = com.teleexpertise.dao.JpaUtil.getEntityManagerFactory().createEntityManager();
        ActeTechnique acte = em.find(ActeTechnique.class, id);
        em.close();
        return acte;
    }
}