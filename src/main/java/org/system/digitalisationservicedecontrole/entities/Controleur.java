package org.system.digitalisationservicedecontrole.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;


import java.util.Base64;
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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateIntegration ;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateEmbauche ;
    private String grade ;
    private String numTele;
    private String username;
    private String email ;
    private String password;
    @Lob
    @Column(name = "image_data", columnDefinition = "LONGBLOB")
    private byte[] ImageData;

    @ManyToOne()
    @JoinColumn(name = "IdResponsableControleur_")
    private ResponsableControleur responsableControleur ;

    @OneToMany(mappedBy = "controleur")
    private Collection<Formulaire> formulaires ;


    public String getImageDataAsBase64() {
        return Base64.getEncoder().encodeToString(getImageData());
    }





}
