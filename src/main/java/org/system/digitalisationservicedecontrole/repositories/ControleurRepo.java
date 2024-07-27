package org.system.digitalisationservicedecontrole.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.system.digitalisationservicedecontrole.DTOs.ControleurDTO;
import org.system.digitalisationservicedecontrole.entities.Controleur;
import org.system.digitalisationservicedecontrole.entities.Formulaire;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ControleurRepo extends JpaRepository<Controleur, Long> {
    Optional<Controleur> findByUsername(String username);
    @Query("SELECT c.idControleur, c.nom, c.prenom, c.matricule, c.dateIntegration, c.dateEmbauche, COUNT(f.idFormulaire), c.grade, c.numTele, c.username, c.email, c.password, c.ImageData " +
            "FROM Controleur c LEFT JOIN c.formulaires f " +
            "WHERE f.dateControle BETWEEN :startDate AND :endDate " +
            "GROUP BY c.idControleur, c.nom, c.prenom, c.matricule, c.dateIntegration, c.dateEmbauche, c.grade, c.numTele, c.username, c.email, c.password, c.ImageData " +
            "ORDER BY COUNT(f.idFormulaire) DESC")
    List<Object[]> findAllControleursWithCount(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    long count();

}
