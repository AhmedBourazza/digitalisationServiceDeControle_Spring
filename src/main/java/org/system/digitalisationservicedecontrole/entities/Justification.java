package org.system.digitalisationservicedecontrole.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Justification {
    private Long idJustification;
    private String enonce ;
    private Long idQuestion_ ;
}
