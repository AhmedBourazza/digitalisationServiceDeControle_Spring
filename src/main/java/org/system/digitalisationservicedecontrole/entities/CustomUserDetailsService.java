package org.system.digitalisationservicedecontrole.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.system.digitalisationservicedecontrole.entities.Controleur;
import org.system.digitalisationservicedecontrole.entities.ResponsableControleur;
import org.system.digitalisationservicedecontrole.entities.ResponsableGeneral;
import org.system.digitalisationservicedecontrole.repositories.ControleurRepo;
import org.system.digitalisationservicedecontrole.repositories.ResponsableControleurRepo;
import org.system.digitalisationservicedecontrole.repositories.ResponsableGeneralRepo;

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
            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles("RESPONSABLE_GENERALE")
                    .build();
        }

        Optional<ResponsableControleur> responsableControleurs = responsableControleursRepository.findByUsername(username);
        if (responsableControleurs.isPresent()) {
            logger.debug("User found in ResponsableControleur repository");
            ResponsableControleur user = responsableControleurs.get();
            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles("RESPONSABLE_CONTROLEURS")
                    .build();
        }

        Optional<Controleur> controleur = controleurRepository.findByUsername(username);
        if (controleur.isPresent()) {
            logger.debug("User found in Controleur repository");
            Controleur user = controleur.get();
            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles("CONTROLEUR")
                    .build();
        }

        logger.error("User not found with username: {}", username);
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
