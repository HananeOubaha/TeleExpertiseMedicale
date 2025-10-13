package com.teleexpertise.model.entities;
import com.teleexpertise.model.enums.ConsultationStatutEnum;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List ;

@Entity
@Table(name = "consultation")
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // L'Infirmier met le Patient en file d'attente, le Généraliste prend le dossier
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generaliste_id", nullable = false)
    private MedecinGeneraliste generaliste; // Le médecin qui crée la consultation

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    private String motif;
    private String observations;
    private String diagnostic;
    private String prescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConsultationStatutEnum statut; // EN_ATTENTE_CONSULTATION, TERMINEE, EN_ATTENTE_AVIS_SPECIALISTE

    @Column(name = "cout_total", nullable = false)
    private double coutTotal = 150.0; // Coût de base (150 DH fixe pour la consultation)

    // Relation ManyToMany avec ActeTechnique (Radiographie, IRM, etc.)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "consultation_acte",
            joinColumns = @JoinColumn(name = "consultation_id"),
            inverseJoinColumns = @JoinColumn(name = "acte_id")
    )
    private List<ActeTechnique> actes;

    public Consultation() {
        this.dateCreation = LocalDateTime.now();
        this.statut = ConsultationStatutEnum.EN_ATTENTE_CONSULTATION;
        this.coutTotal = 150.0; // Initialisation du coût de base
    }

    // --- Getters et Setters nécessaires ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; } // RÉSOUD setPatient

    public MedecinGeneraliste getGeneraliste() { return generaliste; }
    public void setGeneraliste(MedecinGeneraliste generaliste) { this.generaliste = generaliste; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; } // RÉSOUD setMotif

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; } // RÉSOUD setObservations

    public String getDiagnostic() { return diagnostic; }
    public void setDiagnostic(String diagnostic) { this.diagnostic = diagnostic; } // RÉSOUD setDiagnostic

    public String getPrescription() { return prescription; }
    public void setPrescription(String prescription) { this.prescription = prescription; } // RÉSOUD setPrescription

    public ConsultationStatutEnum getStatut() { return statut; }
    public void setStatut(ConsultationStatutEnum statut) { this.statut = statut; } // RÉSOUD setStatut

    public List<ActeTechnique> getActes() { return actes; }
    public void setActes(List<ActeTechnique> actes) { this.actes = actes; } // RÉSOUD setActes

    public double getCoutTotal() { return coutTotal; }
    public void setCoutTotal(double coutTotal) { this.coutTotal = coutTotal; }
}