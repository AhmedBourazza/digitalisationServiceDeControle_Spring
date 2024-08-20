package org.system.digitalisationservicedecontrole.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.system.digitalisationservicedecontrole.entities.Unite;

import java.util.List;

public interface UniteRepo extends JpaRepository<Unite, Long> {
    long count();
    List<Unite> findByEntite_IdEntite(Long Identite);

    @Query("SELECT u.idUnite AS idUnite, u.nom AS nom FROM Unite u WHERE u.entite.idEntite = ?1")
    List<Object[]> findUnitesByEntiteId(Long idEntite);
}
