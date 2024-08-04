package org.system.digitalisationservicedecontrole.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.system.digitalisationservicedecontrole.entities.Equipement;
import org.system.digitalisationservicedecontrole.entities.Formulaire;

import java.util.Date;
import java.util.List;

public interface FormulaireRepo extends JpaRepository<Formulaire, Long> {

    List<Formulaire> findFormulaireByEquipement(Equipement equipement);
    @Query("SELECT f FROM Formulaire f ORDER BY f.dateControle DESC limit 5")
    List<Formulaire> findAllOrderByDateControleDesc();


    @Query("SELECT COUNT(f) FROM Controleur c, Formulaire f " +
            "WHERE f.controleur.idControleur = c.idControleur " +
            "AND c.idControleur = :myId " +
            "AND f.dateControle BETWEEN :startDate AND :endDate")
    Long countNumControls(@Param("myId") Long id, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT c.idControleur, COUNT(f.idFormulaire) " +
            "FROM Controleur c LEFT JOIN c.formulaires f " +
            "WHERE f.dateControle BETWEEN :startDate AND :endDate " +
            "GROUP BY c.idControleur " +
            "ORDER BY COUNT(f.idFormulaire) DESC")
    List<Object[]> findNumOfControlsForAllControleurs(@Param("startDate") Date startDate, @Param("endDate") Date endDate);



    long count();

}
