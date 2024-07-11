package org.system.digitalisationservicedecontrole.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;
import java.util.Date;
@Data
@NoArgsConstructor  @AllArgsConstructor
@ToString
@Entity


public class ResponsableControleur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idResponsableControleur ;
    private String nom ;
    private String prenom ;
    private String matricule ;
    private Date dateEmbauche ;
    private Date dateIntegration ;
    private String grade ;

    @OneToMany(mappedBy="responsableControleur")
    private Collection<Controleur>  controleurs ;

   @ManyToOne()
    private ResponsableGeneral responsableGeneral ;
}
