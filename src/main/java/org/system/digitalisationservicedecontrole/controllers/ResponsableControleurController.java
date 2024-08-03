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
    @Autowired
    private GestionSession gestionSession;
    @Autowired
    private FormulaireRepo formulaireRepo;
    @Autowired
    private ObjectMapper jacksonObjectMapper;
    @Autowired
    private ResponsableControleurRepo responsableControleurRepo;


    @GetMapping("/responsableControleur/login")
    public String login() {
        return "RC_login";
    }

    @GetMapping("/responsableControleur/gestionControleurs")
    public String Controleurs(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        List<Controleur> listeControleurs = controleurRepo.findAll();
        model.addAttribute("listeControleurs", listeControleurs);
        return "RC_gestionControleurs";
    }
    @GetMapping("/responsableControleur/dashboard")
    public String dashboard(Model model, HttpSession session) {
        // Préparer le modèle avec les données de la session
        gestionSession.prepareModel(session, model);

        // Récupérer tous les formulaires, triés par date de contrôle décroissante
        List<Formulaire> formulaires = formulaireRepo.findAllOrderByDateControleDesc();

        // Définir le début du mois en cours
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startOfCurrentMonth = calendar.getTime(); // Premier jour du mois en cours à minuit

        // Définir la fin du mois en cours
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date endOfCurrentMonth = calendar.getTime(); // Dernier jour du mois en cours à 23:59:59

        // Obtenir le format du mois et de l'année pour le mois en cours
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM yyyy", Locale.FRENCH);
        String monthYearCurrentMonth = monthFormat.format(calendar.getTime());

        // Récupérer les contrôleurs avec leurs comptes de contrôles pour le mois en cours
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

        // Récupérer les noms et comptes de contrôles des contrôleurs pour les logs
        List<String> nomsControleurs = controleursWithControlesCurrentMonth.stream()
                .map(c -> c.getNom() + " " + c.getPrenom())
                .collect(Collectors.toList());
        List<Long> compteControles = controleursWithControlesCurrentMonth.stream()
                .map(ControleurDTO::getFormCount)
                .collect(Collectors.toList());

        // Ajouter les données JSON au modèle
        try {
            model.addAttribute("nomsControleurs", jacksonObjectMapper.writeValueAsString(nomsControleurs));
            model.addAttribute("compteControles", jacksonObjectMapper.writeValueAsString(compteControles));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // Récupérer les données d'équipement pour le mois en cours
        List<Object[]> equipmentResultsCurrentMonth = equipementRepo.countControlsByEquipement(startOfCurrentMonth, endOfCurrentMonth);

        List<String> equipmentNamesCurrentMonth = new ArrayList<>();
        List<Long> equipmentCountsCurrentMonth = new ArrayList<>();

        for (Object[] result : equipmentResultsCurrentMonth) {
            if (result.length >= 2) {
                equipmentNamesCurrentMonth.add((String) result[0]); // Nom de l'équipement
                equipmentCountsCurrentMonth.add((Long) result[1]); // Compte des contrôles
            }
        }

        // Ajouter les données d'équipement au modèle pour le mois en cours
        model.addAttribute("equipmentNamesCurrentMonth", equipmentNamesCurrentMonth);
        model.addAttribute("equipmentCountsCurrentMonth", equipmentCountsCurrentMonth);

        // Ajouter le mois et l'année du mois en cours au modèle
        model.addAttribute("monthYearCurrentMonth", monthYearCurrentMonth);

        // Définir le début et la fin du mois précédent
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startOfPreviousMonth = calendar.getTime(); // Premier jour du mois précédent à minuit

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date endOfPreviousMonth = calendar.getTime(); // Dernier jour du mois précédent à 23:59:59

        // Récupérer les données d'équipement pour le mois précédent
        List<Object[]> equipmentResultsPreviousMonth = equipementRepo.countControlsByEquipement(startOfPreviousMonth, endOfPreviousMonth);

        List<String> equipmentNamesPreviousMonth = new ArrayList<>();
        List<Long> equipmentCountsPreviousMonth = new ArrayList<>();

        for (Object[] result : equipmentResultsPreviousMonth) {
            if (result.length >= 2) {
                equipmentNamesPreviousMonth.add((String) result[0]); // Nom de l'équipement
                equipmentCountsPreviousMonth.add((Long) result[1]); // Compte des contrôles
            }
        }

        // Ajouter les données d'équipement au modèle pour le mois précédent
        model.addAttribute("equipmentNamesLastMonth", equipmentNamesPreviousMonth);
        model.addAttribute("equipmentCountsLastMonth", equipmentCountsPreviousMonth);

        // Obtenir le format du mois et de l'année pour le mois précédent
        String monthYearPreviousMonth = monthFormat.format(calendar.getTime());
        model.addAttribute("monthYearLastMonth", monthYearPreviousMonth);

        // Ajouter d'autres données au modèle
        model.addAttribute("formulaires", formulaires);
        model.addAttribute("controleurs", topControleurs);
        model.addAttribute("totalEquipements", equipementRepo.count());
        model.addAttribute("totalEntites", entiteRepo.count());
        model.addAttribute("totalUnites", uniteRepo.count());
        model.addAttribute("totalControleurs", controleurRepo.count());
        model.addAttribute("totalFormulaires", formulaireRepo.count());

        return "RC_dashboard";
    }



    @GetMapping("/responsableControleur/editProfile")
    public String EditProfile(Model model, HttpSession session) {
        // Préparer le modèle pour la session
        gestionSession.prepareModel(session, model);


        return "RC_editProfile"; // Retourne la vue d'édition du profil
    }

    @GetMapping("/responsableControleur/monProfile")
    public String MonProfile(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        return "RC_monProfile";
    }

//-----------------------
@GetMapping("/responsableControleur/gestionEquipement/ajout")
public String afficherEquipementForm(Model model , HttpSession session) {
    gestionSession.prepareModel(session, model);
    model.addAttribute("equipement", new Equipement());
    return "RC_gestionEquipements_ajout";
}

    @PostMapping("/responsableControleur/gestionEquipement/ajout")
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
    public String modificationEquipement(@PathVariable("id") Long id,Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        Optional<Equipement> equipementOptional = equipementRepo.findById(id);
        if (equipementOptional.isPresent()) {
            model.addAttribute("equipement", equipementOptional.get());
            return "RC_gestionEquipements_modification";
        } else {
            return "redirect:/responsableControleur/gestionEquipements";
        }
    }

    @PostMapping("/responsableControleur/gestionEquipement/modification/{id}")
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
        return "redirect:/responsableControleur/gestionEquipements";
    }


    //------------------------


    @GetMapping("/responsableControleur/archiveFormulaire/parEquipements")
    public String RC_archiveFormulaire_ParEquipements(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        List<Equipement> listeEquipements = equipementRepo.findAll();
        model.addAttribute("listeEquipements", listeEquipements );
        return "RC_archiveFormulaire_ParEquipements";
    }
    @GetMapping("/responsableControleur/archiveFormulaire/parEquipements/voirArchive/{id}")
    public String RG_archiveFormulaire_ParEquipements_voirArchive(@PathVariable("id") Long id, Model model, HttpSession session ) {
        Equipement equipement = equipementRepo.findById(id).get();
        gestionSession.prepareModel(session, model);

        List<Formulaire> formulairesByEquipement = formulaireRepo.findFormulaireByEquipement(equipement);
        model.addAttribute("formulairesByEquipement", formulairesByEquipement );
        return "RC_archiveFormulaire_ParEquipements_voirArchive";
    }



    @GetMapping("/responsableControleur/gestionEquipements")
    public String GestionEquipements(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        List<Equipement> listeEquipements = equipementRepo.findAll();
        model.addAttribute("listeEquipements", listeEquipements );
        return "RC_gestionListeEquipements"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }


    @GetMapping("/responsableControleur/gestionEntites")
    public String gestionEntites(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        List<Entite> listeEntites = entiteRepo.findAll();

        model.addAttribute("listeEntites", listeEntites);
        return "RC_gestionEntites";}

    @GetMapping("/responsableControleur/gestionEntites/ajout")
    public String afficherFormEntite(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        return "RC_gestionEntites_ajout";


    }

    @PostMapping("/responsableControleur/gestionEntites/ajout")
    public String AjoutEntite(@ModelAttribute("entite") Entite entite) {
        entiteRepo.save(entite);
        return "redirect:/responsableControleur/gestionEntites";


    }
    @GetMapping("/responsableControleur/gestionEntites/suppression/{id}")
    public String supprimerEntiteGet(@PathVariable("id") Long id,Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        entiteRepo.deleteById(id);
        return "redirect:/responsableControleur/gestionEntites";
    }
    @GetMapping("/responsableControleur/gestionEntites/modification/{id}")
    public String modificationEntite(@PathVariable("id") Long id,Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
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
    public String gestionUnite(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        List<Unite> listeUnites = uniteRepo.findAll();

        model.addAttribute("listeUnites", listeUnites);
        return "RC_gestionUnites";


    }

//---------- AJOUTER UNITE---------------------------------------

    @GetMapping("/responsableControleur/gestionUnites/ajout")
    public String AjoutUniteForm(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
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
    public String modificationUnite(@PathVariable("id") Long id, Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
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
    public String modificationControleur(@PathVariable("id") Long id,Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
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
    public String ajoutControleurForm(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
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
