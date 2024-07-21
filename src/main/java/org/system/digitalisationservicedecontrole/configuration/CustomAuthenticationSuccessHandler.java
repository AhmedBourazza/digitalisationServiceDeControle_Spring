package org.system.digitalisationservicedecontrole.configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.system.digitalisationservicedecontrole.entities.MyUserDetails;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.debug("Authentication successful for user: {}", authentication.getName());

        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

        // Créer ou récupérer la session
        HttpSession session = request.getSession();

        // Ajouter les informations de l'utilisateur à la session
        session.setAttribute("nom", userDetails.getFirstName() );
        session.setAttribute("prenom", userDetails.getLastName() );
        session.setAttribute("userImage", userDetails.getImageDataAsBase64());
        session.setAttribute("email", userDetails.getEmail());
        session.setAttribute("grade", userDetails.getGrade());
        session.setAttribute("date_integration", userDetails.getDate_integration());
        session.setAttribute("matricule", userDetails.getMatricule());
        session.setAttribute("date_embauche", userDetails.getDate_embauche());

        boolean isResponsableGeneral = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_RESPONSABLE_GENERALE"));
        boolean isResponsableControleur = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_RESPONSABLE_CONTROLEURS"));
        boolean isControleur = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_CONTROLEUR"));

        String redirectUrl;
        if (isResponsableGeneral) {
            redirectUrl = "/responsableGeneral/dashboard";
        } else if (isResponsableControleur) {
            redirectUrl = "/responsableControleur/dashboard";
        } else if (isControleur) {
            redirectUrl = "/controleur/dashboard";
        } else {
            redirectUrl = "/index";
        }

        response.sendRedirect(redirectUrl);
    }
}
