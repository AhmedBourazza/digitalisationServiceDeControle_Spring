package org.system.digitalisationservicedecontrole.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResponsableControleurController {
    @GetMapping("/responsableControleur/login")
    public String login() {
        return "RC_login";
    }

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

    @GetMapping("/responsableControleur/gestionEquipements")
    public String GestionEquipements() {
        return "RC_gestionListeEquipements"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }
    @GetMapping("/responsableControleur/gestionEntites")
    public String gestionEntites() {
        return "RC_gestionEntites";}

    @GetMapping("/responsableControleur/gestionEntites/modification")
    public String modificationEntite() {
        return "RC_gestionEntites_modification";

    }

    @GetMapping("/responsableControleur/gestionEntites/ajout")
    public String AjoutEntite() {
        return "RC_gestionEntites_ajout";


    }
    @GetMapping("/responsableControleur/gestionUnites")
    public String gestionUnite() {
        return "RC_gestionUnites";


    }

    @GetMapping("/responsableControleur/gestionUnites/ajout")
    public String AjoutUnite() {
        return "RC_gestionUnites_ajout";


    }
    @GetMapping("/responsableControleur/gestionUnites/modification")
    public String modificationUnite() {
        return "RC_gestionUnites_modification";


    }
}
