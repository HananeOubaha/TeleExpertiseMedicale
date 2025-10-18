//package com.teleexpertise.model.service;
//
//import com.teleexpertise.model.dao.PatientDao;
//import com.teleexpertise.model.entities.Patient;
//import com.teleexpertise.model.entities.SignesVitaux;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
///**
// * Tests unitaires pour la logique métier de l'Infirmier.
// */
//@ExtendWith(MockitoExtension.class)
//class InfirmierServiceTest {
//
//    @Mock
//    private PatientDao patientDao; // Simuler le DAO
//
//    @InjectMocks
//    private InfirmierService infirmierService; // Injecter le Mock dans le Service
//
//    @Test
//    void accueillirPatient_NouveauPatient_DoitCreerEtLierSV() {
//        // Préparation du scénario: Nouveau Patient (ID null)
//        Patient nouveauPatient = new Patient();
//        SignesVitaux sv = new SignesVitaux();
//        sv.setTemperature(37.5);
//
//        // Simuler la sauvegarde réussie (avec l'ID généré par la DB)
//        when(patientDao.save(any(Patient.class))).thenAnswer(invocation -> {
//            Patient p = invocation.getArgument(0);
//            p.setId(10L); // Simuler la génération de l'ID
//            return p;
//        });
//
//        // Exécution
//        Patient result = infirmierService.accueillirPatient(nouveauPatient, sv);
//
//        // Validation
//        assertNotNull(result.getId(), "L'ID du patient doit être généré.");
//        assertEquals(1, result.getSignesVitauxList().size(), "Un SV doit être ajouté.");
//        assertEquals(result, sv.getPatient(), "Le SV doit être lié au nouveau patient.");
//
//        verify(patientDao, times(1)).save(nouveauPatient);
//    }
//
//    @Test
//    void rechercherPatient_DoitRetournerPatientExistant() {
//        // Préparation
//        String numSecuExistant = "123";
//        Patient patientMock = new Patient();
//
//        // Définir le comportement du Mock
//        when(patientDao.findByNumSecu(numSecuExistant)).thenReturn(patientMock);
//
//        // Exécution et Validation
//        Patient result = infirmierService.rechercherPatient(numSecuExistant);
//
//        assertNotNull(result, "Le patient doit être trouvé.");
//        verify(patientDao, times(1)).findByNumSecu(numSecuExistant);
//    }
//}