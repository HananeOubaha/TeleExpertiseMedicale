package com.teleexpertise.model.service;

import com.teleexpertise.model.dao.ConsultationDao;
import com.teleexpertise.model.entities.ActeTechnique;
import com.teleexpertise.model.entities.Consultation;
import com.teleexpertise.model.enums.ConsultationStatutEnum;
import java.util.List;
import com.teleexpertise.model.enums.SpecialiteEnum;
import com.teleexpertise.model.entities.MedecinSpecialiste;
import java.util.Comparator;
import java.util.stream.Collectors;
import com.teleexpertise.model.dao.MedecinSpecialisteDao;

public class GeneralisteService {

    private final ConsultationDao consultationDao = new ConsultationDao();
    private final MedecinSpecialisteDao specialistDao = new MedecinSpecialisteDao();
    private static final double COUT_CONSULTATION_BASE = 150.0; // 150 DH

    /**
     * US4: Calcule le coût total (Consultation + Actes)
     * Utilisation Lambda / Stream API: map().sum()
     * @param actes Liste des actes liés à la consultation.
     * @return Le coût total de la consultation.
     */
    public double calculerCoutTotal(List<ActeTechnique> actes) {
        double coutActes = 0.0;

        if (actes != null && !actes.isEmpty()) {
            // Exigence Lambda/Stream API : calculer la somme des coûts des actes
            coutActes = actes.stream()
                    .mapToDouble(ActeTechnique::getCout)
                    .sum();
        }

        return COUT_CONSULTATION_BASE + coutActes;
    }

    /**
     * US1 & US4: Clôture une consultation après avoir calculé le coût final.
     * @param consultation La consultation à clôturer.
     * @return La consultation mise à jour.
     */
    public Consultation cloturerConsultation(Consultation consultation) {
        consultation.setStatut(ConsultationStatutEnum.TERMINEE);

        // Calcul du coût total avant sauvegarde
        double nouveauCoutTotal = calculerCoutTotal(consultation.getActes());
        consultation.setCoutTotal(nouveauCoutTotal);

        return consultationDao.save(consultation);
    }

    public Consultation saveConsultation(Consultation consultation) {
        return consultationDao.save(consultation);
    }
    /**
     * US3: Recherche et filtre les spécialistes disponibles.
     * Utilisation Stream API: Filtrage par spécialité et tri par tarif.
     */
    public List<MedecinSpecialiste> findSpecialistes(SpecialiteEnum specialite) {

        // 1. Récupérer tous les spécialistes actifs
        List<MedecinSpecialiste> tousLesSpecialistes = specialistDao.findAll();

        // 2. LOGIQUE CRITIQUE (Stream API)
        return tousLesSpecialistes.stream()
                // Filtre par spécialité (Exigence Stream API)
                .filter(s -> s.getSpecialite() == specialite)

                // Tri par tarif (Exigence Stream API) - Du moins cher au plus cher
                .sorted(Comparator.comparingDouble(MedecinSpecialiste::getTarifConsultation))

                .collect(Collectors.toList());
    }

    // NOUVEAU: Méthode pour charger un spécialiste par ID (pour les étapes suivantes)
    public MedecinSpecialiste findSpecialisteById(Long id) {
        return specialistDao.findById(id);
    }
}