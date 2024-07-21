package org.system.digitalisationservicedecontrole.controllers;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.system.digitalisationservicedecontrole.configuration.GestionSession;
import org.system.digitalisationservicedecontrole.entities.Equipement;
import org.system.digitalisationservicedecontrole.repositories.EquipementRepo;

import java.util.List;


@Controller
public class ContoleurController {
    @Autowired
    EquipementRepo equipementRepo ;
    @Autowired
    private GestionSession gestionSession;

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
    @GetMapping("/controleur/formulaire1")
    public String afficherFormulaire(Model model , HttpSession session) {
        gestionSession.prepareModel(session, model);
        return "C_formulaire1"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }


}
