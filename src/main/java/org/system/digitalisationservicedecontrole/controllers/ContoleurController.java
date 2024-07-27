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
import java.util.List;


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
    public String afficherProfile(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        return "C_monProfile"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
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
