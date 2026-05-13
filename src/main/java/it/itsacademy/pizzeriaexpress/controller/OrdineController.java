package it.itsacademy.pizzeriaexpress.controller;

import it.itsacademy.pizzeriaexpress.dto.AggiungiPizzaAllOrdineDTO;
import it.itsacademy.pizzeriaexpress.dto.IdRiderDTO;
import it.itsacademy.pizzeriaexpress.dto.OrdineDTO;
import it.itsacademy.pizzeriaexpress.dto.RegistraOrdineDTO;
import it.itsacademy.pizzeriaexpress.service.OrdineService;
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
    public OrdineDTO creaOrdine(@PathVariable Long idCliente, @RequestBody RegistraOrdineDTO nuovoOrdine) {
        return ordineService.creaOrdine(idCliente, nuovoOrdine);
    }

    @GetMapping(path = "/clienti/{idCliente}/ordini/{codiceOrdine}", produces = json)
    public OrdineDTO cercaOrdine(@PathVariable Long idCliente, @PathVariable String codiceOrdine) {
        return ordineService.cercaOrdine(idCliente, codiceOrdine);
    }

    @PostMapping(path = "/clienti/{idCliente}/ordini/{codiceOrdine}/pizze", produces = json, consumes = json)
    @ResponseStatus(HttpStatus.CREATED)
    public OrdineDTO aggiungiPizza(@PathVariable Long idCliente, @PathVariable String codiceOrdine,
                                   @RequestBody AggiungiPizzaAllOrdineDTO pizzaAggiunta) {
        return ordineService.aggiungiPizza(idCliente, codiceOrdine, pizzaAggiunta.getIdPizza(), pizzaAggiunta.getQuantita());
    }

    @GetMapping(path = "/ordini", produces = json)
    public Collection<OrdineDTO> cercaTuttiGliOrdini() {
        return ordineService.tuttiGliOrdini();
    }

    @PatchMapping(path = "/clienti/{idCliente}/ordini/{codiceOrdine}", produces = json, consumes = json)
    public OrdineDTO cambiaRider(@PathVariable Long idCliente, @PathVariable String codiceOrdine,
                                 @RequestBody IdRiderDTO idRider) {
        return ordineService.cambiaRider(idCliente, codiceOrdine, idRider.getIdRider());
    }
}