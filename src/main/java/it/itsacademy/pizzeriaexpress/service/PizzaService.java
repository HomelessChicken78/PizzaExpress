package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.PizzaDTO;

public interface PizzaService {
    public PizzaDTO creaPizza(PizzaDTO nuovaPizza);

    public PizzaDTO eliminaPizza(Long idPizza);

    public PizzaDTO cercaPizza(Long idPizza);

    public PizzaDTO modificaPizza(Long idPizza, PizzaDTO nuovaPizza);
}
