package org.system.digitalisationservicedecontrole.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.system.digitalisationservicedecontrole.entities.Question;

public interface QuestionRepo extends JpaRepository<Question, Long> {
}
