package com.teleexpertise.model.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "consultation")
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Le médecin généraliste qui a créé cette consultation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generaliste_id", nullable = false)
    private MedecinGeneraliste generaliste;

    // Nous ajouterons d'autres champs (motif, statut, patient) plus tard

    public Consultation() {}

    // --- Getters et Setters nécessaires ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MedecinGeneraliste getGeneraliste() { return generaliste; }
    public void setGeneraliste(MedecinGeneraliste generaliste) { this.generaliste = generaliste; }
}