package tn.esprit.insurance.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Devis implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idDev;

    double prime;
    double franchise;
    @Enumerated(EnumType.STRING)
    StatPol statPol;
    LocalDate startinsurance;
    LocalDate endinsurance;
    Boolean archived;

    //Voiture
    String marque;
    float puissance;
    LocalDate miseencirc;
    String utilisation; //usage prv ou utilitaire
    int bonus_malus;
    double valneuf;
    double valvenal;

    //Habitation
    String habitation;
    int nbrchambre;
    float superficiech;
    float superficietot;
    double capitalmob;
    double capitalphoto;
    double capitaldamage;

    //Bateau
    String nom_bat;
    String type_bat;
    int numconge;
    int immatriculation;
    String port_dattache;
    LocalDate construct_bat;
    double val_bat;
    int nbr_chevaux;
    int longueur;
    int nbr_places;

    //Scolarité
    String duree;
    String periodicite;

    //Santé
    String medical;
    //periodicite
    //duree

    //Voyage
    String pays;
    String duree_voy;
    int nbr_totperso;
    double billet;


    @JsonIgnore
    @ManyToOne
    Insurance insurance;

    @OneToMany(mappedBy = "devis", cascade = CascadeType.PERSIST)
    Set<Sinister> sinisters;

    public Devis(Devis devis, Insurance insurance, Set<Sinister> sinistres) {
    }
}
