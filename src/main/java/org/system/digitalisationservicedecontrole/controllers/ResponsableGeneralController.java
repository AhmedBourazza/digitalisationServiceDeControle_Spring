package org.system.digitalisationservicedecontrole.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.system.digitalisationservicedecontrole.entities.*;
import org.system.digitalisationservicedecontrole.repositories.*;

import java.io.IOException;
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
    @Autowired
    private ResponsableControleurRepo responsableControleurRepo;

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

    //*********************************Gestion Equipement*************************************

    @GetMapping("/responsableGeneral/gestionEquipement/ajout")
    public String afficherEquipementForm(Model model) {
        model.addAttribute("equipement", new Equipement());
        return "RG_gestionEquipements_ajout";
    }

    @PostMapping("/responsableGeneral/gestionEquipement/ajout")
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

    @PostMapping("/responsableGeneral/gestionEquipement/modification/{id}")
    public String enregistrerModificationsEquipement(@ModelAttribute("equipement") Equipement equipement,
                                                     @RequestParam("imageFile") MultipartFile imageFile,
                                                     RedirectAttributes redirectAttributes) {
        try {
            if (!imageFile.isEmpty()) {
                equipement.setImageData(imageFile.getBytes());
            } else {

            Equipement existingEquipement =equipementRepo.findById(equipement.getIdEquipement()).orElseThrow(() -> new IllegalArgumentException("Controleur non trouvé avec l'id: " + equipement.getIdEquipement()));
                equipement.setImageData(existingEquipement.getImageData());

            }
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'erreur
        }

        equipementRepo.save(equipement);

        redirectAttributes.addFlashAttribute("successMessage", "Les modifications ont été enregistrées avec succès.");
        return "redirect:/responsableGeneral/gestionEquipements";
    }

    //--------------------------------------
    //*********************************Gestion Entités*************************************


    @GetMapping("/responsableGeneral/gestionEntites")
    public String GestionEntites(Model m) {
        List<Entite> listeEntites = entiteRepo.findAll();
        m.addAttribute("listeEntites", listeEntites);
        return "RG_gestionEntites";}




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
    @GetMapping("/responsableGeneral/gestionEntites/modification/{id}")
    public String modificationEntite(@PathVariable("id") Long id, Model model) {
        Optional<Entite> entiteOptional = entiteRepo.findById(id);
        if (entiteOptional.isPresent()) {
            model.addAttribute("entite", entiteOptional.get());
            return "RG_gestionEntites_modification"; // Ensure this matches your template name
        } else {
            // Handle entity not found
            return "redirect:/responsableGeneral/gestionEntites";
        }
    }



    @PostMapping("/responsableGeneral/gestionEntites/modification/{id}")
    public String enregistrerModificationsEntite(@PathVariable("id") Long id, @ModelAttribute("entite") Entite entite) {
        entite.setIdEntite(id); // Ensure the ID is set for update
        entiteRepo.save(entite);
        return "redirect:/responsableGeneral/gestionEntites";
    }
    //*********************************Gestion unités*************************************

    @GetMapping("/responsableGeneral/gestionUnites")
    public String gestionUnite(Model m) {
        List<Unite> listeUnites = uniteRepo.findAll();
        m.addAttribute("listeUnites", listeUnites);
        return "RG_gestionUnites";


    }

    //---------- AJOUTER UNITE---------------------------------------

    @GetMapping("/responsableGeneral/gestionUnites/ajout")
    public String AjoutUniteForm(Model model) {
        List<Entite> listeEntites = entiteRepo.findAll();
        model.addAttribute("listeEntites", listeEntites);
        model.addAttribute("unite", new Unite());
        return "RG_gestionUnites_ajout";

    }

    @PostMapping("/responsableGeneral/gestionUnites/ajout")
    public String ajoutUnite(@ModelAttribute Unite unite) {
        uniteRepo.save(unite);
        return "redirect:/responsableGeneral/gestionUnites";
    }


    //------------ Supprimer l'unité ---------------------------------------------
    @PostMapping("/responsableGeneral/gestionUnites/suppression/{id}")
    public String supprimerUnite(@PathVariable("id") Long id) {
        uniteRepo.deleteById(id);
        return "redirect:/responsableGeneral/gestionUnites";
    }

    @GetMapping("/responsableGeneral/gestionUnites/modification")
    public String modificationUnite() {
        return "RG_gestionUnites_modification";


    }


//******************** GESTION DES RESPONSABLE DES CONTROLEURS**********************
    @GetMapping("/responsableGeneral/gestionResponsableControleurs")
    public String responsableControleurs(Model m) {
        List<ResponsableControleur> listeResponsableControleurs = responsableControleurRepo.findAll();
        m.addAttribute("listeResponsableControleurs", listeResponsableControleurs );

        return "RG_gestionResponsableControleurs";
    }

    //------Ajout de responsable controleur----------------------
    @GetMapping("/responsableGeneral/gestionResponsableControleurs/ajout")
    public String ajoutResponsableControleursForm(Model m) {
        m.addAttribute("responsableControleur", new ResponsableControleur());
        return "RG_gestionResponsableControleurs_ajout";
    }
    @PostMapping("/responsableGeneral/gestionResponsableControleurs/ajout")
    public String ajoutResponsableControleurs(@ModelAttribute ResponsableControleur responsableControleur) {
        responsableControleurRepo.save(responsableControleur);
        return "redirect:/responsableGeneral/gestionResponsableControleurs";
    }
    //--------Suppression reponsable controleur-------------------
    @PostMapping("/responsableGeneral/gestionResponsableControleurs/suppression/{id}")
    public String supprimerResponsableControleur(@PathVariable("id") Long id) {
        responsableControleurRepo.deleteById(id);
        return "redirect:/responsableGeneral/gestionResponsableControleurs";
    }
    //--------Modification de responsable de controleur---------------------

    @GetMapping("/responsableGeneral/gestionResponsableControleurs/modification")
    public String modifierResponsableControleurs(Model m) {

        return "RG_gestionResponsableControleurs_modification";
    }
    //*********************************Gestion Controleurs*************************************
    //------ Ajout D'un CONTROLEURS -----------------------------------------

    @GetMapping("/responsableGeneral/gestionControleurs/ajout")
    public String ajoutControleurForm(Model model) {
        model.addAttribute("controleur", new Controleur());
        return "RG_gestionControleurs_ajout";
    }

    @PostMapping("/responsableGeneral/gestionControleurs/ajout")
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
        return "redirect:/responsableGeneral/gestionControleurs";
    }

    //*******Suppression d'un CONTROLEURS*************************************
    @PostMapping("/responsableGeneral/gestionControleurs/suppression/{id}")
    public String supprimerControleur(@PathVariable("id") Long id) {
        controleurRepo.deleteById(id);
        return "redirect:/responsableGeneral/gestionControleurs";
    }

    //--------------------------





    @GetMapping("/responsableGeneral/gestionControleurs/modification/{id}")
    public String modificationControleur(@PathVariable("id") Long id, Model model) {
        Optional<Controleur> controleurOptional = controleurRepo.findById(id);
        if (controleurOptional.isPresent()) {
            model.addAttribute("controleur", controleurOptional.get());
            return "RG_gestionControleurs_modification"; // Nom de la vue Thymeleaf
        } else {
            // Gestion du cas où le contrôleur n'est pas trouvé
            return "redirect:/responsableGeneral/gestionControleurs";
        }
    }




    @PostMapping("/responsableGeneral/gestionControleurs/modification/{id}")
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
        return "redirect:/responsableGeneral/gestionControleurs"; // Rediriger vers la liste des contrôleurs après modification
    }
}
