package org.system.digitalisationservicedecontrole.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idQuestion;
    private String enonce ;
    private String sousSection ; //ce champs constitue la sous section qui englope plusieurs question

    @ManyToOne()
    @JoinColumn(name = "IdSection_")
    private Section section;

    @OneToMany(mappedBy = "question")
    private Collection<Reponse> reponses;

}
