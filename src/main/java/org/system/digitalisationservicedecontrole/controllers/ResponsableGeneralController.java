package org.system.digitalisationservicedecontrole.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResponsableGeneralController {

    @GetMapping("/responsableGeneral/listeControleurs")
    public String Controleurs() {
        return "RG_listeControleurs"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
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
    public String GestionEquipements() {
        return "RG_gestionListeEquipements"; // Assurez-vous que "C_listeEquipements.html" est présent dans le dossier templates
    }
    @GetMapping("/responsableGeneral/gestionEquipements/modification")
    public String modificationEquipement() {
        return "RG_gestionEquipements_modification";

    }

    @GetMapping("/responsableGeneral/gestionEquipements/ajout")
    public String AjoutEquipement() {
        return "RG_gestionEquipements_ajout";

    }


    @GetMapping("/responsableGeneral/gestionEntites")
    public String GestionEntites() {
        return "RG_gestionEntites";

    @GetMapping("/responsableGeneral/gestionEntites/modification")
    public String modificationEntite() {
        return "RG_gestionEntites_modification";

    }
    @GetMapping("/responsableGeneral/gestionEntites/ajout")
    public String AjoutEntite() {
        return "RG_gestionEntites_ajout";


    }
}
