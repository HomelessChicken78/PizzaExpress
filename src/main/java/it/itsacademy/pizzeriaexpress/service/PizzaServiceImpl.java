package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.PizzaDTO;
import it.itsacademy.pizzeriaexpress.entity.Pizza;
import it.itsacademy.pizzeriaexpress.exception.NotFoundException;
import it.itsacademy.pizzeriaexpress.repository.PizzaRepository;
import it.itsacademy.pizzeriaexpress.utility.mapper.PizzaMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PizzaServiceImpl implements PizzaService {
    @Autowired
    PizzaRepository repositoryPizza;

    @Autowired
    PizzaMapper mapper;

    @Override
    public PizzaDTO creaPizza(PizzaDTO nuovaPizza) {
        Pizza saved = repositoryPizza.save(mapper.toEntity(nuovaPizza));
        return mapper.toDTO(saved);
    }

    @Override
    public PizzaDTO eliminaPizza(Long idPizza) {
        PizzaDTO trovata = cercaPizza(idPizza);
        repositoryPizza.deleteById(idPizza);
        return trovata;
    }

    @Override
    public PizzaDTO cercaPizza(Long idPizza) {
        Pizza trovata = repositoryPizza.findById(idPizza)
                .orElseThrow(
                        () -> new NotFoundException("Non è stato possibile trovare una pizza")
                );
        return mapper.toDTO(trovata);
    }

    @Override
    public PizzaDTO modificaPizza(Long idPizza, PizzaDTO nuovaPizza) {
        nuovaPizza.setIdPizza(idPizza);
        Pizza saved = repositoryPizza.save(mapper.toEntity(nuovaPizza));
        return mapper.toDTO(saved);
    }
}
