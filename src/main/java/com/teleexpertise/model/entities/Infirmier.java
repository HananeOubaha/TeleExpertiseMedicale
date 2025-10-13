package com.teleexpertise.model.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "infirmier")
// Indique que la clé primaire de cette table est aussi la clé étrangère de la table 'utilisateur'
@PrimaryKeyJoinColumn(name = "id")
// CRUCIAL : Mappe cette classe à la valeur 'INFIRMIER' dans la colonne de discrimination (rôle)
@DiscriminatorValue("INFIRMIER")
public class Infirmier extends Utilisateur {

    // Constructeur par défaut requis par JPA
    public Infirmier() {

    }

}
