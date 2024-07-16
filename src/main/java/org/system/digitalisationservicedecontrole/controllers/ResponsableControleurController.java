package org.system.digitalisationservicedecontrole.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.system.digitalisationservicedecontrole.entities.Controleur;
import org.system.digitalisationservicedecontrole.entities.Entite;
import org.system.digitalisationservicedecontrole.entities.Equipement;
import org.system.digitalisationservicedecontrole.entities.Unite;
import org.system.digitalisationservicedecontrole.repositories.ControleurRepo;
import org.system.digitalisationservicedecontrole.repositories.EntiteRepo;
import org.system.digitalisationservicedecontrole.repositories.EquipementRepo;
import org.system.digitalisationservicedecontrole.repositories.UniteRepo;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

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
    @Autowired
    private UniteRepo uniteRepp ;


    @GetMapping("/responsableControleur/login")
    public String login() {
        return "RC_login";
    }

    @GetMapping("/responsableControleur/gestionControleurs")
    public String Controleurs(Model m) {
        List<Controleur> listeControleurs = controleurRepo.findAll();

        m.addAttribute("listeControleurs", listeControleurs);
        return "RC_gestionControleurs";
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

//-----------------------
    @GetMapping("/responsableControleur/gestionEquipement/ajout")
    public String ajoutEquipementForm(Model model) {
        model.addAttribute("equipement", new Equipement());
        return "RC_gestionEquipements_ajout";
    }

    @PostMapping("/responsableControleur/gestionEquipement/ajout")
    public String ajoutEquipement(@ModelAttribute Equipement equipement) {
        equipementRepo.save(equipement);
        return "redirect:/responsableControleur/gestionEquipements";
    }

    @PostMapping("/responsableControleur/gestionEquipement/suppression/{id}")
    public String supprimerEquipement(@PathVariable("id") Long id) {
        equipementRepo.deleteById(id);
        return "redirect:/responsableControleur/gestionEquipements";
    }

    @GetMapping("/responsableControleur/gestionEquipement/modification/{id}")
    public String modificationEquipement(@PathVariable("id") Long id, Model model) {
        Optional<Equipement> equipementOptional = equipementRepo.findById(id);
        if (equipementOptional.isPresent()) {
            model.addAttribute("equipement", equipementOptional.get());
            return "RC_gestionEquipements_modification";
        } else {

            return "redirect:/responsableControleur/gestionEquipements";
        }

    }

    @PostMapping("/responsableControleur/gestionEquipement/modification")
    public String enregistrerModificationsEquipement(@ModelAttribute("equipement") Equipement equipement) {

        equipementRepo.save(equipement);

        return "redirect:/responsableControleur/gestionEquipements";
    }


    //------------------------



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
    public String AjoutUniteForm(Model model) {
        model.addAttribute("unite", new Unite());
        return "RC_gestionUnites_ajout";

    }

    @PostMapping("/responsableControleur/gestionUnites/ajout")
    public String ajoutUnite(@ModelAttribute Unite unite) {
        uniteRepo.save(unite);
        return "redirect:/responsableControleur/gestionUnites";
    }

    @GetMapping("/responsableControleur/gestionUnites/modification")
    public String modificationUnite() {
        return "RC_gestionUnites_modification";


    }
}
