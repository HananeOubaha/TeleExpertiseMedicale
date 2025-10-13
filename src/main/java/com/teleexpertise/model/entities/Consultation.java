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

    public MedecinGeneraliste getGeneraliste() { return generaliste; }
    public void setGeneraliste(MedecinGeneraliste generaliste) { this.generaliste = generaliste; }
}