package com.teleexpertise.model.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "medecin_generaliste")
@PrimaryKeyJoinColumn(name = "id")
public class MedecinGeneraliste extends Utilisateur {

    @OneToMany(mappedBy = "generaliste", fetch = FetchType.LAZY)
    private List<Consultation> consultationsCrees;

    // Constructeur par défaut requis par JPA
    public MedecinGeneraliste() {}

    public List<Consultation> getConsultationsCrees() { return consultationsCrees; }
    public void setConsultationsCrees(List<Consultation> consultationsCrees) { this.consultationsCrees = consultationsCrees; }
}