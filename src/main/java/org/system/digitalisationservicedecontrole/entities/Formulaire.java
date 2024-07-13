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

public class Formulaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFormulaire ;
    private String matriculeExemplaireEquipement ;
    private String secteur ;
    private Date dateControle ;
    private String Proprietaire ;
    @ManyToOne()
    @JoinColumn(name = "IdControleur_")
    private Controleur controleur ;
    @ManyToOne()
    @JoinColumn(name = "IdEquipement_")
    private Equipement equipement ;
    @ManyToOne()
    @JoinColumn(name = "IdUnite_")
    private Unite unite ;
    @OneToMany(mappedBy = "formulaire")
    private Collection<Section> sections;

    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image image;
}
