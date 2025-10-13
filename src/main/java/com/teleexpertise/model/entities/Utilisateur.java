package com.teleexpertise.model.entities;

import com.teleexpertise.model.enums.RoleEnum;
import jakarta.persistence.*;

// Utilisateur abstrait pour l'héritage
@Entity
@Table(name = "utilisateur")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING) // CRUCIAL POUR L'HÉRITAGE
public abstract class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String nom;
    protected String prenom;

    @Column(unique = true, nullable = false)
    protected String email;

    // Le hash bcrypt du mot de passe
    @Column(name = "mot_de_passe_hache", nullable = false)
    protected String motDePasseHache;

    @Enumerated(EnumType.STRING)
    @Column(insertable = false, updatable = false)
    protected RoleEnum role;

    // Constructeur par défaut requis par JPA
    public Utilisateur() {}

    // --- Getters et Setters nécessaires ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotDePasseHache() { return motDePasseHache; }
    public void setMotDePasseHache(String motDePasseHache) { this.motDePasseHache = motDePasseHache; }

    public RoleEnum getRole() { return role; }
    public void setRole(RoleEnum role) { this.role = role; }
}