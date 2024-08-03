package org.system.digitalisationservicedecontrole.controllers;
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
import org.system.digitalisationservicedecontrole.repositories.ControleurRepo;
import org.system.digitalisationservicedecontrole.repositories.EquipementRepo;
import org.system.digitalisationservicedecontrole.repositories.FormulaireRepo;
import org.system.digitalisationservicedecontrole.repositories.UniteRepo;
import org.system.digitalisationservicedecontrole.services.FormulaireService;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    @GetMapping("/controleur/login")
    public String login() {
        return "C_login"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }

    @GetMapping("/controleur/dashboard")
    public String dashboard(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        return "C_dashboard"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
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
        java.sql.Date startDate = new java.sql.Date(cal.getTimeInMillis()); // Utiliser java.sql.Date

        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
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
        return "C_formHydrant"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }

    @GetMapping("/controleur/formulaireMonitors")
    public String afficherFormulaireMonitors(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        return "C_formMonitors";
    }
    @GetMapping("/controleur/formulairePompeIncendie")
    public String afficherFormulairePempeIncendie(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        return "C_formPompeIncendie";
    }
    @GetMapping("/controleur/formulaire1")
    public String afficherFormulaire(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        return "C_formulaire1"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }
    @GetMapping("/controleur/formulaireSprinklers")
    public String afficherFormulaireSprinklers(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        return "C_formSprinklers"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }
    @GetMapping("/controleur/formulaireRIA_PIA")
    public String afficherFormulaireRIA_PIA(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        return "C_formRIA_PIA"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }

    @GetMapping("/tst2")
    public String tst2() {
        return "tst2"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
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

        // Enregistrement du formulaire et des réponses
        formulaireService.saveFormulaireAndReponses(formulaire, formulaireDTO.getReponses());

        return "redirect:/controleur/listeEquipements";
    }

}
