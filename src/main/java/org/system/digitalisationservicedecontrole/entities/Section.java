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

public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSection ;
    private String nom ;
    @ManyToOne()
    @JoinColumn(name = "IdFormulaire_")
    private Formulaire formulaire ;


    @OneToMany(mappedBy = "section")
    private Collection<Question> questions;
}
