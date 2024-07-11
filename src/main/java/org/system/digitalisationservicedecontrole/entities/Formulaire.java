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
    private Date dateControle ;
    private String Proprietaire ;
    @ManyToOne()
    private Controleur controleur ;
    @ManyToOne()
    private Equipement equipement ;
    @ManyToOne()
    private Unite unite ;

    @OneToMany(mappedBy = "formulaire")
    private Collection<Section> sections;


}
