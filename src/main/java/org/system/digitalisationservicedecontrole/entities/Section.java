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
    @JoinColumn(name = "IdEquipement_")
    private Equipement equipement ;


    @OneToMany(mappedBy = "section")
    private Collection<Question> questions;
}
