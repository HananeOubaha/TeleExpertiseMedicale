package com.teleexpertise.model.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "actetechnique")
public class ActeTechnique {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom; // Ex: Radiographie, IRM

    @Column(nullable = false)
    private double cout; // Tarif de l'acte

    // Constructeur par défaut requis par JPA
    public ActeTechnique() {}

    // Constructeur pour faciliter l'initialisation (non obligatoire)
    public ActeTechnique(String nom, double cout) {
        this.nom = nom;
        this.cout = cout;
    }

    // --- Getters et Setters nécessaires ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public double getCout() { return cout; }
    public void setCout(double cout) { this.cout = cout; }
}