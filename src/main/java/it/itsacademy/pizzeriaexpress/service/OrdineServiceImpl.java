package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.OrdineDTO;
import it.itsacademy.pizzeriaexpress.dto.OrdinePizzaDTO;
import it.itsacademy.pizzeriaexpress.dto.PizzaDTO;
import it.itsacademy.pizzeriaexpress.entity.Ordine;
import it.itsacademy.pizzeriaexpress.entity.Pizza;
import it.itsacademy.pizzeriaexpress.exception.NotFoundException;
import it.itsacademy.pizzeriaexpress.repository.OrdineRepository;
import it.itsacademy.pizzeriaexpress.utility.mapper.OrdineMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Transactional
public class OrdineServiceImpl implements OrdineService {
    @Autowired
    OrdineRepository repositoryOrdine;

    @Autowired
    PizzaService pizzaService;

    @Autowired
    OrdineMapper mapper;

    @Override
    public OrdineDTO creaOrdine(OrdineDTO nuovoOrdine) {
        Ordine saved = repositoryOrdine.save(mapper.toEntity(nuovoOrdine));

        return mapper.toDTO(saved);
    }

    @Override
    public OrdineDTO modificaOrdine(String codiceOrdine, OrdineDTO ordineCambiato) {
        ordineCambiato.setCodice(codiceOrdine);
        cercaOrdine(codiceOrdine);
        Ordine saved = repositoryOrdine.save(mapper.toEntity(ordineCambiato));

        return null;
    }

    @Override
    public OrdineDTO cercaOrdine(String codiceOrdine) {
        Ordine trovato = repositoryOrdine.findById(codiceOrdine).orElseThrow(
                () -> new NotFoundException("Non è stato possibile trovare un ordine con codice: " + codiceOrdine)
        );

        return mapper.toDTO(trovato);
    }

    @Override
    public Collection<OrdineDTO> tuttiGliOrdini() {
        return mapper.toDTO(repositoryOrdine.findAll());
    }

    @Override
    public OrdineDTO aggiungiPizza(String codiceOrdine, Long idPizza, Integer quantita) {
        PizzaDTO pizza = pizzaService.cercaPizza(idPizza);
        OrdineDTO ordine = cercaOrdine(codiceOrdine);
        ordine.getPizzeOrdinate().add(
                new OrdinePizzaDTO(quantita, pizza)
        );

        Ordine saved = repositoryOrdine.save(mapper.toEntity(ordine));
        return mapper.toDTO(saved);
    }
}
