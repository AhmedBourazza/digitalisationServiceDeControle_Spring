package org.system.digitalisationservicedecontrole.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.system.digitalisationservicedecontrole.entities.Question;
import org.system.digitalisationservicedecontrole.entities.Section;

import java.util.List;

public interface QuestionRepo extends JpaRepository<Question, Long> {
    List<Question> findBySection(Section section);
}
