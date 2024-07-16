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

    @GetMapping("/responsableControleur/gestionEntites/ajout")
    public String afficherFormEntite() {
        return "RC_gestionEntites_ajout";


    }

    @PostMapping("/responsableControleur/gestionEntites/ajout")
    public String AjoutEntite(@ModelAttribute("entite") Entite entite) {
        entiteRepo.save(entite);
        return "redirect:/responsableControleur/gestionEntites";


    }
    @GetMapping("/responsableControleur/gestionEntites/suppression/{id}")
    public String supprimerEntiteGet(@PathVariable("id") Long id) {
        entiteRepo.deleteById(id);
        return "redirect:/responsableControleur/gestionEntites";
    }
    @GetMapping("/responsableControleur/gestionEntites/modification/{id}")
    public String modificationEntite(@PathVariable("id") Long id, Model model) {
        Optional<Entite> entiteOptional = entiteRepo.findById(id);
        if (entiteOptional.isPresent()) {
            model.addAttribute("entite", entiteOptional.get());
            return "RC_gestionEntites_modification"; // Ensure this matches your template name
        } else {
            // Handle entity not found
            return "redirect:/responsableControleur/gestionEntites";
        }
    }



    @PostMapping("/responsableControleur/gestionEntites/modification/{id}")
    public String enregistrerModificationsEntite(@PathVariable("id") Long id, @ModelAttribute("entite") Entite entite) {
        entite.setIdEntite(id); // Ensure the ID is set for update
        entiteRepo.save(entite);
        return "redirect:/responsableControleur/gestionEntites";
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
