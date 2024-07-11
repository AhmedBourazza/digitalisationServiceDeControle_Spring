package org.system.digitalisationservicedecontrole.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


import java.util.Collection;
import java.util.Date;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Controleur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idControleur ;
    private String nom ;
    private String prenom ;
    private String matricule ;
    private Date dateIntegration ;
    private Date dateEmbauche ;

    @ManyToOne()
    private ResponsableControleur responsableControleur ;

    @OneToMany(mappedBy = "controleur")
    private Collection<Formulaire> formulaires ;







}
