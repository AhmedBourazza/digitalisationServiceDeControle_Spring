package org.system.digitalisationservicedecontrole.controllers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.system.digitalisationservicedecontrole.DTOs.FormulaireDTO;
import org.system.digitalisationservicedecontrole.DTOs.ReponseDTO;
import org.system.digitalisationservicedecontrole.configuration.GestionSession;
import org.system.digitalisationservicedecontrole.entities.Controleur;
import org.system.digitalisationservicedecontrole.entities.Equipement;
import org.system.digitalisationservicedecontrole.entities.Formulaire;
import org.system.digitalisationservicedecontrole.entities.Unite;
import org.system.digitalisationservicedecontrole.repositories.*;
import org.system.digitalisationservicedecontrole.services.FormulaireService;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;


@Controller
public class ContoleurController {
    @Autowired
    EquipementRepo equipementRepo ;
    @Autowired
    private GestionSession gestionSession;
    @Autowired
    private FormulaireRepo formulaireRepo;
    @Autowired
    private FormulaireService formulaireService;
    @Autowired
    private UniteRepo uniteRepo;
    @Autowired
    private ControleurRepo controleurRepo ;
    @Autowired
    private ObjectMapper jacksonObjectMapper;
    @Autowired
    private EntiteRepo entiteRepo;

    @GetMapping("/controleur/login")
    public String login() {
        return "C_login"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }

