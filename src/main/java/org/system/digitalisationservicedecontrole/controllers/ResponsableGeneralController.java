package org.system.digitalisationservicedecontrole.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.system.digitalisationservicedecontrole.entities.Controleur;
import org.system.digitalisationservicedecontrole.entities.Entite;
import org.system.digitalisationservicedecontrole.entities.Equipement;
import org.system.digitalisationservicedecontrole.entities.Unite;
import org.system.digitalisationservicedecontrole.repositories.ControleurRepo;
import org.system.digitalisationservicedecontrole.repositories.EntiteRepo;
import org.system.digitalisationservicedecontrole.repositories.EquipementRepo;
import org.system.digitalisationservicedecontrole.repositories.UniteRepo;

import java.util.List;
import java.util.Optional;

@Controller
public class ResponsableGeneralController {

    @Autowired
    private ControleurRepo controleurRepo;
    @Autowired
    private EntiteRepo entiteRepo;
    @Autowired
    private UniteRepo uniteRepo;
    @Autowired
    private EquipementRepo equipementRepo ;

    @GetMapping("/responsableGeneral/login")
    public String login() {
        return "RG_login"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }


    @GetMapping("/responsableGeneral/gestionControleurs")
    public String Controleurs(Model m) {
        List<Controleur> listeControleurs = controleurRepo.findAll();

        m.addAttribute("listeControleurs", listeControleurs);
        return "RG_gestionControleurs"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }

    @GetMapping("/responsableGeneral/dashboard")
    public String dashboard() {
        return "RG_dashboard";
    }
    @GetMapping("/responsableGeneral/editProfile")
    public String EditProfile() {
        return "RG_editProfile"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }

    @GetMapping("/responsableGeneral/monProfile")
    public String MonProfile() {
        return "RG_monProfile"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }

 

    @GetMapping("/responsableGeneral/gestionEquipements")
    public String GestionEquipements(Model m) {
        List<Equipement> listeEquipements = equipementRepo.findAll();
        m.addAttribute("listeEquipements", listeEquipements);
        return "RG_gestionListeEquipements"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }
    @GetMapping("/responsableGeneral/gestionEquipements/modification")
    public String modificationEquipement() {
        return "RG_gestionEquipements_modification";

    }

    //----------------------------------------
    @GetMapping("/responsableGeneral/gestionEquipement/ajout")
    public String ajoutEquipementForm(Model model) {
        model.addAttribute("equipement", new Equipement());
        return "RG_gestionEquipements_ajout";
    }

    @PostMapping("/responsableGeneral/gestionEquipement/ajout")
    public String ajoutEquipement(@ModelAttribute Equipement equipement) {
        equipementRepo.save(equipement);
        return "redirect:/responsableGeneral/gestionEquipements";
    }

    @PostMapping("/responsableGeneral/gestionEquipement/suppression/{id}")
    public String supprimerEquipement(@PathVariable("id") Long id) {
        equipementRepo.deleteById(id);
        return "redirect:/responsableGeneral/gestionEquipements";
    }


    @GetMapping("/responsableGeneral/gestionEquipement/modification/{id}")
    public String modificationEquipement(@PathVariable("id") Long id, Model model) {
        Optional<Equipement> equipementOptional = equipementRepo.findById(id);
        if (equipementOptional.isPresent()) {
            model.addAttribute("equipement", equipementOptional.get());
            return "RG_gestionEquipements_modification";
        } else {

            return "redirect:/responsableGeneral/gestionEquipements";
        }

    }

    @PostMapping("/responsableGeneral/gestionEquipement/modification")
    public String enregistrerModificationsEquipement(@ModelAttribute("equipement") Equipement equipement) {

        equipementRepo.save(equipement);

        return "redirect:/responsableGeneral/gestionEquipements";
    }


    //--------------------------------------


    @GetMapping("/responsableGeneral/gestionEntites")
    public String GestionEntites(Model m) {
        List<Entite> listeEntites = entiteRepo.findAll();
        m.addAttribute("listeEntites", listeEntites);
        return "RG_gestionEntites";}

    @GetMapping("/responsableGeneral/gestionEntites/modification")
    public String modificationEntite() {
        return "RG_gestionEntites_modification";

    }
    @GetMapping("/responsableGeneral/gestionEntites/ajout")
    public String afficherFormEntite() {
        return "RG_gestionEntites_ajout";


    }

    @PostMapping("/responsableGeneral/gestionEntites/ajout")
    public String AjoutEntite(@ModelAttribute("entite") Entite entite) {
        entiteRepo.save(entite);
        return "redirect:/responsableGeneral/gestionEntites";


    }
    @GetMapping("/responsableGeneral/gestionEntites/suppression/{id}")
    public String supprimerEntiteGet(@PathVariable("id") Long id) {
        entiteRepo.deleteById(id);
        return "redirect:/responsableGeneral/gestionEntites";
    }

    @GetMapping("/responsableGeneral/gestionUnites")
    public String gestionUnite(Model m) {
        List<Unite> listeUnites = uniteRepo.findAll();
        m.addAttribute("listeUnites", listeUnites);
        return "RG_gestionUnites";


    }

    @GetMapping("/responsableGeneral/gestionUnites/ajout")
    public String AjoutUnite() {
        return "RG_gestionUnites_ajout";
    }



    @GetMapping("/responsableGeneral/gestionUnites/modification")
    public String modificationUnite() {
        return "RG_gestionUnites_modification";


    }



    @GetMapping("/responsableGeneral/gestionResponsableControleurs")
    public String responsableControleurs() {

        return "RG_gestionResponsableControleurs"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }
}
