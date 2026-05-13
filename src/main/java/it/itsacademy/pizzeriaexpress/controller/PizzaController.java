package it.itsacademy.pizzeriaexpress.controller;

import it.itsacademy.pizzeriaexpress.dto.PizzaDTO;
import it.itsacademy.pizzeriaexpress.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path = "/pizze")
public class PizzaController {
    private static final String json = "application/json";

    @Autowired
    private PizzaService pizzaService;

    @PostMapping(consumes = json, produces = json)
    @ResponseStatus(HttpStatus.CREATED)
    public PizzaDTO creaPizza(@RequestBody PizzaDTO dtoNuovaPizza) {
        return pizzaService.creaPizza(dtoNuovaPizza);
    }

    @DeleteMapping(path = "/{idPizza}", produces = json)
    public PizzaDTO eliminaPizza(@PathVariable Long idPizza) {
        return pizzaService.eliminaPizza(idPizza);
    }

    @GetMapping(path = "/{idPizza}", produces = json)
    public PizzaDTO cercaPizza(@PathVariable Long idPizza) {
        return pizzaService.cercaPizza(idPizza);
    }

    @GetMapping(produces = json)
    public Collection<PizzaDTO> cercaTutteLePizze() {
        return pizzaService.tutteLePizze();
    }

    @PutMapping(path = "/{idPizza}", produces = json, consumes = json)
    public PizzaDTO modificaPizza(@PathVariable Long idPizza, @RequestBody PizzaDTO pizzaModificataDTO) {
        return pizzaService.modificaPizza(idPizza, pizzaModificataDTO);
    }
}
