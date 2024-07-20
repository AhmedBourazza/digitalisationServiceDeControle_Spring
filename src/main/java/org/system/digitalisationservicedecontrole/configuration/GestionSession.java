package org.system.digitalisationservicedecontrole.configuration;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class GestionSession {


    public void prepareModel(HttpSession s, Model model){
        String nom = (String) s.getAttribute("nom");
        String prenom = (String) s.getAttribute("prenom");
        byte[] userImage = (byte[]) s.getAttribute("userImage");
        String email = (String) s.getAttribute("email");

        model.addAttribute("nomMoi", nom);
        model.addAttribute("prenomMoi", prenom);
        model.addAttribute("imageMoi", userImage);
        model.addAttribute("emailMoi", email);

    }

}
