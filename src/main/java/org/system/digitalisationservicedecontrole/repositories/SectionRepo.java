package org.system.digitalisationservicedecontrole.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.system.digitalisationservicedecontrole.entities.Equipement;
import org.system.digitalisationservicedecontrole.entities.Section;

import java.util.List;
@Repository
public interface SectionRepo extends JpaRepository<Section, Long> {
    List<Section> findByEquipement(Equipement equipement);
}
