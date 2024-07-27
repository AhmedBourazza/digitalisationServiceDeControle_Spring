package org.system.digitalisationservicedecontrole.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.system.digitalisationservicedecontrole.DTOs.ReponseDTO;
import org.system.digitalisationservicedecontrole.entities.Formulaire;
import org.system.digitalisationservicedecontrole.entities.Question;
import org.system.digitalisationservicedecontrole.entities.Reponse;
import org.system.digitalisationservicedecontrole.entities.Section;
import org.system.digitalisationservicedecontrole.repositories.FormulaireRepo;
import org.system.digitalisationservicedecontrole.repositories.QuestionRepo;
import org.system.digitalisationservicedecontrole.repositories.ReponseRepo;
import org.system.digitalisationservicedecontrole.repositories.SectionRepo;

import java.util.List;

@Service
public class FormulaireService {

    @Autowired
    private FormulaireRepo formulaireRepository;

    @Autowired
    private SectionRepo sectionRepository;

    @Autowired
    private QuestionRepo questionRepository;

    @Autowired
    private ReponseRepo reponseRepository;

    public void saveFormulaireAndReponses(Formulaire formulaire, List<ReponseDTO> reponses) {
        // Save the formulaire first
        Formulaire savedFormulaire = formulaireRepository.save(formulaire);

        // Get sections related to the equipment
        List<Section> sections = sectionRepository.findByEquipement(formulaire.getEquipement());

        for (ReponseDTO reponseDTO : reponses) {
            // Find the corresponding question by matching the text
            for (Section section : sections) {
                List<Question> questions = questionRepository.findBySection(section);
                for (Question question : questions) {
                    if (question.getEnonce().equals(reponseDTO.getQuestionText())) {
                        // Create and save the Reponse entity
                        Reponse reponse = new Reponse();
                        reponse.setEnonce(reponseDTO.getReponseText());
                        reponse.setJustification(reponseDTO.getJustification());
                        reponse.setQuestion(question);
                        reponse.setDateControle(savedFormulaire.getDateControle());
                        reponse.setFormulaire(savedFormulaire);
                        reponseRepository.save(reponse);
                    }
                }
            }
        }
    }
}
