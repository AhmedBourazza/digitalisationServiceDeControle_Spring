package org.system.digitalisationservicedecontrole.DTOs;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReponseDTO {
    private String questionText;
    private String reponseText;
    private String justification;
}
