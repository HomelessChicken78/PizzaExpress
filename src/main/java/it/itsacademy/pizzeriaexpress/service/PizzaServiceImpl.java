package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.PizzaDTO;
import it.itsacademy.pizzeriaexpress.repository.PizzaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PizzaServiceImpl implements PizzaService {
    @Autowired
    PizzaRepository repositoryPizza;

    @Override
    public PizzaDTO creaPizza(PizzaDTO nuovaPizza) {
        return null;
    }

    @Override
    public PizzaDTO eliminaPizza(Integer idPizza) {
        return null;
    }

    @Override
    public PizzaDTO cercaPizza(Integer idPizza) {
        return null;
    }

    @Override
    public PizzaDTO modificaPizza(PizzaDTO nuovaPizza) {
        return null;
    }
}
