package org.system.digitalisationservicedecontrole.entities;


import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Table(name = "Excel") // Remplacez "votre_table" par le nom de votre table
@Getter
@Setter
public class Excel {
    @Id
    private Long id; // Assurez-vous d'avoir un identifiant pour l'entité

    private String dateControle;
    private String entite;
    private String unite;
    private String section;
    private String question;
    private String reponse;
}
