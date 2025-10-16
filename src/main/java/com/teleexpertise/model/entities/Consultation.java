package com.teleexpertise.model.entities;

import com.teleexpertise.model.enums.ConsultationStatutEnum;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "consultation")
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Relations Principales ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generaliste_id", nullable = false)
    private MedecinGeneraliste generaliste;

    // NOUVELLE RELATION 1:1 vers la Demande d'Expertise (pour la gestion du mappedBy)
    // Côté non-propriétaire de la relation 1:1 de la demande
    @OneToOne(mappedBy = "consultation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DemandeExpertise demandeExpertise;


    // --- Relations TICKET-002 (Télé-expertise synchrone) ---
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creneau_reserve_id")
    private Creneau creneauReserve; // RÉSOUD L'ERREUR DE MAPPING DANS Creneau.java


    // --- Relation ManyToMany (Actes) ---
    // CORRECTION : L'annotation @ManyToMany doit être immédiatement au-dessus de la List
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "consultation_acte",
            joinColumns = @JoinColumn(name = "consultation_id"),
            inverseJoinColumns = @JoinColumn(name = "acte_id")
    )
    private List<ActeTechnique> actes; // CORRECTION : Supprime la déclaration en double


    // --- Champs Statiques / Données ---
    @Column(nullable = false)
    private LocalDateTime dateCreation;

    private String motif;
    private String observations;
    private String diagnostic;
    private String prescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConsultationStatutEnum statut;

    @Column(name = "cout_total", nullable = false)
    private double coutTotal = 150.0;


    // --- Constructeur ---
    public Consultation() {
        this.dateCreation = LocalDateTime.now();
        this.statut = ConsultationStatutEnum.EN_ATTENTE_AVIS_SPECIALISTE;
        this.coutTotal = 150.0;
    }

    // --- Getters et Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public MedecinGeneraliste getGeneraliste() { return generaliste; }
    public void setGeneraliste(MedecinGeneraliste generaliste) { this.generaliste = generaliste; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }

    public String getDiagnostic() { return diagnostic; }
    public void setDiagnostic(String diagnostic) { this.diagnostic = diagnostic; }

    public String getPrescription() { return prescription; }
    public void setPrescription(String prescription) { this.prescription = prescription; }

    public ConsultationStatutEnum getStatut() { return statut; }
    public void setStatut(ConsultationStatutEnum statut) { this.statut = statut; }

    public double getCoutTotal() { return coutTotal; }
    public void setCoutTotal(double coutTotal) { this.coutTotal = coutTotal; }

    public List<ActeTechnique> getActes() { return actes; }
    public void setActes(List<ActeTechnique> actes) { this.actes = actes; }

    public Creneau getCreneauReserve() { return creneauReserve; }
    public void setCreneauReserve(Creneau creneauReserve) { this.creneauReserve = creneauReserve; }

    public DemandeExpertise getDemandeExpertise() { return demandeExpertise; }
    public void setDemandeExpertise(DemandeExpertise demandeExpertise) { this.demandeExpertise = demandeExpertise; }
}