    @GetMapping("/controleur/dashboard")
    public String dashboard(Model model, HttpSession session) {
        gestionSession.prepareModel(session, model);
        Long controleurId = (Long) session.getAttribute("id");

        // Définir les dates de début et de fin de l'année en cours
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        java.sql.Date startOfYear = new java.sql.Date(calendar.getTimeInMillis());
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
        java.sql.Date endOfYear = new java.sql.Date(calendar.getTimeInMillis());

        // Récupérer les contrôles par mois pour l'année en cours
        List<Object[]> controlsByMonth = controleurRepo.findNumOfControlsByMonth(controleurId, startOfYear, endOfYear);

        // Préparer les données pour le graphique
        List<String> months = Arrays.asList("Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre");
        Map<String, Long> controlsData = new LinkedHashMap<>();
        for (String month : months) {
            controlsData.put(month, 0L);
        }
        for (Object[] result : controlsByMonth) {
            Integer monthIndex = (Integer) result[0] - 1; // Les mois en Java commencent à 0
            Long count = (Long) result[1];
            controlsData.put(months.get(monthIndex), count);
        }

        // Ajouter les données JSON au modèle
        try {
            model.addAttribute("months", jacksonObjectMapper.writeValueAsString(new ArrayList<>(controlsData.keySet())));
            model.addAttribute("controlsData", jacksonObjectMapper.writeValueAsString(new ArrayList<>(controlsData.values())));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // Ajouter l'année actuelle au modèle
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        model.addAttribute("currentYear", currentYear);



        // Counts
        //Nombre de Controles lors du mois actuel
        calendar.setTime(new java.util.Date()); // Réinitialiser le calendrier à la date actuelle
        calendar.set(Calendar.DAY_OF_MONTH, 1); // Début du mois
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        java.sql.Date startOfMonth = new java.sql.Date(calendar.getTimeInMillis());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);// Fin du mois
        java.sql.Date endOfMonth = new java.sql.Date(calendar.getTimeInMillis());
        Long numCounts = formulaireRepo.countNumControls(controleurId, startOfMonth, endOfMonth);
        model.addAttribute("NumCounts", numCounts);


        //classement du controleur connecté
        List<Object[]> allControlsForMonth = formulaireRepo.findNumOfControlsForAllControleurs(startOfMonth, endOfMonth);
        int rank = 1;
        for (Object[] result : allControlsForMonth) {

            if (result[0].equals(controleurId)) {
                break;
            }
            rank++;
        }
        Long nombreControleurs = controleurRepo.count();

        model.addAttribute("rank", rank);
        model.addAttribute("nombreControleurs", nombreControleurs);

        List<Object[]> formsByEquipement = controleurRepo.countControlsByEquipementForControleurThisMonth(controleurId, startOfMonth, endOfMonth);
        List<String> equipementNames = new ArrayList<>();
        List<Long> formCounts = new ArrayList<>();
        for (Object[] result : formsByEquipement) {
            Equipement equipement = (Equipement) result[0];
            equipementNames.add(equipement.getNom());
            formCounts.add((Long) result[1]);
        }

        // Ajouter les données JSON au modèle
        try {
            model.addAttribute("equipementNames", jacksonObjectMapper.writeValueAsString(equipementNames));
            model.addAttribute("formCounts", jacksonObjectMapper.writeValueAsString(formCounts));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }



        // Formulaires par équipement pour le mois précédent
        calendar.setTime(new java.util.Date()); // Réinitialiser le calendrier à la date actuelle
        calendar.add(Calendar.MONTH, -1); // Reculer d'un mois
        calendar.set(Calendar.DAY_OF_MONTH, 1); // Début du mois précédent
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        java.sql.Date startOfPreviousMonth = new java.sql.Date(calendar.getTimeInMillis());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)); // Fin du mois précédent
        java.sql.Date endOfPreviousMonth = new java.sql.Date(calendar.getTimeInMillis());
        List<Object[]> formsByEquipementPrev = controleurRepo.countControlsByEquipementForControleurThisMonth(controleurId, startOfPreviousMonth, endOfPreviousMonth);
        List<String> equipementNamesPrev = new ArrayList<>();
        List<Long> formCountsPrev = new ArrayList<>();
        for (Object[] result : formsByEquipementPrev) {
            Equipement equipement = (Equipement) result[0];

            equipementNamesPrev.add(equipement.getNom());
            formCountsPrev.add((Long) result[1]);
        }
        try {
            model.addAttribute("equipementNamesPrev", jacksonObjectMapper.writeValueAsString(equipementNamesPrev));
            model.addAttribute("formCountsPrev", jacksonObjectMapper.writeValueAsString(formCountsPrev));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        List<Formulaire> mesFormulaires = formulaireRepo.findAllOrderByDateControleDescWhereIDC(controleurId);
        model.addAttribute("formulaires", mesFormulaires);

// Ajouter les noms des mois actuel et précédent
        String currentMonthName = months.get(Calendar.getInstance().get(Calendar.MONTH));
        model.addAttribute("monthYearCurrentMonth", currentMonthName);

        Calendar previousMonthCalendar = Calendar.getInstance();
        previousMonthCalendar.add(Calendar.MONTH, -1); // Reculer d'un mois
        String previousMonthName = months.get(previousMonthCalendar.get(Calendar.MONTH));
        model.addAttribute("monthYearLastMonth", previousMonthName);
        return "C_dashboard";
    }


    @GetMapping("/controleur/listeEquipements")
    public String afficherECOEquipements(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        List<Equipement> listeEquipements = equipementRepo.findAll();
        model.addAttribute("listeEquipements", listeEquipements );
        return "C_listeEquipements"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }


    @GetMapping("/controleur/monProfile")
    public String afficherProfile(Model model, HttpSession session) {
        // Récupérer l'ID du contrôleur à partir de la session
        Long controleurId = (Long) session.getAttribute("id");

        // Récupérer les informations du contrôleur à partir de la base de données
        Controleur controleur = controleurRepo.findById(controleurId).orElse(null);
        gestionSession.prepareModel(session, model);

        // Ajouter les informations du contrôleur au modèle
        model.addAttribute("controleur", controleur);

        // Récupérer tous les équipements
        List<Equipement> tousEquipements = equipementRepo.findAll();

        // Définir les dates de début et de fin pour le mois en cours
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        java.sql.Date startDate = new java.sql.Date(cal.getTimeInMillis()); // Utiliser java.sql.Date

        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        java.sql.Date endDate = new java.sql.Date(cal.getTimeInMillis()); // Utiliser java.sql.Date

        // Récupérer le nombre de contrôles par équipement pour le contrôleur ce mois-ci
        List<Object[]> controlsByEquipement = controleurRepo.countControlsByEquipementForControleurThisMonth(controleurId, startDate, endDate);

        // Transformer la liste des contrôles en une map pour une utilisation plus facile dans la vue
        Map<Long, Long> controlsMap = new HashMap<>();
        for (Object[] result : controlsByEquipement) {
            Long equipementId = ((Equipement) result[0]).getIdEquipement();
            Long count = (Long) result[1];
            controlsMap.put(equipementId, count);
        }

        // Ajouter les informations au modèle, y compris tous les équipements
        model.addAttribute("tousEquipements", tousEquipements);
        model.addAttribute("controlsMap", controlsMap);

        return "C_monProfile";
    }
    @GetMapping("/controleur/formulaireHydrant")
    public String afficherFormulaireHydrant(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        model.addAttribute("entites", entiteRepo.findAll());
        model.addAttribute("unites", uniteRepo.findAll());

        return "C_formHydrant"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }

    @GetMapping("/controleur/formulaireMonitors")
    public String afficherFormulaireMonitors(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        model.addAttribute("entites", entiteRepo.findAll());
        model.addAttribute("unites", uniteRepo.findAll());
        return "C_formMonitors";
    }
    @GetMapping("/controleur/formulairePompeIncendie")
    public String afficherFormulairePempeIncendie(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        model.addAttribute("entites", entiteRepo.findAll());
        model.addAttribute("unites", uniteRepo.findAll());
        return "C_formPompeIncendie";
    }
    @GetMapping("/controleur/formulaire1")
    public String afficherFormulaire(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        model.addAttribute("entites", entiteRepo.findAll());
        model.addAttribute("unites", uniteRepo.findAll());
        return "C_formulaire1"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }
    @GetMapping("/controleur/formulaireSprinklers")
    public String afficherFormulaireSprinklers(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        model.addAttribute("entites", entiteRepo.findAll());
        model.addAttribute("unites", uniteRepo.findAll());
        return "C_formSprinklers"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }
    @GetMapping("/controleur/formulaireRIA_PIA")
    public String afficherFormulaireRIA_PIA(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        model.addAttribute("entites", entiteRepo.findAll());
        model.addAttribute("unites", uniteRepo.findAll());
        return "C_formRIA_PIA"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }

    @GetMapping("/controleur/formulaireDeluge")
    public String afficherFormulaireDeluge(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        model.addAttribute("entites", entiteRepo.findAll());
        model.addAttribute("unites", uniteRepo.findAll());
        return "C_formDeluge"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }





    @PostMapping("/controleur/remplirFormulaire/ajout")

    public String submitFormulaire(@ModelAttribute FormulaireDTO formulaireDTO) {
        // Récupération des entités depuis la base de données
        Equipement equipement = equipementRepo.findById(formulaireDTO.getIdEquipement())
                .orElseThrow(() -> new EntityNotFoundException("Equipement not found"));
        Unite unite = uniteRepo.findById(formulaireDTO.getIdUnite())
                .orElseThrow(() -> new EntityNotFoundException("Unite not found"));
        Controleur controleur = controleurRepo.findById(formulaireDTO.getIdControleur())
                .orElseThrow(() -> new EntityNotFoundException("Controleur not found"));

        // Création et configuration de l'objet Formulaire
        Formulaire formulaire = new Formulaire();
        formulaire.setEquipement(equipement);
        formulaire.setUnite(unite);
        formulaire.setSecteur(formulaireDTO.getSecteur());
        formulaire.setProprietaire(formulaireDTO.getProprietaire());
        formulaire.setDateControle(Date.valueOf(formulaireDTO.getDateControle()));
        formulaire.setMatriculeExemplaireEquipement(formulaireDTO.getMatriculeExemplaireEquipement());
        formulaire.setControleur(controleur);
        formulaire.setNomCompletControleur(formulaireDTO.getNomCompletControleur());

        // Enregistrement du formulaire et des réponses
        formulaireService.saveFormulaireAndReponses(formulaire, formulaireDTO.getReponses());

        return "redirect:/controleur/listeEquipements";
    }

}
