package com.teleexpertise.model.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(name = "date_naissance")
    private String dateNaissance;

    @Column(name = "num_securite_sociale", unique = true, nullable = false)
    private String numSecuriteSociale;

    private String telephone;
    private String adresse;

    @Column(name = "date_arrivee", nullable = false)
    private LocalDateTime dateArrivee;

    // Relations: Un patient a plusieurs mesures de signes vitaux
    // CascadeType.ALL signifie que si le Patient est supprimé, ses SignesVitaux le sont aussi.
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SignesVitaux> signesVitauxList = new ArrayList<>();

    // Constructeur par défaut requis par JPA
    public Patient() {
        this.dateArrivee = LocalDateTime.now();
    }

    // --- Getters et Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getNumSecuriteSociale() {
        return numSecuriteSociale;
    }

    public void setNumSecuriteSociale(String numSecuriteSociale) {
        this.numSecuriteSociale = numSecuriteSociale;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public LocalDateTime getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(LocalDateTime dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public List<SignesVitaux> getSignesVitauxList() {
        return signesVitauxList;
    }

    public void setSignesVitauxList(List<SignesVitaux> signesVitauxList) {
        this.signesVitauxList = signesVitauxList;
    }

    // Méthode utilitaire pour ajouter un signe vital
    public void addSignesVitaux(SignesVitaux sv) {
        this.signesVitauxList.add(sv);
        sv.setPatient(this);
    }
}