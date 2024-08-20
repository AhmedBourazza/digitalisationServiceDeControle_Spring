package org.system.digitalisationservicedecontrole.DTOs;

public class UniteDTO {
    private Long idUnite;
    private String nom;

    // Constructeur
    public UniteDTO(Long idUnite, String nom) {
        this.idUnite = idUnite;
        this.nom = nom;
    }

    // Getters et setters
    public Long getIdUnite() {
        return idUnite;
    }

    public void setIdUnite(Long idUnite) {
        this.idUnite = idUnite;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
