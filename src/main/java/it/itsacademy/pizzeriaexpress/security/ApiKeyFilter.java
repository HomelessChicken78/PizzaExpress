package it.itsacademy.pizzeriaexpress.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/*
 Prima che la richiesta arrivi alla servlet e quindi alla controller, attraversa una serie di filtri, chiamata filter chain.
 All'interno ogni filtro prende la richiesta (che all'interno contiene l'header, il body etc) e un oggetto di tipo response,
 oltre che avere accesso a un SecurityContextHolder, con cui, attraverso il metodo .getContext(),
 accedere o modificare lo stato di sicurezza attuale (eg. autenticazione e autorizzazione).
 Grazie a questi oggetti è possibile controllare gli header della richiesta (richiesta.getHeaders())
 (a patto che la richiesta sia di tipo http) e:
    1. Lanciare un errore se non sono quelli che ci aspettiamo, bloccando l'intera catena
    di filtri prima ancora che raggiunga la controller
     2. Se va a buon, settare la Authentication (cioè dire a spring: questo utente è autenticato
    (e non ha autorizzazioni al momento)) e passare la palla al prossimo filtro (o alla controller finale).
 NOTA: questo filtro da solo NON fa nulla di magico! Deve essere registrato
 in una SecurityFilterChain di Spring Security per essere effettivamente invocato.
 */

/**
 *  Filtro che intercetta tutte le richieste HTTP per validare API key e API secret.
 *  Se le credenziali sono corrette, crea un ApiKeyAuthentication e la inserisce
 *  nel SecurityContext, rendendo la request "autenticata" per Spring Security.
 */
@Component
public class ApiKeyFilter extends GenericFilterBean {

    @Autowired
    private AuthService authService;

    /**
     * doFilter() è il metodo centrale del filtro. Un pò come test(T t) in Predicate.
     *
     * @param request  rappresenta la richiesta HTTP in arrivo. Di tipo generico (interfaccia) ServletRequest,
     *                 perché i filtri possono teoricamente lavorare con protocolli diversi da HTTP.
     * @param response rappresenta la risposta HTTP che il server restituirà.
     * @param chain    rappresenta la catena di filtri della Servlet/Spring Security.
     *                 Ogni filtro deve decidere se proseguire o bloccare la request.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // ServletRequest/ServletResponse sono generici, ma dobbiamo comunque usarli per fare l'override.
        // Noi sappiamo che la request è HTTP, quindi facciamo il cast a HttpServletRequest/HttpServletResponse
        // per poter usare metodi specifici come getHeader() e sendError().
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // If either header is missing, skip authentication processing
        // and pass control down to the next filter in the security chain
        // in this way the config will check if Authentication is required
        /*if (apiKey == null || apiSecret == null) {
            chain.doFilter(request, response);
            return;
        }*/

        try {
            // authService.getAuthentication() prende la request (che contiene l'header) legge gli header,
            // verifica se sono corretti e, se tutto è valido, restituisce un
            // oggetto Authentication (ApiKeyAuthentication).
            Authentication authentication = authService.getAuthentication(httpRequest);

            // SecurityContextHolder è il contenitore centrale di Spring Security
            // che tiene lo stato di autenticazione della request corrente.
            // Quando si chiama SecurityContextHolder.getContext(), si sta usando una sorta di fabbrica.
            // La chiamata recupera il SecurityContext associato al thread corrente. Se non esiste,
            // ne crea uno nuovo. Questo garantisce che ogni request abbia il proprio contesto separato
            // e che non ci siano interferenze tra thread diversi.
            SecurityContextHolder.getContext()

                    // setAuthentication(authentication) dice a Spring Security:
                    // "Questa request è autenticata, ecco chi è l'utente e quali ruoli ha"
                    // e gli passa un oggetto di tipo Authentication, che all'interno contiene
                    // l'utente (principal), le sue autorizzazioni (authorities), se è autenticato ecc.
                    .setAuthentication(authentication);

            // Ogni request passa attraverso una catena di filtri (FilterChain).
            // chain.doFilter() fa passare la request al prossimo filtro nella catena.
            // Alla fine, se tutto va bene, la request arriva al controller.
            chain.doFilter(request, response);
        } catch (BadCredentialsException e) {
            // Se la API key o il segreto non sono validi, interrompiamo la request
            // e restituiamo 401 Unauthorized al client.
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }
}