package org.system.digitalisationservicedecontrole.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.system.digitalisationservicedecontrole.entities.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.debug("Configuring Security Filter Chain");

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    logger.debug("Configuring request matchers");
                    auth.requestMatchers("/index","/libraryImages/**", "/login", "/error", "/css/**", "/img/**").permitAll();
                    auth.requestMatchers("/responsableGeneral/**").hasRole("RESPONSABLE_GENERALE");
                    auth.requestMatchers("/responsableControleur/**").hasRole("RESPONSABLE_CONTROLEURS");
                    auth.requestMatchers("/controleur/**").hasRole("CONTROLEUR");
                    auth.anyRequest().authenticated();
                })
                .formLogin(form -> {
                    logger.debug("Configuring form login");
                    form.loginPage("/login")
                            .successHandler(customAuthenticationSuccessHandlerr()) // Utilisation du handler de succès d'authentification
                            .failureUrl("/login?error=true")
                            .permitAll();
                })
                .logout(logout -> {
                    logger.debug("Configuring logout");
                    logout.logoutUrl("/logout")
                            .logoutSuccessUrl("/login?logout=true")
                            .invalidateHttpSession(true)
                            .deleteCookies("JSESSIONID")
                            .permitAll();
                })
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.debug("Creating PasswordEncoder bean");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        logger.debug("Creating DaoAuthenticationProvider bean");
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandlerr() {
        logger.debug("Creating CustomAuthenticationSuccessHandler bean");
        return new CustomAuthenticationSuccessHandler();
    }
}
