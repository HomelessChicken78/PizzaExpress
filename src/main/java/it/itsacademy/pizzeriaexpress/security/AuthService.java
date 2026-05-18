package it.itsacademy.pizzeriaexpress.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * Questo service si occupa di autenticare le richieste HTTP basate su API key.
 * La logica principale è: leggere l'header X-API-KEY e, se valido, creare un oggetto
 * Authentication che rappresenta l'utente autenticato.
 */
@Service
public class AuthService {
    // Nome dell'header HTTP dove ci aspettiamo di trovare la chiave API
    private final String headerApiKey;

    // Chiave API valida iniettata da application.properties
    private final String requestedApiKey;

    // Nome dell'header HTTP dove ci aspettiamo di trovare la API secret
    private final String headerApiSecret;

    // API secret valida iniettata da application.properties
    private final String requestedApiSecret;

    // @Value serve a cercare un valore dall'application.properties
    public AuthService(@Value("${api.key.header}") String headerApiKey,
                       @Value("${api.key}") String requestedApiKey,
                       @Value("${api.secret}") String headerApiSecret,
                       @Value("${api.secret.header}") String requestedApiSecret) {
        this.requestedApiKey = requestedApiKey;
        this.headerApiKey = headerApiKey;
        this.headerApiSecret = headerApiSecret;
        this.requestedApiSecret = requestedApiSecret;
    }

    /**
     * Controlla la request HTTP e ritorna un oggetto Authentication se la chiave API è valida.
     *
     * @param request Oggetto HttpServletRequest che rappresenta la richiesta HTTP
     * @return Authentication oggetto rappresentante l'utente autenticato.
     * L'interfaccia Authentication rappresenta l'identità dell'utente all'interno di Spring Security.
     * Contiene:
     * - getPrincipal() -> chi è l'utente
     * - getAuthorities() -> ruoli dell'utente
     * - isAuthenticated() -> true/false
     * - altre info per Spring Security
     * @throws BadCredentialsException se la chiave API è mancante o non valida
     */
    public Authentication getAuthentication(HttpServletRequest request) {
        // HttpServletRequest request è un oggetto che rappresenta tutta la richiesta HTTP che arriva al server.
        // Contiene informazioni come:
        // - header HTTP (es. Authorization, X-API-KEY)
        // - parametri URL (?id=123)
        // - corpo della richiesta (JSON, form data, ecc.)
        // Qui ci serve per leggere l'header contenente la API key.-

        // request.getHeader(headerApiKey) legge il valore dell'header corrispondente a headerApiKey dalla richiesta.
        // Se l'header non esiste, ritorna null.
        String apiKey = request.getHeader(headerApiKey);

        // Controllo della validità della chiave
        if (apiKey == null || !apiKey.equals(requestedApiKey)) {
            // Se la chiave non c'è o non è corretta, lanciamo eccezione
            // Spring Security interpreterà questo come "utente non autenticato"
            throw new BadCredentialsException("Invalid API Key");
        }

        // request.getHeader(headerApiSecret) legge il valore dell'header corrispondente a headerApiSecret dalla richiesta.
        // Se l'header non esiste, ritorna null.
        String apiSecret = request.getHeader(headerApiSecret);

        // Controllo della validità del segreto
        if (requestedApiSecret == null || !requestedApiSecret.equals(apiSecret)) {
            // Se il segreto non c'è o non è corretto, lanciamo eccezione
            // Spring Security interpreterà questo come "utente non autenticato"
            throw new BadCredentialsException("Invalid API Secret");
        }

        // ApiKeyAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES):
        // Creiamo un oggetto Authentication che rappresenta l'utente autenticato.
        // ApiKeyAuthentication, nel nostro caso, estende Authentication.
        // Costruttore:
        //   - primo parametro: principal (chi è l'utente; qui usiamo la chiave API stessa)
        //   - secondo parametro: authorities (ruoli o permessi). Usiamo uthorityUtils.NO_AUTHORITIES:
        // questo è un comodo helper di Spring Security che crea una lista vuota di ruoli.
        // Significa che l'utente autenticato non ha ruoli/authorities assegnati.

        return null;
        //return new ApiKeyAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES);
    }
}