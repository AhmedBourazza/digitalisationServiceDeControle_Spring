package org.system.digitalisationservicedecontrole.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.system.digitalisationservicedecontrole.entities.ResponsableGeneral;

import java.util.Optional;

public interface ResponsableGeneralRepo extends JpaRepository<ResponsableGeneral, Long> {
    Optional<ResponsableGeneral> findByUsername(String username);

}
