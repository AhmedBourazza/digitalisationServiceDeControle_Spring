package org.system.digitalisationservicedecontrole.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.system.digitalisationservicedecontrole.entities.Equipement;

import java.util.Date;
import java.util.List;

public interface EquipementRepo extends JpaRepository<Equipement, Long> {
    long count();
    @Query("SELECT e.nom, COUNT(f) FROM Equipement e LEFT JOIN e.formulaires f " +
            "WHERE f.dateControle BETWEEN :startDate AND :endDate " +
            "GROUP BY e.idEquipement")
    List<Object[]> countControlsByEquipement(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


}
