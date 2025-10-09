package com.teleexpertise.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "infirmier")
@PrimaryKeyJoinColumn(name = "id")
public class Infirmier extends Utilisateur {

}
