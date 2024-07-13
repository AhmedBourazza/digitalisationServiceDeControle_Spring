package org.system.digitalisationservicedecontrole.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity

public class Equipement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEquipement ;
    private String nom ;
    private String description ;
    private String frequence ;

    private String codification ;
    private Long quantite ;
    private String titreFormulaire ;

    @OneToMany(mappedBy = "equipement")
    private Collection<Formulaire> formulaires ;

    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image image;
}
