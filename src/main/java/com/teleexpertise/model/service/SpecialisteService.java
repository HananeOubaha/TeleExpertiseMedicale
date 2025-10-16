package com.teleexpertise.model.service;

import com.teleexpertise.model.dao.CreneauDao;
import com.teleexpertise.model.dao.MedecinSpecialisteDao; // Import du DAO spécifique
import com.teleexpertise.model.entities.Creneau;
import com.teleexpertise.model.entities.MedecinSpecialiste;
import com.teleexpertise.model.enums.SpecialiteEnum;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SpecialisteService {

    private final CreneauDao creneauDao = new CreneauDao();
    private final MedecinSpecialisteDao specialistDao = new MedecinSpecialisteDao(); // Utilisation du DAO

    /**
     * US-SPE-1: Met à jour le profil du spécialiste (tarif, spécialité).
     */
    public MedecinSpecialiste updateProfile(MedecinSpecialiste specialist,
                                            SpecialiteEnum specialite,
                                            double tarif) {
        // Ces lignes devraient maintenant compiler après la correction de l'entité
        specialist.setSpecialite(specialite);
        specialist.setTarifConsultation(tarif);

        // Délègue la persistance au DAO
        return specialistDao.save(specialist);
    }

    /**
     * US-SPE-1: Génère les créneaux pour les 7 prochains jours, de 09h00 à 12h00 (durée 30 min).
     */
    public void genererCreneaux(MedecinSpecialiste specialist) {

        // Supprimer d'abord tous les créneaux existants (si implémenté dans CreneauDao)
        // creneauDao.deleteAllBySpecialisteId(specialist.getId());

        LocalDateTime now = LocalDateTime.now();
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(12, 0);
        int dureeMinutes = 30; // 30 min fixe

        for (int i = 0; i < 7; i++) {
            LocalDateTime jourCourant = now.plusDays(i).withHour(startTime.getHour()).withMinute(startTime.getMinute()).withSecond(0).withNano(0);

            // Ne pas générer de créneau si l'heure de début est déjà passée aujourd'hui
            if (i == 0 && startTime.isBefore(now.toLocalTime())) {
                continue;
            }

            LocalDateTime currentSlotStart = jourCourant;

            while (currentSlotStart.toLocalTime().isBefore(endTime)) {
                LocalDateTime currentSlotEnd = currentSlotStart.plusMinutes(dureeMinutes);

                // Ne pas créer le créneau si la fin dépasse l'heure limite
                if (currentSlotEnd.toLocalTime().isAfter(endTime)) {
                    break;
                }

                Creneau creneau = new Creneau();
                // Ces lignes devraient maintenant compiler après la correction de l'entité Creneau
                creneau.setSpecialiste(specialist);
                creneau.setHeureDebut(currentSlotStart);
                creneau.setHeureFin(currentSlotEnd);
                creneau.setEstDisponible(true);

                creneauDao.save(creneau);

                currentSlotStart = currentSlotEnd;
            }
        }
    }
}