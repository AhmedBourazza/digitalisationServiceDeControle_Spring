package org.system.digitalisationservicedecontrole.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.system.digitalisationservicedecontrole.entities.ResponsableControleur;

import java.util.Optional;

public interface ResponsableControleurRepo extends JpaRepository<ResponsableControleur, Long> {
    Optional<ResponsableControleur> findByUsername(String username);
    long count();
}
