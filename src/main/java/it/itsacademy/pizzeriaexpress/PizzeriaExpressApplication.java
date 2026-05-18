package it.itsacademy.pizzeriaexpress;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.*;

/*
 * Spring Security, se presente nel progetto, protegge automaticamente tutti gli endpoint HTTP dell'applicazione.
 * Senza configurazioni personalizzate:
 * - ogni richiesta richiede autenticazione;
 * - viene creato automaticamente un utente di default:
 *      username: user
 *      password: generata casualmente e mostrata in console;
 *
 * SecurityAutoConfiguration:
 * - abilita la configurazione automatica della sicurezza web
 *   (login, filtri di sicurezza, protezione endpoint, ecc.).
 *
 * UserDetailsServiceAutoConfiguration:
 * - crea automaticamente il servizio che gestisce l'utente di default
 *   ("user") se non ne definiamo uno personalizzato.
 *
 * Escludendo entrambe le configurazioni:
 * - l'applicazione NON richiede login;
 * - tutti gli endpoint REST sono accessibili liberamente;
 * - Spring Security non crea utenti automatici.
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class,
		UserDetailsServiceAutoConfiguration.class})
public class PizzeriaExpressApplication {
	public static void main(String[] args) {
		SpringApplication.run(PizzeriaExpressApplication.class, args);
	}
}
