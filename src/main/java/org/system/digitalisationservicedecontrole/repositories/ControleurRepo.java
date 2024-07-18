package org.system.digitalisationservicedecontrole.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.system.digitalisationservicedecontrole.entities.Controleur;

import java.util.List;
import java.util.Optional;

public interface ControleurRepo extends JpaRepository<Controleur, Long> {
    Optional<Controleur> findByUsername(String username);

}
