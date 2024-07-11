package org.system.digitalisationservicedecontrole.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Question {
    private Long idQuestion;
    private String enonce ;
    private String reponse ;
    private Long idSection_ ;

}
