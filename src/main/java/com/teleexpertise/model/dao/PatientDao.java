package com.teleexpertise.model.dao;

import com.teleexpertise.model.entities.Patient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import com.teleexpertise.dao.JpaUtil;
import java.util.List;

public class PatientDao {

    /**
     * Recherche un Patient par son numéro de Sécurité Sociale (unique).
     * @param numSecu Le numéro de Sécurité Sociale.
     * @return L'objet Patient ou null si non trouvé.
     */
    public Patient findByNumSecu(String numSecu) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        Patient patient = null;

        try {
            // CORRECTION CRITIQUE : Utilisation de LEFT JOIN FETCH pour charger la collection LAZY
            TypedQuery<Patient> query = em.createQuery(
                    "SELECT p FROM Patient p LEFT JOIN FETCH p.signesVitauxList WHERE p.numSecuriteSociale = :numSecu", Patient.class);
            query.setParameter("numSecu", numSecu);

            patient = query.getSingleResult();
        } catch (NoResultException e) {
            return null; // Patient non trouvé
        } catch (Exception e) {
            System.err.println("Erreur DAO lors de la recherche du patient : " + e.getMessage());
        } finally {
            em.close();
        }
        return patient;
    }

    /**
     * Persiste un nouveau Patient ou met à jour un Patient existant.
     * @param patient L'objet Patient à sauvegarder.
     * @return Le Patient sauvegardé avec son ID.
     */
    public Patient save(Patient patient) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();
            // merge() gère l'insertion (si ID est null) ou la mise à jour (si ID est non null)
            patient = em.merge(patient);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Erreur DAO lors de la sauvegarde du patient : " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return patient;
    }
    public List<Patient> findAllOrderByArrivalTime() {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        List<Patient> patients = null;

        try {
            // La requête JPQL avec EAGER FETCH (correction du Lazy loading)
            TypedQuery<Patient> query = em.createQuery(
                    "SELECT p FROM Patient p LEFT JOIN FETCH p.signesVitauxList ORDER BY p.dateArrivee ASC", Patient.class);

            // CORRECTION : L'appel de getResultList() est bien sur la requête
            patients = query.getResultList();

        } catch (Exception e) {
            System.err.println("Erreur DAO lors de la récupération de la liste des patients : " + e.getMessage());
        } finally {
            em.close();
        }
        return patients;
    }
}