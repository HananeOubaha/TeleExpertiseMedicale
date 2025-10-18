//package com.teleexpertise.model.service;
//
//import com.teleexpertise.model.entities.ActeTechnique;
//import org.junit.jupiter.api.Test;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
///**
// * Tests unitaires pour la logique métier du Médecin Généraliste (US4: Calcul des coûts).
// */
//class GeneralisteServiceTest {
//
//    private GeneralisteService service = new GeneralisteService();
//    private static final double COUT_BASE = 150.0;
//
//    @Test
//    void calculerCoutTotal_SansActes() {
//        // Cas : Consultation simple sans acte technique
//        List<ActeTechnique> actes = Arrays.asList();
//
//        double coutAttendu = COUT_BASE; // 150.0 DH
//        double coutReel = service.calculerCoutTotal(actes);
//
//        assertEquals(coutAttendu, coutReel, "Le coût doit être égal au coût de base.");
//    }
//
//    @Test
//    void calculerCoutTotal_AvecUnActe() {
//        // Cas : Consultation + 1 Acte (Radiographie à 150.0 DH)
//        ActeTechnique radio = new ActeTechnique("Radiographie", 150.0);
//        List<ActeTechnique> actes = Arrays.asList(radio);
//
//        double coutAttendu = COUT_BASE + 150.0; // 300.0 DH
//        double coutReel = service.calculerCoutTotal(actes);
//
//        assertEquals(coutAttendu, coutReel, "Le coût doit être égal à la base plus l'acte.");
//    }
//
//    @Test
//    void calculerCoutTotal_AvecPlusieursActes() {
//        // Cas : Consultation + 3 Actes (150 + 200 + 800)
//        ActeTechnique radio = new ActeTechnique("Radio", 150.0);
//        ActeTechnique echo = new ActeTechnique("Échographie", 200.0);
//        ActeTechnique irm = new ActeTechnique("IRM", 800.0);
//        List<ActeTechnique> actes = Arrays.asList(radio, echo, irm);
//
//        double coutAttendu = COUT_BASE + 150.0 + 200.0 + 800.0; // 1300.0 DH
//        double coutReel = service.calculerCoutTotal(actes);
//
//        assertEquals(coutAttendu, coutReel, "Le coût doit inclure tous les actes.");
//    }
//}