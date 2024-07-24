package org.system.digitalisationservicedecontrole.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Entity

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Reponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReponse;
    private Date dateControle ;
    private String enonce ;
    private String justification ;
    private String matriculeExemplaireEquipement ;
    private String Proprietaire ;
    @ManyToOne()
    @JoinColumn(name = "IdQuestion_")
    private Question question;
    @ManyToOne()
    @JoinColumn(name = "IdControleur_")
    private Controleur controleur ;

}
