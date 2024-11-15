package org.system.digitalisationservicedecontrole.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Base64;
import java.util.Collection;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity

public class Equipement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEquipement ;
    private String nom ;
    private String description ;
    private String frequence ;

    private String codification ;
    private Long quantite ;
    private String titreFormulaire ;
    private String formulaireUrl;
    private String documentationUrl;

    @Lob
    @Column(name = "image_data", columnDefinition = "LONGBLOB")
    private byte[] ImageData;

    @OneToMany(mappedBy = "equipement")
    private Collection<Formulaire> formulaires ;

    public String getImageDataAsBase64() {
        return Base64.getEncoder().encodeToString(getImageData());
    }


    public void setImageDataFromBase64(String base64String) {
        this.ImageData = Base64.getDecoder().decode(base64String);
    }

    @OneToMany(mappedBy = "equipement")
    private Collection<Section> sections;

}

