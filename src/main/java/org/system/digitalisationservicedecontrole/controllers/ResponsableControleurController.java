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
    @GetMapping("/responsableControleur/gestionEquipement/ajout")
    public String ajoutEquipement() {
        return "RC_gestionEquipements_ajout";
    }

    @GetMapping("/responsableControleur/GestionEquipements")
    public String GestionEquipements() {
        return "RC_gestionListeEquipements"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }
}
