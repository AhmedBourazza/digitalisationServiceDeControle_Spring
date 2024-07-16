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
@NoArgsConstructor  @AllArgsConstructor
@ToString
@Entity


public class ResponsableControleur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idResponsableControleur ;
    private String nom ;
    private String prenom ;
    private String matricule ;
    private Date dateEmbauche ;
    private Date dateIntegration ;
    private String grade ;
    private String numTele;
    private String username;
    private String email ;
    private String password;
    @Lob
    @Column(name = "image_data", columnDefinition = "LONGBLOB")
    private byte[] ImageData;
    @OneToMany(mappedBy="responsableControleur")
    private Collection<Controleur>  controleurs ;

   @ManyToOne()
   @JoinColumn(name = "IdResponsableGeneral_")
   private ResponsableGeneral responsableGeneral ;

    public String getImageDataAsBase64() {
        return Base64.getEncoder().encodeToString(getImageData());
    }


}
