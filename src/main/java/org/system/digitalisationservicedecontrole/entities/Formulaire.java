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

public class Formulaire {

    private Long idFormulaire ;
    private String matriculeExemplaireEquipement ;
    private Date dateControle ;
    private String Proprietaire ;

    private Long idControleur_ ;
    private Long idEquipement_ ;
    private Long idUnite_ ;


}
