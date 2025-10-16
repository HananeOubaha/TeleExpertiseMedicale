package com.teleexpertise.model.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "creneau")
public class Creneau {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialiste_id", nullable = false)
    private MedecinSpecialiste specialiste; // Le propriétaire du créneau

    @Column(name = "heure_debut", nullable = false)
    private LocalDateTime heureDebut;

    @Column(name = "heure_fin", nullable = false)
    private LocalDateTime heureFin;

    @Column(name = "est_disponible", nullable = false)
    private Boolean estDisponible;

    // Relation Optionnelle: Si un créneau est réservé, il est lié à une consultation
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "creneauReserve")
    private Consultation consultationAssociee;

    public Creneau() {
        this.estDisponible = true;
    }

    // --- Getters et Setters (Résout toutes les erreurs de 'setXyz' dans Service/DAO) ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MedecinSpecialiste getSpecialiste() { return specialiste; }
    public void setSpecialiste(MedecinSpecialiste specialiste) { this.specialiste = specialiste; }

    public LocalDateTime getHeureDebut() { return heureDebut; }
    public void setHeureDebut(LocalDateTime heureDebut) { this.heureDebut = heureDebut; }

    public LocalDateTime getHeureFin() { return heureFin; }
    public void setHeureFin(LocalDateTime heureFin) { this.heureFin = heureFin; }

    public Boolean getEstDisponible() { return estDisponible; }
    public void setEstDisponible(Boolean estDisponible) { this.estDisponible = estDisponible; }

    public Consultation getConsultationAssociee() { return consultationAssociee; }
    public void setConsultationAssociee(Consultation consultationAssociee) { this.consultationAssociee = consultationAssociee; }
}