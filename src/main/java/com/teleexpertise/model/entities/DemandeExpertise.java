package com.teleexpertise.model.entities;

import com.teleexpertise.model.enums.PrioriteEnum;
import com.teleexpertise.model.enums.ConsultationStatutEnum;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "demande_expertise")
public class DemandeExpertise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relation 1:1 avec la Consultation
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_id", nullable = false)
    private Consultation consultation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialiste_id") // Ne pas mettre nullable=false ici, car il peut être désigné plus tard
    private MedecinSpecialiste specialisteDemande;

    @Column(name = "question_posee", length = 1000)
    private String questionPosee;

    @Column(name = "avis_specialiste", length = 2000)
    private String avisSpecialiste;

    private String recommandations;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrioriteEnum priorite;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConsultationStatutEnum statut; // Résolu : nécessite la mise à jour de l'Enum

    @Column(name = "date_demande")
    private LocalDateTime dateDemande;

    public DemandeExpertise() {
        this.dateDemande = LocalDateTime.now();
        // Le symbole est maintenant résolu si l'Enum a été mis à jour
        this.statut = ConsultationStatutEnum.EN_ATTENTE_AVIS_SPECIALISTE;
    }

    // --- Getters et Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Consultation getConsultation() {
        return consultation;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
    }

    public MedecinSpecialiste getSpecialisteDemande() {
        return specialisteDemande;
    }

    public void setSpecialisteDemande(MedecinSpecialiste specialisteDemande) {
        this.specialisteDemande = specialisteDemande;
    }

    public String getQuestionPosee() {
        return questionPosee;
    }

    public void setQuestionPosee(String questionPosee) {
        this.questionPosee = questionPosee;
    }

    public String getAvisSpecialiste() {
        return avisSpecialiste;
    }

    public void setAvisSpecialiste(String avisSpecialiste) {
        this.avisSpecialiste = avisSpecialiste;
    }

    public String getRecommandations() {
        return recommandations;
    }

    public void setRecommandations(String recommandations) {
        this.recommandations = recommandations;
    }

    public PrioriteEnum getPriorite() {
        return priorite;
    }

    public void setPriorite(PrioriteEnum priorite) {
        this.priorite = priorite;
    }

    public ConsultationStatutEnum getStatut() {
        return statut;
    }

    public void setStatut(ConsultationStatutEnum statut) {
        this.statut = statut;
    }

    public LocalDateTime getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(LocalDateTime dateDemande) {
        this.dateDemande = dateDemande;
    }
}