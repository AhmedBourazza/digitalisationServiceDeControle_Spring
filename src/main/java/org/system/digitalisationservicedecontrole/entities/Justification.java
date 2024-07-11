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
public class Justification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idJustification;
    private String enonce ;
    @ManyToOne()
    private Question question;

}
