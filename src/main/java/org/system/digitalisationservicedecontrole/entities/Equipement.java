package org.system.digitalisationservicedecontrole.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Equipement {

    private Long idEquipement ;
    private String nom ;
    private Date description ;
    private String frequence ;

    private Long codification ;
    private Long quantite ;
    private Long titreFormulaire ;

}
