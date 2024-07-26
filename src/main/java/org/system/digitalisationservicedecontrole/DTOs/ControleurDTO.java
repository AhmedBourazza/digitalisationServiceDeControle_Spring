package org.system.digitalisationservicedecontrole.DTOs;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Base64;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ControleurDTO {
    public Long idControleur ;
    private String nom ;
    private String prenom ;
    private String matricule ;
    private Date dateIntegration ;
    private Date dateEmbauche ;
    private Long formCount;

    private String grade ;
    private String numTele;
    private String username;
    private String email ;
    private String password;
    private byte[] ImageData;
    public String getImageDataAsBase64() {
        return Base64.getEncoder().encodeToString(getImageData());
    }


}
