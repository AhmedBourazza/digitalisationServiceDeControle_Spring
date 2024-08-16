package org.system.digitalisationservicedecontrole.DTOs;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FormulaireDTO {
    private Long idEquipement;
    private Long idUnite;
    private String secteur;
    private String proprietaire;
    private LocalDate dateControle;
    private String matriculeExemplaireEquipement;
    private Long idControleur;
    private List<ReponseDTO> reponses  ;

    // Getters et Setters
}