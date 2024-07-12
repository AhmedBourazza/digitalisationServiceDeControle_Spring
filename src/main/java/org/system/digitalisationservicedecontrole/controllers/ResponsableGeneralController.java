package org.system.digitalisationservicedecontrole.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResponsableGeneralController {

    @GetMapping("/Controleurs")
    public String Controleurs() {
        return "ECH_controleurs"; // Assurez-vous que "ECO_Equipements.html" est présent dans le dossier templates
    }
    @GetMapping("/Dashboard")
    public String Dashboard() {
        return "ECH_dashboard"; // Assurez-vous que "ECO_Equipements.html" est présent dans le dossier templates
    }

    @GetMapping("/EditProfile")
    public String EditProfile() {
        return "ECH_editProfile"; // Assurez-vous que "ECO_Equipements.html" est présent dans le dossier templates
    }

    @GetMapping("/MonProfile")
    public String MonProfile() {
        return "ECH_MonProfile"; // Assurez-vous que "ECO_Equipements.html" est présent dans le dossier templates
    }
}
