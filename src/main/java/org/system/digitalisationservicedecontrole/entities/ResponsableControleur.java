package org.system.digitalisationservicedecontrole.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
@Data
@NoArgsConstructor  @AllArgsConstructor
@ToString


public class ResponsableControleur {
    private Long idResponsableControleur ;
    private String nom ;
    private String prenom ;
    private String matricule ;
    private Date dateEmbauche ;
    private Date dateIntegration ;
    private String grade ;
    private Long idResponsableGeneral_ ;
}
