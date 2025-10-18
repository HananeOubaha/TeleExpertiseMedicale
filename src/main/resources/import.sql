-- Fichier: src/main/resources/import.sql
-- Correction: Suppression des IDs manuels pour les tables auto-incrémentées (Utilisateur, ActeTechnique)

-- Mot de passe haché (BCrypt) pour 'password123' : $2a$10$Xo7G673r74F2T0gB.Q6z6.M02P4k/k4Vz/j1D9h2p1w2G4u4I8V6S

-- ######################################################
-- 1. Insertion des Utilisateurs (SANS ID MANUEL)
-- PostgreSQL gère l'ID automatiquement
-- ######################################################
-- ######################################################
-- Script d'Insertion Manuelle FINALISÉ (PostgreSQL)
-- ######################################################

-- 1. Insertion des Utilisateurs (IDs forcés)
INSERT INTO utilisateur (id, nom, prenom, email, mot_de_passe_hache, role)
VALUES (1, 'DUPONT', 'Sophie', 'sophie.dupont@hopital.com', '$2a$10$Xo7G673r74F2T0gB.Q6z6.M02P4k/k4Vz/j1D9h2p1w2G4u4I8V6S', 'INFIRMIER');

INSERT INTO utilisateur (id, nom, prenom, email, mot_de_passe_hache, role)
VALUES (2, 'MARTIN', 'Pierre', 'pierre.martin@hopital.com', '$2a$10$Xo7G673r74F2T0gB.Q6z6.M02P4k/k4Vz/j1D9h2p1w2G4u4I8V6S', 'GENERALISTE');

INSERT INTO utilisateur (id, nom, prenom, email, mot_de_passe_hache, role)
VALUES (3, 'LEGRAND', 'Anne', 'anne.legrand@hopital.com', '$2a$10$Xo7G673r74F2T0gB.Q6z6.M02P4k/k4Vz/j1D9h2p1w2G4u4I8V6S', 'SPECIALISTE');

INSERT INTO utilisateur (id, nom, prenom, email, mot_de_passe_hache, role)
VALUES (4, 'DUBOIS', 'Marc', 'marc.dubois@hopital.com', '$2a$10$Xo7G673r74F2T0gB.Q6z6.M02P4k/k4Vz/j1D9h2p1w2G4u4I8V6S', 'SPECIALISTE');


-- 2. Insertion dans les Tables Spécifiques aux Rôles
INSERT INTO medecin_generaliste (id) VALUES (2);
INSERT INTO medecin_specialiste (id, specialite, tarif_consultation)
VALUES (3, 'CARDIOLOGUE', 300.00);
INSERT INTO medecin_specialiste (id, specialite, tarif_consultation)
VALUES (4, 'DERMATOLOGUE', 250.00);


-- 3. Insertion des Actes Techniques Médicaux (CORRECTION DE L'APOSTROPHE)
-- INSERT INTO actetechnique (id, nom, cout) VALUES (1, 'Radiographie', 150.00);
-- INSERT INTO actetechnique (id, nom, cout) VALUES (2, 'Échographie', 200.00);
-- INSERT INTO actetechnique (id, nom, cout) VALUES (3, 'IRM', 800.00);
-- INSERT INTO actetechnique (id, nom, cout) VALUES (4, 'Électrocardiogramme', 100.00);
-- INSERT INTO actetechnique (id, nom, cout) VALUES (5, 'Fond d''oeil', 120.00); -- CORRIGÉ ICI
-- INSERT INTO actetechnique (id, nom, cout) VALUES (6, 'Analyse de sang', 50.00);
-- INSERT INTO actetechnique (id, nom, cout) VALUES (7, 'Analyse d''urine', 30.00);
-- INSERT INTO actetechnique (id, nom, cout) VALUES (8, 'DERMATOLOGIQUES(Laser)', 500.00);
--
-- -- 4. Mise à jour de la Séquence (CRITIQUE pour l'auto-incrémentation future)
-- SELECT setval('utilisateur_id_seq', 4, true);
-- SELECT setval('actetechnique_id_seq', 8, true);