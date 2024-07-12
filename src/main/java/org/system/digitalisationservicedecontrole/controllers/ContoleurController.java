package org.system.digitalisationservicedecontrole.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ContoleurController {

    @GetMapping("/Equipements")
    public String afficherECOEquipements() {
        return "ECO_Equipements"; // Assurez-vous que "ECO_Equipements.html" est présent dans le dossier templates
    }


    @GetMapping("/Profile")
    public String afficherProfile() {
        return "ECO_Profile"; // Assurez-vous que "ECO_Equipements.html" est présent dans le dossier templates
    }

    @GetMapping("/Formulaire")
    public String afficherFormulaire() {
        return "ECO_formulaire1"; // Assurez-vous que "ECO_Equipements.html" est présent dans le dossier templates
    }


}
