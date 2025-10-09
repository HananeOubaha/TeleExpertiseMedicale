package com.teleexpertise.model.entities;

import com.teleexpertise.model.enums.SpecialiteEnum;
import jakarta.persistence.*;

@Entity
@Table(name = "medecin_specialiste")
@PrimaryKeyJoinColumn(name = "id")
public class MedecinSpecialiste extends Utilisateur {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpecialiteEnum specialite;

    // Tarif journalier de l'expertise/consultation
    @Column(name = "tarif_consultation")
    private double tarifConsultation;

    // --- Getters et Setters (incluant ceux pour specialite et tarifConsultation) ---
}
