package com.teleexpertise.model.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.time.Instant;

@Entity
@Table(name = "creneau")
public class Creneau {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialiste_id", nullable = false)
    private MedecinSpecialiste specialiste;

    @Column(name = "heure_debut", nullable = false)
    private LocalDateTime heureDebut;

    @Column(name = "heure_fin", nullable = false)
    private LocalDateTime heureFin;

    @Column(name = "est_disponible", nullable = false)
    private Boolean estDisponible;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "creneauReserve")
    private Consultation consultationAssociee;

    public Creneau() {
        this.estDisponible = true;
    }

    // --- Getters et Setters standard ---
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

    // --- NOUVEAUX GETTERS CONFORMES (pour l'affichage JSTL) ---

    /** * Getter pour l'heure de début, retournant java.util.Date pour JSTL.
     * Accessible en JSP par ${creneau.debutDate}.
     */
    public Date getDebutDate() {
        if (this.heureDebut == null) return null;
        return Date.from(this.heureDebut.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Getter pour l'heure de fin, retournant java.util.Date pour JSTL.
     * Accessible en JSP par ${creneau.finDate}.
     */
    public Date getFinDate() {
        if (this.heureFin == null) return null;
        return Date.from(this.heureFin.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Logique de vérification pour l'affichage 'Archivé'.
     */
    public boolean estPasse() {
        return this.heureFin.isBefore(LocalDateTime.now());
    }
}