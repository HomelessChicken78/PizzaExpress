package it.itsacademy.pizzeriaexpress.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /**
     * <p>Definisce la catena di filtri di sicurezza per la nostra applicazione.</p>
     * <ul>
     * <li>Solo i percorsi relativi a clienti, ordini e riders richiedono API key.</li>
     * <li>Tutti gli altri endpoint restano pubblici.</li>
     * <li>Le richieste sono stateless: ogni request deve portare la chiave API.</li>
     * </ul>
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {

        // Disabilitiamo CSRF perché stiamo costruendo una API stateless.
        // CSRF (Cross-Site Request Forgery) è un attacco in cui un sito malevolo
        // può far eseguire azioni su un altro sito in cui l'utente è autenticato.
        // Esempio: sei loggato in Lorenzo.com e visiti Nicol.com; Nicol.com invia
        // una richiesta POST a Lorenzo.com usando i cookie di sessione del tuo browser.
        // Senza protezione, il sito di Lorenzo potrebbe eseguire questa richiesta come se fossi tu.
        // Spring Security protegge le applicazioni stateful generando un token CSRF
        // unico per ogni sessione, che deve essere inviato con ogni form POST.
        // Nel nostro caso:
        // - L'API è stateless (non usiamo sessioni né form HTML)
        // - L'autenticazione avviene tramite API key nell'header
        // -> il token CSRF non serve, quindi lo disabilitiamo.
        http.csrf(AbstractHttpConfigurer::disable);

        // Configuriamo quali richieste devono essere protette
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/clienti",      // il percorso principale
                        "/clienti/**",   // tutti i sotto-endpoint
                        "/ordini",       // percorso principale ordini
                        "/ordini/**",    // sotto-endpoint ordini
                        "/riders",       // percorso principale riders
                        "/riders/**"     // sotto-endpoint riders
                ).authenticated()  // richiedono autenticazione tramite API key
                .anyRequest().permitAll() // tutti gli altri endpoint restano pubblici
        );

        // Stato della sessione: STATELESS perché l'autenticazione avviene a ogni request tramite API key
        // invece di creare una sessione.
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // Aggiungiamo il filtro custom ApiKeyFilter
        // Così ogni request verso i percorsi protetti passa prima per la validazione della chiave API
        http.addFilterBefore(new ApiKeyFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}