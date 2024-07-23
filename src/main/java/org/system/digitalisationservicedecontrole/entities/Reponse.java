package org.system.digitalisationservicedecontrole.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Entity

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Reponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReponse;
    private String enonce ;
    private String justification ;
    @ManyToOne()
    @JoinColumn(name = "IdQuestion_")
    private Question question;

}
