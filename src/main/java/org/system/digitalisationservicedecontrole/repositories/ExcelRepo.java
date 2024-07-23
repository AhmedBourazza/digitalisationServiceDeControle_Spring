package org.system.digitalisationservicedecontrole.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.CrudRepository;
import org.system.digitalisationservicedecontrole.entities.Excel;

import java.util.List;
public interface ExcelRepo extends CrudRepository<Excel, Long> {

    @Query(value = "SELECT f.date_controle, e.nom, u.nom, s.nom, q.enonce, r.enonce , r.justification,c.username " +
            "FROM reponse r " +
            "JOIN question q ON r.id_question_ = q.id_question " +
            "JOIN section s ON q.id_section_ = s.id_section " +
            "JOIN formulaire f ON s.id_formulaire_ = f.id_formulaire " +
            "JOIN controleur c ON f.id_controleur_  = c.id_controleur  " +
            "JOIN unite u ON f.id_unite_ = u.id_unite " +
            "JOIN entite e ON u.id_entite_ = e.id_entite " +
            "WHERE f.matricule_exemplaire_equipement = :matricule " +
            "AND DATE_FORMAT(r.date_controle, '%Y-%m-%d %H:%i') = DATE_FORMAT(:dateControle, '%Y-%m-%d %H:%i') " +
            "ORDER BY s.id_section", nativeQuery = true)
    List<Object[]> findReponses(@Param("matricule") String matricule, @Param("dateControle") String dateControle);
}
