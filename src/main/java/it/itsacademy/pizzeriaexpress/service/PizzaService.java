package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.PizzaDTO;

public interface PizzaService {
    public PizzaDTO creaPizza(PizzaDTO nuovaPizza);

    public PizzaDTO eliminaPizza(Integer idPizza);

    public PizzaDTO cercaPizza(Integer idPizza);

    public PizzaDTO modificaPizza(PizzaDTO nuovaPizza);
}
