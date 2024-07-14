package org.system.digitalisationservicedecontrole.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.system.digitalisationservicedecontrole.entities.Controleur;
import org.system.digitalisationservicedecontrole.entities.Entite;
import org.system.digitalisationservicedecontrole.entities.Equipement;
import org.system.digitalisationservicedecontrole.entities.Unite;
import org.system.digitalisationservicedecontrole.repositories.ControleurRepo;
import org.system.digitalisationservicedecontrole.repositories.EntiteRepo;
import org.system.digitalisationservicedecontrole.repositories.EquipementRepo;
import org.system.digitalisationservicedecontrole.repositories.UniteRepo;

import java.util.List;

@Controller
public class ResponsableControleurController {
    @Autowired
    private ControleurRepo controleurRepo;
    @Autowired
    private EntiteRepo entiteRepo;
    @Autowired
    private UniteRepo uniteRepo;
    @Autowired
    private EquipementRepo equipementRepo;


    @GetMapping("/responsableControleur/login")
    public String login() {
        return "RC_login";
    }

    @GetMapping("/responsableControleur/listeControleurs")
    public String Controleurs(Model m) {
        List<Controleur> listeControleurs = controleurRepo.findAll();

        m.addAttribute("listeControleurs", listeControleurs);
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
    public String GestionEquipements(Model m ) {
        List<Equipement> listeEquipements = equipementRepo.findAll();
        m.addAttribute("listeEquipements", listeEquipements );
        return "RC_gestionListeEquipements"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }


    @GetMapping("/responsableControleur/gestionEntites")
    public String gestionEntites(Model m ) {
        List<Entite> listeEntites = entiteRepo.findAll();

        m.addAttribute("listeEntites", listeEntites);
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
    public String gestionUnite(Model m) {
        List<Unite> listeUnites = uniteRepo.findAll();

        m.addAttribute("listeUnites", listeUnites);
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
