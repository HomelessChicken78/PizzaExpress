package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.PizzaDTO;

import java.util.Collection;

public interface PizzaService {
    PizzaDTO creaPizza(PizzaDTO nuovaPizza);

    PizzaDTO eliminaPizza(Long idPizza);

    PizzaDTO cercaPizza(Long idPizza);

    Collection<PizzaDTO> tutteLePizze();

    PizzaDTO modificaPizza(Long idPizza, PizzaDTO nuovaPizza);
}
