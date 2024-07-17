package org.system.digitalisationservicedecontrole.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
public String afficherEquipementForm(Model model) {
    model.addAttribute("equipement", new Equipement());
    return "RC_gestionEquipements_ajout";
}

    @PostMapping("/responsableControleur/gestionEquipement/ajout")
    public String ajoutEquipement(@ModelAttribute Equipement equipement,
                                  @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            if (!imageFile.isEmpty()) {
                equipement.setImageData(imageFile.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

//---------- AJOUTER UNITE---------------------------------------

    @GetMapping("/responsableControleur/gestionUnites/ajout")
    public String AjoutUniteForm(Model model) {
        List<Entite> listeEntites = entiteRepo.findAll();
        model.addAttribute("listeEntites", listeEntites);
        model.addAttribute("unite", new Unite());
        return "RC_gestionUnites_ajout";

    }

    @PostMapping("/responsableControleur/gestionUnites/ajout")
    public String ajoutUnite(@ModelAttribute Unite unite) {
        uniteRepo.save(unite);
        return "redirect:/responsableControleur/gestionUnites";
    }
    //------------------------------------------------
// Afficher le formulaire de modification
    @GetMapping("/responsableControleur/gestionUnites/modification/{id}")
    public String modificationUnite(@PathVariable("id") Long id, Model model) {
        List<Entite> listeEntites = entiteRepo.findAll();
        model.addAttribute("listeEntites", listeEntites);
        Optional<Unite> uniteOptional = uniteRepo.findById(id);
        if (uniteOptional.isPresent()) {
            model.addAttribute("unite", uniteOptional.get());
            return "RC_gestionUnites_modification"; // Assurez-vous que ce nom correspond à votre template
        } else {
            // Gérer le cas où l'unité n'est pas trouvée
            return "redirect:/responsableControleur/gestionUnites";
        }
    }

    // Enregistrer les modifications
    @PostMapping("/responsableControleur/gestionUnites/modification/{id}")
    public String enregistrerModificationsUnite(@PathVariable("id") Long id, @ModelAttribute("unite") Unite unite) {
        unite.setIdUnite(id); // Assurez-vous que l'ID est défini pour la mise à jour
        uniteRepo.save(unite);
        return "redirect:/responsableControleur/gestionUnites";
    }

    //------------ Supprimer l'unité ---------------------------------------------
    @PostMapping("/responsableControleur/gestionUnites/suppression/{id}")
    public String supprimerUnite(@PathVariable("id") Long id) {
        uniteRepo.deleteById(id);
        return "redirect:/responsableControleur/gestionUnites";
    }


    @GetMapping("/responsableControleur/gestionControleurs/modification/{id}")
    public String modificationControleur(@PathVariable("id") Long id, Model model) {
        Optional<Controleur> controleurOptional = controleurRepo.findById(id);
        if (controleurOptional.isPresent()) {
            model.addAttribute("controleur", controleurOptional.get());
            return "RC_gestionControleurs_modification"; // Nom de la vue Thymeleaf
        } else {
            // Gestion du cas où le contrôleur n'est pas trouvé
            return "redirect:/responsableControleur/gestionControleurs";
        }
    }




    @PostMapping("/responsableControleur/gestionControleurs/modification/{id}")
    public String processModificationForm(@PathVariable("id") Long id,
                                          @ModelAttribute Controleur controleur,
                                          @RequestParam("imageFile") MultipartFile imageFile,
                                          RedirectAttributes redirectAttributes) {
        try {
            if (!imageFile.isEmpty()) {
                controleur.setImageData(imageFile.getBytes());
            } else {
                // Si aucun fichier n'est téléchargé, récupérez l'image existante
                Controleur existingControleur = controleurRepo.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Controleur non trouvé avec l'id: " + id));
                controleur.setImageData(existingControleur.getImageData());
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'erreur
        }

        controleurRepo.save(controleur); // Sauvegarder les modifications

        redirectAttributes.addFlashAttribute("successMessage", "Les modifications ont été enregistrées avec succès.");
        return "redirect:/responsableControleur/gestionControleurs"; // Rediriger vers la liste des contrôleurs après modification
    }

    //------ Ajout D'un CONTROLEURS -----------------------------------------
    @GetMapping("/responsableControleur/gestionControleurs/ajout")
    public String ajoutControleurForm(Model model) {
        model.addAttribute("controleur", new Controleur());
        return "RC_gestionControleurs_ajout";
    }

    @PostMapping("/responsableControleur/gestionControleurs/ajout")
    public String ajoutControleur(@ModelAttribute Controleur controleur,
                                  @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            if (!imageFile.isEmpty()) {
                controleur.setImageData(imageFile.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
            //ho
        }
        controleurRepo.save(controleur);
        return "redirect:/responsableControleur/gestionControleurs";
    }

    //*******Suppression d'un CONTROLEURS*************************************
    @PostMapping("/responsableControleur/gestionControleurs/suppression/{id}")
    public String supprimerControleur(@PathVariable("id") Long id) {
        controleurRepo.deleteById(id);
        return "redirect:/responsableControleur/gestionControleurs";
    }
}
