package org.system.digitalisationservicedecontrole.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.system.digitalisationservicedecontrole.entities.Entite;

public interface EntiteRepo extends JpaRepository<Entite, Long> {
}
