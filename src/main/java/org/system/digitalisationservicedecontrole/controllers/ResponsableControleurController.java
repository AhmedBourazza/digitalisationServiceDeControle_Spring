package org.system.digitalisationservicedecontrole.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResponsableControleurController {

    @GetMapping("/responsableControleur/listeControleurs")
    public String Controleurs() {
        return "RC_ListeControleurs";
    }
    @GetMapping("/responsableControleur/dashboard")
    public String Dashboard() {
        return "RC_dashboard";
    }

    @GetMapping("/responsableControleur/editProfile")
    public String EditProfile() {
        return "RC_editProfile";
    }

    @GetMapping("/responsableControleur/monProfile")
    public String MonProfile() {
        return "RC_monProfile";
    }
    @GetMapping("/responsableControleur/gestionEquipement/modification")
    public String modificationEquipement() {
        return "RC_gestionEquipements_modification";
    }
}
