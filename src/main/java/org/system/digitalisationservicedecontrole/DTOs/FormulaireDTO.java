package org.system.digitalisationservicedecontrole.DTOs;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data

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
    public FormulaireDTO() {
        // Initialisation avec une capacité pour 350 éléments
        reponses = new ArrayList<>(350);
        // Remplir la liste avec des instances de ReponseDTO
        for (int i = 0; i < 350; i++) {
            reponses.add(new ReponseDTO());
        }}

    // Getters et Setters
}