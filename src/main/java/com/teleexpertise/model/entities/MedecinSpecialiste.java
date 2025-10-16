package com.teleexpertise.model.entities;

import com.teleexpertise.model.enums.SpecialiteEnum;
import jakarta.persistence.*;

@Entity
@Table(name = "medecin_specialiste")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("SPECIALISTE")
public class MedecinSpecialiste extends Utilisateur {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpecialiteEnum specialite;

    // Tarif journalier de l'expertise/consultation
    @Column(name = "tarif_consultation")
    private double tarifConsultation;

    public SpecialiteEnum getSpecialite() {
        return specialite;
    }

    public void setSpecialite(SpecialiteEnum specialite) { // Mettre à jour la spécialité
        this.specialite = specialite;
    }

    public double getTarifConsultation() {
        return tarifConsultation;
    }

    public void setTarifConsultation(double tarifConsultation) { // Mettre à jour le tarif
        this.tarifConsultation = tarifConsultation;
    }
}
