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

public class Controleur {
    private Long idControleur ;
    private String nom ;
    private String prenom ;
    private String matricule ;
    private Date dateIntegration ;
    private Date dateEmbauche ;
    private Long idResponsableControleur_ ;


}
