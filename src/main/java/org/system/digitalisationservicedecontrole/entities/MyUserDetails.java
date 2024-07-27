package org.system.digitalisationservicedecontrole.entities;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Base64;
import java.util.Collection;
import java.util.Date;

public class MyUserDetails implements UserDetails {
   @Getter
   private final Long id;
    @Getter
    private final String firstName;
    @Getter
    private final String lastName;
    @Getter
    private final byte[] imageData;
    @Getter
    private final String username;
    @Getter
    private final String email;
    @Getter
    private final String password;
    @Getter
    private final String grade;
    @Getter
    private final Date date_integration;
    @Getter
    private final Date date_embauche;
    @Getter
    private final String matricule;
 @Getter
 private final String num_tele;

    private final Collection<? extends GrantedAuthority> authorities;

    public MyUserDetails(String num_tele,Long id ,String matricule,Date date_embauche  ,  Date date_integration ,  String grade , String email ,String firstName, String lastName, byte[] imageData, String username, String password, Collection<? extends GrantedAuthority> authorities) {
      this.id=id ;
      this.num_tele= num_tele;
       this.date_integration = date_integration ;
       this.date_embauche = date_embauche ;
       this.matricule =matricule ;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.grade=grade ;
        this.imageData = imageData;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }


    public String getImageDataAsBase64() {
        return Base64.getEncoder().encodeToString(getImageData());
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }




    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
