package com.teleexpertise.model.service;

import com.teleexpertise.model.dao.PatientDao;
import com.teleexpertise.model.dao.UtilisateurDao;
import com.teleexpertise.model.entities.Patient;
import com.teleexpertise.model.entities.SignesVitaux;
import java.time.LocalDateTime;

public class InfirmierService {

    private PatientDao patientDao = new PatientDao();

    public Patient accueillirPatient(Patient patient, SignesVitaux signesVitaux) {

        // La méthode save() du DAO gère la création (si patient.getId() == null) ou la mise à jour.

        // 1. Lier les signes vitaux au patient et définir l'heure
        signesVitaux.setHeureMesure(LocalDateTime.now());

        // 2. Ajouter les signes vitaux à la liste du patient
        if (patient.getId() != null) {
            // Patient existant: Ajouter la nouvelle mesure à la liste
            patient.addSignesVitaux(signesVitaux);
        } else {
            // Nouveau patient: Le Patient() a déjà initialisé dateArrivee = now()
            patient.addSignesVitaux(signesVitaux);
            // Le statut "en file d'attente" est implicite par la date d'arrivée non traitée.
        }

        // 3. Sauvegarder (persistance)
        return patientDao.save(patient);
    }

    /**
     * US1: Recherche d'un patient existant.
     */
    public Patient rechercherPatient(String numSecu) {
        return patientDao.findByNumSecu(numSecu);
    }


}