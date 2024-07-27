package org.system.digitalisationservicedecontrole.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.system.digitalisationservicedecontrole.DTOs.ControleurDTO;
import org.system.digitalisationservicedecontrole.configuration.GestionSession;
import org.system.digitalisationservicedecontrole.entities.*;
import org.system.digitalisationservicedecontrole.repositories.*;


import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private ResponsableGeneralRepo responsableGeneralRepo;
    @Autowired
    private GestionSession gestionSession;
    @Autowired
    private FormulaireRepo formulaireRepo;
    @Autowired
    private ObjectMapper jacksonObjectMapper;


    @GetMapping("/responsableGeneral/login")
    public String login() {
        return "RG_login"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }

    @GetMapping("/responsableGeneral/archiveFormulaire/parEquipements")
    public String RG_archiveFormulaire_ParEquipements(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        List<Equipement> listeEquipements = equipementRepo.findAll();
        model.addAttribute("listeEquipements", listeEquipements );
        return "RG_archiveFormulaire_ParEquipements";
    }
    @GetMapping("/responsableGeneral/archiveFormulaire/parEquipements/voirArchive/{id}")
    public String RG_archiveFormulaire_ParEquipements_voirArchive(@PathVariable("id") Long id, Model model, HttpSession session ) {
        Equipement equipement = equipementRepo.findById(id).get();
        gestionSession.prepareModel(session, model);

        List<Formulaire> formulairesByEquipement = formulaireRepo.findFormulaireByEquipement(equipement);
        model.addAttribute("formulairesByEquipement", formulairesByEquipement );
        return "RG_archiveFormulaire_ParEquipements_voirArchive";
    }

    @GetMapping("/responsableGeneral/gestionControleurs")
    public String Controleurs(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        List<Controleur> listeControleurs = controleurRepo.findAll();
        model.addAttribute("listeControleurs", listeControleurs);
        return "RG_gestionControleurs"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }
    @GetMapping("/responsableGeneral/dashboard")
    public String dashboard(Model model, HttpSession session) {
        gestionSession.prepareModel(session, model);

        // Récupération des formulaires
        List<Formulaire> formulaires = formulaireRepo.findAllOrderByDateControleDesc();

        // Définir le début et la fin du mois en cours
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1); // Premier jour du mois en cours
        Date startOfCurrentMonth = calendar.getTime(); // Premier jour du mois en cours
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)); // Dernier jour du mois en cours
        Date endOfCurrentMonth = calendar.getTime(); // Dernier jour du mois en cours

        // Obtenir le mois et l'année du mois en cours
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM yyyy", Locale.FRENCH);
        String monthYearCurrentMonth = monthFormat.format(calendar.getTime()); // Format du mois en cours

        // Récupération des contrôleurs pour le mois en cours
        List<Object[]> resultsCurrentMonth = controleurRepo.findAllControleursWithCount(startOfCurrentMonth, endOfCurrentMonth);

        // Mapper les résultats vers les DTO des contrôleurs
        List<ControleurDTO> controleursWithControlesCurrentMonth = resultsCurrentMonth.stream()
                .map(result -> {
                    ControleurDTO dto = new ControleurDTO();
                    dto.setIdControleur((Long) result[0]);
                    dto.setNom((String) result[1]);
                    dto.setPrenom((String) result[2]);
                    dto.setMatricule((String) result[3]);
                    dto.setDateIntegration((Date) result[4]);
                    dto.setDateEmbauche((Date) result[5]);
                    dto.setFormCount((Long) result[6]);
                    dto.setGrade((String) result[7]);
                    dto.setNumTele((String) result[8]);
                    dto.setUsername((String) result[9]);
                    dto.setEmail((String) result[10]);
                    dto.setPassword((String) result[11]);
                    dto.setImageData((byte[]) result[12]);
                    return dto;
                })
                .collect(Collectors.toList());

        // Obtenir les 5 meilleurs contrôleurs pour le mois en cours
        List<ControleurDTO> topControleurs = controleursWithControlesCurrentMonth.stream()
                .limit(5)
                .collect(Collectors.toList());

        // Log pour le débogage
        List<String> nomsControleurs = controleursWithControlesCurrentMonth.stream()
                .map(c -> c.getNom() + " " + c.getPrenom())
                .collect(Collectors.toList());
        List<Long> compteControles = controleursWithControlesCurrentMonth.stream()
                .map(ControleurDTO::getFormCount)
                .collect(Collectors.toList());

        // Ajout des données JSON au modèle
        try {
            model.addAttribute("nomsControleurs", jacksonObjectMapper.writeValueAsString(nomsControleurs));
            model.addAttribute("compteControles", jacksonObjectMapper.writeValueAsString(compteControles));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // Récupérer les données d'équipement pour le mois en cours
        List<Object[]> equipmentResultsCurrentMonth = equipementRepo.countControlsByEquipement(startOfCurrentMonth, endOfCurrentMonth);
        System.out.println("Résultats d'équipement (Mois en cours) : " + Arrays.deepToString(equipmentResultsCurrentMonth.toArray()));

        List<String> equipmentNamesCurrentMonth = new ArrayList<>();
        List<Long> equipmentCountsCurrentMonth = new ArrayList<>();

        for (Object[] result : equipmentResultsCurrentMonth) {
            if (result.length >= 2) {
                equipmentNamesCurrentMonth.add((String) result[0]); // Nom de l'équipement
                equipmentCountsCurrentMonth.add((Long) result[1]); // Compte des contrôles
            }
        }

        // Ajouter les données d'équipement au modèle
        model.addAttribute("equipmentNamesCurrentMonth", equipmentNamesCurrentMonth);
        model.addAttribute("equipmentCountsCurrentMonth", equipmentCountsCurrentMonth);

        // Ajouter le mois et l'année du mois en cours au modèle
        model.addAttribute("monthYearCurrentMonth", monthYearCurrentMonth);

        // Ajouter d'autres données au modèle
        model.addAttribute("formulaires", formulaires);
        model.addAttribute("controleurs", topControleurs);
        model.addAttribute("totalEquipements", equipementRepo.count());
        model.addAttribute("totalEntites", entiteRepo.count());
        model.addAttribute("totalUnites", uniteRepo.count());
        model.addAttribute("totalControleurs", controleurRepo.count());
        model.addAttribute("totalFormulaires", formulaireRepo.count());

        return "RG_dashboard";
    }
    @GetMapping("/responsableGeneral/editProfile")
    public String EditProfile(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        ResponsableGeneral responsableGeneral = responsableGeneralRepo.findById((Long)session.getAttribute("id"))
                .orElseThrow(() -> new RuntimeException("ResponsableGeneral not found"));

        // Ajoutez l'entité au modèle
        model.addAttribute("responsableGeneral", responsableGeneral);
        return "RG_editProfile"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }
    @PostMapping("/responsableGeneral/editProfile")
    public String modifierResponsableGeneral(
            @ModelAttribute ResponsableGeneral responsableGeneral,
            @RequestParam("imageData") MultipartFile imageData) {
        // Traitez l'image si un fichier est fourni
        if (imageData != null && !imageData.isEmpty()) {
            try {
                byte[] imageBytes = imageData.getBytes();
                responsableGeneral.setImageData(imageBytes);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("makhdaminch tsawer");
            }
        }

        // Trouvez et mettez à jour l'existant ResponsableGeneral
        ResponsableGeneral existingResponsableGeneral = responsableGeneralRepo.findById(responsableGeneral.getIdResponsableGeneral())
                .orElseThrow(() -> new RuntimeException("ResponsableGeneral not found"));

        existingResponsableGeneral.setNom(responsableGeneral.getNom());
        existingResponsableGeneral.setPrenom(responsableGeneral.getPrenom());
        existingResponsableGeneral.setDateIntegration(responsableGeneral.getDateIntegration());
        existingResponsableGeneral.setUsername(responsableGeneral.getUsername());
        existingResponsableGeneral.setMatricule(responsableGeneral.getMatricule());
        existingResponsableGeneral.setDateEmbauche(responsableGeneral.getDateEmbauche());
        existingResponsableGeneral.setNumTele(responsableGeneral.getNumTele());
        // Mettez à jour l'image uniquement si elle a été modifiée
        if (responsableGeneral.getImageData() != null && responsableGeneral.getImageData().length > 0) {
            existingResponsableGeneral.setImageData(responsableGeneral.getImageData());
        }

        // Sauvegardez les changements
        responsableGeneralRepo.save(existingResponsableGeneral);

        return "redirect:/responsableGeneral/monProfile";
    }

    @GetMapping("/responsableGeneral/monProfile")
    public String MonProfile(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        return "RG_monProfile"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }

 

    @GetMapping("/responsableGeneral/gestionEquipements")
    public String GestionEquipements(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        List<Equipement> listeEquipements = equipementRepo.findAll();
        model.addAttribute("listeEquipements", listeEquipements);
        return "RG_gestionListeEquipements"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }
    @GetMapping("/responsableGeneral/gestionEquipements/modification")
    public String modificationEquipement(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        return "RG_gestionEquipements_modification";

    }

    //*********************************Gestion Equipement*************************************

    @GetMapping("/responsableGeneral/gestionEquipement/ajout")
    public String afficherEquipementForm(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        model.addAttribute("equipement", new Equipement());
        return "RG_gestionEquipements_ajout";
    }

    @PostMapping("/responsableGeneral/gestionEquipement/ajout")
    public String ajoutEquipement(@ModelAttribute Equipement equipement,
                                  @RequestParam("imageFile") MultipartFile imageFile) {

        try {
            if (!imageFile.isEmpty()) {
                equipement.setImageData(imageFile.getBytes());

            }else {
                // Read the default image bytes
                InputStream defaultImageStream = getClass().getResourceAsStream("/static/img/unknown.png");
                if (defaultImageStream != null) {
                    byte[] defaultImageBytes = defaultImageStream.readAllBytes();
                    equipement.setImageData(defaultImageBytes);
                }

        } }catch (IOException e) {
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
    public String modificationEquipement(@PathVariable("id") Long id, Model model,HttpSession session) {

            gestionSession.prepareModel(session, model);
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
    public String GestionEntites(Model model,HttpSession session) {
        gestionSession.prepareModel(session, model);
        List<Entite> listeEntites = entiteRepo.findAll();
        model.addAttribute("listeEntites", listeEntites);
        return "RG_gestionEntites";}




    @GetMapping("/responsableGeneral/gestionEntites/ajout")
    public String afficherFormEntite(Model model,HttpSession session) {
        gestionSession.prepareModel(session, model);
        return "RG_gestionEntites_ajout";


    }




    @PostMapping("/responsableGeneral/gestionEntites/ajout")
    public String AjoutEntite(@ModelAttribute("entite") Entite entite) {
        entiteRepo.save(entite);
        return "redirect:/responsableGeneral/gestionEntites";


    }

    @GetMapping("/responsableGeneral/gestionEntites/suppression/{id}")
    public String supprimerEntiteGet(@PathVariable("id") Long id,Model model,HttpSession session) {
        gestionSession.prepareModel(session, model);
        entiteRepo.deleteById(id);
        return "redirect:/responsableGeneral/gestionEntites";
    }
    @GetMapping("/responsableGeneral/gestionEntites/modification/{id}")
    public String modificationEntite(@PathVariable("id") Long id,Model model,HttpSession session) {
        gestionSession.prepareModel(session, model);
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
    public String gestionUnite(Model model,HttpSession session) {
        gestionSession.prepareModel(session, model);
        List<Unite> listeUnites = uniteRepo.findAll();
        model.addAttribute("listeUnites", listeUnites);
        return "RG_gestionUnites";


    }

    //---------- AJOUTER UNITE---------------------------------------

    @GetMapping("/responsableGeneral/gestionUnites/ajout")
    public String AjoutUniteForm(Model model,HttpSession session) {
        gestionSession.prepareModel(session, model);
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
//------ Modifier une unité---------------------------------------
// Afficher le formulaire de modification
@GetMapping("/responsableGeneral/gestionUnites/modification/{id}")
public String modificationUnite(@PathVariable("id") Long id,Model model,HttpSession session) {
    gestionSession.prepareModel(session, model);
    List<Entite> listeEntites = entiteRepo.findAll();
    model.addAttribute("listeEntites", listeEntites);
    Optional<Unite> uniteOptional = uniteRepo.findById(id);
    if (uniteOptional.isPresent()) {
        model.addAttribute("unite", uniteOptional.get());
        return "RG_gestionUnites_modification"; // Assurez-vous que ce nom correspond à votre template
    } else {
        // Gérer le cas où l'unité n'est pas trouvée
        return "redirect:/responsableGeneral/gestionUnites";
    }
}

    // Enregistrer les modifications
    @PostMapping("/responsableGeneral/gestionUnites/modification/{id}")
    public String enregistrerModificationsUnite(@PathVariable("id") Long id, @ModelAttribute("unite") Unite unite) {
        unite.setIdUnite(id); // Assurez-vous que l'ID est défini pour la mise à jour
        uniteRepo.save(unite);
        return "redirect:/responsableGeneral/gestionUnites";
    }


    //******************** GESTION DES RESPONSABLE DES CONTROLEURS**********************
    @GetMapping("/responsableGeneral/gestionResponsableControleurs" )
    public String responsableControleurs(Model model,HttpSession session) {
        gestionSession.prepareModel(session, model);
        List<ResponsableControleur> listeResponsableControleurs = responsableControleurRepo.findAll();
        model.addAttribute("listeResponsableControleurs", listeResponsableControleurs );

        return "RG_gestionResponsableControleurs";
    }

    //------Ajout de responsable controleur----------------------
    @GetMapping("/responsableGeneral/gestionResponsableControleurs/ajout")
    public String ajoutResponsableControleursForm(Model model,HttpSession session) {
        gestionSession.prepareModel(session, model);
        model.addAttribute("responsableControleur", new ResponsableControleur());
        return "RG_gestionResponsableControleurs_ajout";
    }
    @PostMapping("/responsableGeneral/gestionResponsableControleurs/ajout")
    public String ajoutResponsableControleurs(@ModelAttribute ResponsableControleur responsableControleur,
                                              @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            if (!imageFile.isEmpty()) {
                responsableControleur.setImageData(imageFile.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
            //ho
        }
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
    @GetMapping("/responsableGeneral/gestionResponsableControleurs/modification/{id}")
    public String modificationResponsableControleur(@PathVariable("id") Long id,Model model,HttpSession session) {
        gestionSession.prepareModel(session, model);
        Optional<ResponsableControleur> responsablecontroleurOptional = responsableControleurRepo.findById(id);
        if (responsablecontroleurOptional.isPresent()) {
            model.addAttribute("responsableControleur", responsablecontroleurOptional.get());
            return "RG_gestionResponsableControleurs_modification"; // Nom de la vue Thymeleaf
        } else {
            // Gestion du cas où le contrôleur n'est pas trouvé
            return "redirect:/responsableGeneral/gestionResponsableControleurs";
        }
    }

    // Enregistrer les modifications
    @PostMapping("/responsableGeneral/gestionResponsableControleurs/modification/{id}")
    public String enregistrerModificationsResponsableControleur(@PathVariable("id") Long id,
                                                                @ModelAttribute ResponsableControleur responsableControleur,
                                                                @RequestParam("imageFile") MultipartFile imageFile,
                                                                RedirectAttributes redirectAttributes) {
        try {
            if (!imageFile.isEmpty()) {
                responsableControleur.setImageData(imageFile.getBytes());
            } else {
                // Si aucun fichier n'est téléchargé, récupérez l'image existante
                ResponsableControleur existingControleur = responsableControleurRepo.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Responsable controleur non trouvé avec l'id: " + id));
                responsableControleur.setImageData(existingControleur.getImageData());
            }
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Erreur lors du téléchargement de l'image");
            return "redirect:/responsableGeneral/gestionResponsableControleurs/modification/" + id;
        }

        responsableControleurRepo.save(responsableControleur); // Sauvegarder les modifications
        redirectAttributes.addFlashAttribute("message", "Modifications enregistrées avec succès");
        return "redirect:/responsableGeneral/gestionResponsableControleurs";
    }

    //*********************************Gestion Controleurs*************************************
    //------ Ajout D'un CONTROLEURS -----------------------------------------

    @GetMapping("/responsableGeneral/gestionControleurs/ajout")
    public String ajoutControleurForm(Model model,HttpSession session) {
        gestionSession.prepareModel(session, model);
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
    public String modificationControleur(@PathVariable("id") Long id,Model model,HttpSession session) {
        gestionSession.prepareModel(session, model);
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
            // Gérer l'ei
        }

        controleurRepo.save(controleur); // Sauvegarder les modifications

        redirectAttributes.addFlashAttribute("successMessage", "Les modifications ont été enregistrées avec succès.");
        return "redirect:/responsableGeneral/gestionControleurs"; // Rediriger vers la liste des contrôleurs après modification
    }
}
