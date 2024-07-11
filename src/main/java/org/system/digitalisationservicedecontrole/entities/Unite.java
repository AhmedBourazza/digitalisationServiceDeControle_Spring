package org.system.digitalisationservicedecontrole.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity

public class Unite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUnite ;
    private String nom ;
    private String emplacement;
    private String Description ;

    @OneToMany(mappedBy = "unite")
    private Collection<Formulaire> formulaires ;

    @ManyToOne()
    private Entite entite ;

}
