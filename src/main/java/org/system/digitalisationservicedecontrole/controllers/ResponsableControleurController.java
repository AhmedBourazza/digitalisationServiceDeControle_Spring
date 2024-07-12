package org.system.digitalisationservicedecontrole.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResponsableControleurController {

    @GetMapping("/responsableControleur/listeControleurs")
    public String Controleurs() {
        return "RC_ListeControleurs"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }
    @GetMapping("/responsableControleur/dashboard")
    public String Dashboard() {
        return "RC_dashboard"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }

    @GetMapping("/responsableControleur/editProfile")
    public String EditProfile() {
        return "RC_editProfile"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }

    @GetMapping("/responsableControleur/monProfile")
    public String MonProfile() {
        return "RC_monProfile"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }

    @GetMapping("/responsableControleur/GestionEquipements")
    public String GestionEquipements() {
        return "RC_GestionListeEquipements"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }
}
