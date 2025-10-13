package com.teleexpertise.model.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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

    // Le champ privé doit rester en LocalDateTime pour la persistance JPA
    @Column(name = "date_arrivee", nullable = false)
    private LocalDateTime dateArrivee;

    // Relations: Un patient a plusieurs mesures de signes vitaux
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SignesVitaux> signesVitauxList = new ArrayList<>();

    // Constructeur par défaut requis par JPA
    public Patient() {
        this.dateArrivee = LocalDateTime.now();
    }

    // --- Getters et Setters (Simples) ---

    // Le Getter de base, MODIFIÉ pour retourner java.util.Date pour JSTL
    // Hibernate utilise cette méthode comme getter principal.
    public Date getDateArrivee() {
        if (this.dateArrivee == null) {
            return null;
        }
        return Date.from(this.dateArrivee.atZone(ZoneId.systemDefault()).toInstant());
    }

    // Le Setter doit rester en LocalDateTime pour insérer dans la DB
    public void setDateArrivee(LocalDateTime dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    // Méthode pour obtenir le LocalDateTime si nécessaire pour Java (non utilisée en JSP)
    public LocalDateTime getDateArriveeAsLocalDateTime() {
        return this.dateArrivee;
    }


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

    public List<SignesVitaux> getSignesVitauxList() {
        return signesVitauxList;
    }

    public void setSignesVitauxList(List<SignesVitaux> signesVitauxList) {
        this.signesVitauxList = signesVitauxList;
    }

    // Méthode utilitaire pour ajouter un signe vital
    public void addSignesVitaux(SignesVitaux sv) {
        if (sv == null) {
            throw new IllegalArgumentException("Signes Vitaux ne peuvent pas être null.");
        }
        this.signesVitauxList.add(sv);
        sv.setPatient(this);
    }
}