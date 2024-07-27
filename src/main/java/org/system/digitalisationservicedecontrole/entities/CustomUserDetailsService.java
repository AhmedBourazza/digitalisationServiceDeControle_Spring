package org.system.digitalisationservicedecontrole.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.system.digitalisationservicedecontrole.repositories.ControleurRepo;
import org.system.digitalisationservicedecontrole.repositories.ResponsableControleurRepo;
import org.system.digitalisationservicedecontrole.repositories.ResponsableGeneralRepo;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private ResponsableGeneralRepo responsableGeneraleRepository;

    @Autowired
    private ResponsableControleurRepo responsableControleursRepository;

    @Autowired
    private ControleurRepo controleurRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Attempting to load user by username: {}", username);

        Optional<ResponsableGeneral> responsableGenerale = responsableGeneraleRepository.findByUsername(username);
        if (responsableGenerale.isPresent()) {
            logger.debug("User found in ResponsableGeneral repository");
            ResponsableGeneral user = responsableGenerale.get();
            System.out.println(user.getNom());
            return new MyUserDetails(user.getNumTele(),user.getIdResponsableGeneral(),user.getMatricule(),user.getDateEmbauche() , user.getDateIntegration(),user.getGrade(),user.getEmail()  , user.getNom(), user.getPrenom(), user.getImageData(), user.getUsername(), user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("ROLE_RESPONSABLE_GENERALE")));
        }

        Optional<ResponsableControleur> responsableControleurs = responsableControleursRepository.findByUsername(username);
        if (responsableControleurs.isPresent()) {
            logger.debug("User found in ResponsableControleur repository");
            ResponsableControleur user = responsableControleurs.get();
            System.out.println(user.getNom());
            return new MyUserDetails(user.getNumTele(),user.getIdResponsableControleur(),user.getMatricule(),user.getDateEmbauche() ,user.getDateIntegration() , user.getGrade(),user.getEmail()  , user.getNom(), user.getPrenom(), user.getImageData(), user.getUsername(), user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("ROLE_RESPONSABLE_CONTROLEURS")));
        }

        Optional<Controleur> controleur = controleurRepository.findByUsername(username);
        if (controleur.isPresent()) {
            logger.debug("User found in Controleur repository");
            Controleur user = controleur.get();
            System.out.println(user.getNom());
            return new MyUserDetails(user.getNumTele(),user.getIdControleur(),user.getMatricule(), user.getDateEmbauche() , user.getDateIntegration(),user.getGrade(), user.getEmail()  , user.getNom(), user.getPrenom(), user.getImageData() , user.getUsername(), user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("ROLE_CONTROLEUR")));
        }

        logger.error("User not found with username: {}", username);
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
