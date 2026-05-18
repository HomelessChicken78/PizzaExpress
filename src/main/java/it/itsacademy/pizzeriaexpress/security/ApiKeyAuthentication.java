package it.itsacademy.pizzeriaexpress.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * <p>Rappresenta l'autenticazione basata su API key.
 * Estende AbstractAuthenticationToken, che è una classe di supporto di Spring Security
 * per implementare Authentication in modo più semplice.
 * Spring Security si basa su un modello ben definito per gestire l’autenticazione e l’autorizzazione delle richieste HTTP:
 * </p><p>
 * SecurityContext per ogni request:
 * Ogni richiesta che attraversa la catena di filtri di Spring Security è associata a un SecurityContext.
 * Questo contesto rappresenta lo stato di sicurezza della richiesta corrente.
 * All’interno del SecurityContext risiede un oggetto Authentication.
 * Questo oggetto descrive l’utente che effettua la richiesta, includendo informazioni identificative
 * (principal) e autorizzative (ruoli o authorities).</p><p>
 * Se non è presente un oggetto Authentication, Spring Security non può determinare se la richiesta sia autenticata.
 * Anche nel caso in cui, ad esempio, una chiave o un token sia valido, è necessario costruire un oggetto
 * Authentication e inserirlo nel SecurityContext per informare Spring Security che la richiesta proviene
 * da un utente autenticato.</p>
 */
public class ApiKeyAuthentication extends AbstractAuthenticationToken {

    // La chiave API del client, usata come "principal" (chi è l'utente)
    private final String apiKey;

    /**
     * Costruttore
     *
     * @param apiKey La chiave API ricevuta
     * @param authorities Lista di ruoli/authorities dell'utente (qui solitamente vuota)
     */
    public ApiKeyAuthentication(String apiKey, Collection<? extends GrantedAuthority> authorities) {
        // Chiamata al costruttore di AbstractAuthenticationToken
        // per inizializzare i ruoli/authorities
        super(authorities);

        this.apiKey = apiKey;

        // Imposta lo stato "autenticato" a true
        // perché stiamo creando l'oggetto solo se la chiave è valida
        setAuthenticated(true);
    }

    /**
     * Credenziali dell'utente.
     * Qui non abbiamo password, quindi ritorniamo null.
     */
    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * Ritorna chi è l'utente (principal).
     * In questo caso la chiave API stessa.
     */
    @Override
    public Object getPrincipal() {
        return apiKey;
    }
}