package com.teleexpertise.model.enums;

/**
 * Définit le statut actuel d'une consultation,
 * gérant ainsi le Scénario A (prise en charge directe) et le Scénario B (télé-expertise).
 */
public enum ConsultationStatutEnum {

    /**
     * Statut initial après qu'un Généraliste sélectionne un patient dans la file d'attente.
     */
    EN_COURS_DE_TRAITEMENT,

    /**
     * Scénario A : La consultation est terminée et le Généraliste a établi un diagnostic
     * et une prescription.
     */
    TERMINEE,

    /**
     * Scénario B : Le Généraliste a besoin de l'avis d'un Spécialiste et a soumis une demande.
     * La consultation reste ouverte.
     */
    EN_ATTENTE_CONSULTATION
}