package it.itsacademy.pizzeriaexpress.controller;

import it.itsacademy.pizzeriaexpress.dto.*;
import it.itsacademy.pizzeriaexpress.exception.BadRequestException;
import it.itsacademy.pizzeriaexpress.service.OrdineService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class OrdineController {
    private static final String json = "application/json";

    @Autowired
    private OrdineService ordineService;

    @PostMapping(path = "/clienti/{idCliente}/ordini", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.CREATED)
    public OrdineDTO creaOrdine(@PathVariable Long idCliente, @RequestBody @Valid RegistraOrdineDTO nuovoOrdine) {
        return ordineService.creaOrdine(idCliente, nuovoOrdine);
    }

    @PostMapping(path = "/clienti/{idCliente}/ordini/prioritari", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.CREATED)
    public OrdineDTO creaOrdinePrioritario(@PathVariable Long idCliente, @RequestBody @Valid RegistraOrdinePrioritarioDTO nuovoOrdine) {
        return ordineService.creaOrdinePrioritario(idCliente, nuovoOrdine);
    }


    @GetMapping(path = "/clienti/{idCliente}/ordini/{codiceOrdine}", produces = json)
    public OrdineDTO cercaOrdine(@PathVariable Long idCliente, @PathVariable String codiceOrdine) {
        return ordineService.cercaOrdine(idCliente, codiceOrdine);
    }

    @PostMapping(path = "/clienti/{idCliente}/ordini/{codiceOrdine}/pizze", produces = json, consumes = json)
    @ResponseStatus(HttpStatus.CREATED)
    public OrdineDTO aggiungiPizza(@PathVariable Long idCliente, @PathVariable String codiceOrdine,
                                   @RequestBody @Valid AggiungiPizzaAllOrdineDTO pizzaAggiunta) {
        return ordineService.aggiungiPizza(idCliente, codiceOrdine, pizzaAggiunta.getIdPizza(), pizzaAggiunta.getQuantita());
    }

    @GetMapping(path = "/ordini", produces = json)
    // La wildcard del generics ?extends è necessaria per dire al compiler che la collection può contenere
    // sia OrdineDTO che le sue sottoclassi (OrdinePrioritarioDTO).
    // Jackson ritorna lo schema corretto in automatico
    public Collection<?extends OrdineDTO> cercaTuttiGliOrdini(String tipoOrdini) {
        if (tipoOrdini == null)
            return ordineService.tuttiGliOrdini();

        return switch (tipoOrdini) {
            case "normali" -> ordineService.tuttiGliOrdiniNonPrioritari();
            case "prioritari" -> ordineService.tuttiGliOrdiniPrioritari();
            default -> throw new BadRequestException("Valore non riconosciuto per tipoOrdine. " +
                    "Inserire \"prioritari\" per ordini prioritari, \"normali\" per ordini non prioritari " +
                    "oppure lasciare il campo vuoto per cercare tutti gli ordini");
        };
    }

    @PatchMapping(path = "/clienti/{idCliente}/ordini/{codiceOrdine}", produces = json, consumes = json)
    public OrdineDTO cambiaRider(@PathVariable Long idCliente, @PathVariable String codiceOrdine,
                                 @RequestBody IdRiderDTO idRider) {
        return ordineService.cambiaRider(idCliente, codiceOrdine, idRider.getIdRider());
    }

    // TODO cambiare un ordine in prioritario
}