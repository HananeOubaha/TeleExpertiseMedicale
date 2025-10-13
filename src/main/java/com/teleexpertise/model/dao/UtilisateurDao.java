package com.teleexpertise.model.dao;

import com.teleexpertise.model.entities.Utilisateur;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import com.teleexpertise.model.util.PasswordUtil;

public class UtilisateurDao {

    /**
     * Recherche un Utilisateur par son email dans la base de données.
     * @param email L'email de l'utilisateur à rechercher.
     * @return L'objet Utilisateur correspondant, ou null si non trouvé.
     */
    public Utilisateur trouverParEmail(String email) {
        EntityManager em = com.teleexpertise.dao.JpaUtil.getEntityManagerFactory().createEntityManager();
        Utilisateur utilisateur = null;

        try {
            // Création de la requête JPQL (Java Persistence Query Language)
            TypedQuery<Utilisateur> query = em.createQuery(
                    "SELECT u FROM Utilisateur u WHERE u.email = :email", Utilisateur.class);
            query.setParameter("email", email);

            // Exécution et récupération du résultat
            utilisateur = query.getSingleResult();

        } catch (NoResultException e) {
            // C'est normal si aucun utilisateur n'est trouvé.
            return null;
        } catch (Exception e) {
            System.err.println("Erreur DAO lors de la recherche par email : " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return utilisateur;
    }

    /**
     * Authentifie un utilisateur avec son email et mot de passe.
     * @param email L'email de l'utilisateur.
     * @param motDePasseClair Le mot de passe en clair (non haché) soumis.
     * @return L'objet Utilisateur si l'authentification réussit, null sinon.
     */
    public Utilisateur authentifier(String email, String motDePasseClair) {
        // 1. Charger l'utilisateur par email
        Utilisateur utilisateur = trouverParEmail(email);

        // 2. Vérifier si l'utilisateur existe
        if (utilisateur == null) {
            System.out.println("Échec d'authentification: Utilisateur non trouvé pour l'email " + email);
            return null;
        }

        // 3. Vérifier le mot de passe avec BCrypt
        String hashStocke = utilisateur.getMotDePasseHache();

        // Assurez-vous que le champ de l'entité s'appelle bien getMotDePasse() ou adaptez.

        if (PasswordUtil.checkPassword(motDePasseClair, hashStocke)) {
            System.out.println("Authentification réussie pour : " + email);
            return utilisateur;
        } else {
            System.out.println("Échec d'authentification: Mot de passe incorrect pour " + email);
            return null;
        }
    }
}