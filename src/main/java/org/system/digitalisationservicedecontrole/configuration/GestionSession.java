package org.system.digitalisationservicedecontrole.configuration;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Date;

@Component
public class GestionSession {


    public void prepareModel(HttpSession s, Model model){
        String nom = (String) s.getAttribute("nom");
        String prenom = (String) s.getAttribute("prenom");
        String userImageBase64 = (String) s.getAttribute("userImage");
        String email = (String) s.getAttribute("email");
        String grade = (String) s.getAttribute("grade");
        Date date_integration = (Date) s.getAttribute("date_integration");
        String matricule = (String) s.getAttribute("matricule");
        Date date_embauche = (Date) s.getAttribute("date_embauche");



        model.addAttribute("nomCompletMoi",nom+" "+prenom);
        model.addAttribute("nomMoi", nom);
        model.addAttribute("prenomMoi", prenom);
        model.addAttribute("imageMoi", userImageBase64);
        model.addAttribute("emailMoi", email);
        model.addAttribute("gradeMoi", grade);
        model.addAttribute("date_integrationMoi", date_integration);
        model.addAttribute("matriculeMoi", matricule);
        model.addAttribute("date_embaucheMoi", date_embauche);

    }

}
