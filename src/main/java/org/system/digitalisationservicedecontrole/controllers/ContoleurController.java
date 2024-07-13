package org.system.digitalisationservicedecontrole.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ContoleurController {

    @GetMapping("/controleur/login")
    public String login() {
        return "C_login"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }

    @GetMapping("/controleur/listeEquipements")
    public String afficherECOEquipements() {
        return "C_listeEquipements"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }


    @GetMapping("/controleur/monProfile")
    public String afficherProfile() {
        return "C_monProfile"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }

    @GetMapping("/controleur/formulaire1")
    public String afficherFormulaire() {
        return "C_formulaire1"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }


}
