package com.teleexpertise.model.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Classe utilitaire pour gérer le hachage et la vérification des mots de passe
 * en utilisant l'algorithme BCrypt.
 */
public class PasswordUtil {

    // Facteur de coût (work factor) pour BCrypt. 10 est un bon équilibre entre sécurité et performance.
    private static final int WORKLOAD = 10;

    /**
     * Génère un hash BCrypt sécurisé à partir d'un mot de passe en clair.
     * * @param password_plaintext Le mot de passe en clair à hacher.
     * @return Le hash BCrypt résultant (incluant le salt).
     */
    public static String hashPassword(String password_plaintext) {
        // Le salage (salt) est généré automatiquement par BCrypt.gensalt(WORKLOAD)
        String salt = BCrypt.gensalt(WORKLOAD);
        return BCrypt.hashpw(password_plaintext, salt);
    }

    /**
     * Vérifie si un mot de passe en clair correspond à un hash BCrypt existant.
     * * @param password_plaintext Le mot de passe soumis par l'utilisateur.
     * @param stored_hash Le hash stocké dans la base de données.
     * @return Vrai si les mots de passe correspondent, Faux sinon.
     */
    public static boolean checkPassword(String password_plaintext, String stored_hash) {
        if (stored_hash == null || stored_hash.isEmpty()) {
            return false;
        }
        // BCrypt.checkpw gère automatiquement l'extraction du salt du hash stocké
        return BCrypt.checkpw(password_plaintext, stored_hash);
    }
    // =======================================================
    // AJOUT TEMPORAIRE POUR GÉNÉRER LE HASH
    // =======================================================
    public static void main(String[] args) {
        String motDePasseAHasher = "password123";
        String nouveauHash = hashPassword(motDePasseAHasher);

        System.out.println("-------------------------------------------------------------------");
        System.out.println("Mot de passe en clair : " + motDePasseAHasher);
        System.out.println("HASH BCrypt GÉNÉRÉ : " + nouveauHash);
        System.out.println("-------------------------------------------------------------------");
    }
    // =======================================================
}
