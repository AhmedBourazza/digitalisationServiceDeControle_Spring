package org.system.digitalisationservicedecontrole.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.system.digitalisationservicedecontrole.entities.Equipement;
import org.system.digitalisationservicedecontrole.entities.Formulaire;

import java.util.List;

public interface FormulaireRepo extends JpaRepository<Formulaire, Long> {

    List<Formulaire> findFormulaireByEquipement(Equipement equipement);

}
