package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.PizzaDTO;

import java.util.Collection;

public interface PizzaService {
    public PizzaDTO creaPizza(PizzaDTO nuovaPizza);

    public PizzaDTO eliminaPizza(Long idPizza);

    public PizzaDTO cercaPizza(Long idPizza);

    public Collection<PizzaDTO> tutteLePizze();

    public PizzaDTO modificaPizza(Long idPizza, PizzaDTO nuovaPizza);
}
