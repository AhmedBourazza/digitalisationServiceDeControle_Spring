package org.system.digitalisationservicedecontrole.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.system.digitalisationservicedecontrole.entities.Controleur;
import org.system.digitalisationservicedecontrole.entities.Formulaire;
import org.system.digitalisationservicedecontrole.repositories.ControleurRepo;

import java.util.List;

@Service

public class RcService {
    @Autowired
    private ControleurRepo controleurRepo;
    @Transactional
    public void mettreAJourRCAvantSuppression(Long idControleur) {
        List<Controleur> controleurs = controleurRepo.findByResponsableControleur_IdResponsableControleur(idControleur);
        for (Controleur controleur : controleurs) {
            controleur.setResponsableControleur(null);
            controleurRepo.save(controleur);
        }
    }
}
