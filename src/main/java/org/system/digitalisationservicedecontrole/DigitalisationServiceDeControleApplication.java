package org.system.digitalisationservicedecontrole;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class DigitalisationServiceDeControleApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalisationServiceDeControleApplication.class, args);
        // Crée une instance de BCryptPasswordEncoder
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Le mot de passe brut que vous souhaitez encoder
        String rawPassword = "123";

        // Encode le mot de passe
        String encodedPassword = encoder.encode(rawPassword);

        // Affiche le mot de passe encodé
        System.out.println("Encoded Password: " + encodedPassword);
    }


}
