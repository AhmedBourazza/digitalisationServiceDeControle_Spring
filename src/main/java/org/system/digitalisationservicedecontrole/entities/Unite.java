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

public class Unite {

    private Long idUnite ;
    private String nom ;
    private String emplacement;
    private String Description ;

    private Long idEntite_ ;

}
