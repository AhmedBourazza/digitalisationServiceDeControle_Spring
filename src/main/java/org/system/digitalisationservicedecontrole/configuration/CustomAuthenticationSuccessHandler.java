package org.system.digitalisationservicedecontrole.configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.debug("Authentication successful for user: {}", authentication.getName());

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
