package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.*;
import it.itsacademy.pizzeriaexpress.entity.*;
import it.itsacademy.pizzeriaexpress.exception.BadRequestException;
import it.itsacademy.pizzeriaexpress.exception.NotFoundException;
import it.itsacademy.pizzeriaexpress.repository.*;
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
    ClienteRepository repositoryCliente;

    @Autowired
    PizzaService pizzaService;

    @Autowired
    OrdineMapper mapper;

    @Override
    public OrdineDTO creaOrdine(Long idCliente, OrdineDTO nuovoOrdine) {
        // Controlla che il cliente ordinante esista
        Cliente clienteOrditore = repositoryCliente.findById(idCliente).orElseThrow(
                () -> new NotFoundException("Non è stato possibile trovare un cliente con id " + idCliente)
        ); // prendi l'entity in stato managed

        // Controlla che l'ordine abbia almeno una pizza
        if (nuovoOrdine.getPizzeOrdinate() == null || nuovoOrdine.getPizzeOrdinate().isEmpty()) {
            throw new BadRequestException("L'ordine appena creato deve contenere almeno una pizza");
        }

        Ordine saved = repositoryOrdine.save(mapper.toEntity(nuovoOrdine)); // salva l'ordine, senza il suo cliente
        clienteOrditore.getOrdini().add(saved); // collega il cliente al suo nuovo ordine

        return mapper.toDTO(saved);
    }

    @Override
    public OrdineDTO modificaOrdine(Long idCliente, String codiceOrdine, OrdineDTO ordineCambiato) {
        ordineCambiato.setCodice(codiceOrdine);
        cercaOrdine(idCliente, codiceOrdine);
        Ordine saved = repositoryOrdine.save(mapper.toEntity(ordineCambiato));

        return mapper.toDTO(saved);
    }

    @Override
    public OrdineDTO cercaOrdine(Long idCliente, String codiceOrdine) {
        // cerca il cliente
        Cliente clienteOrditore = repositoryCliente.findById(idCliente).orElseThrow(
                () -> new NotFoundException("Non è stato possibile trovare un cliente con id " + idCliente)
        );

        // se viene trovato cerca se tra i suoi ordini ha un ordine con codice = codiceOrdine
        Ordine trovato = clienteOrditore.getOrdini().stream()
                .filter(ord -> ord.getCodice().equals(codiceOrdine))
                .findFirst()
                .orElseThrow(
                        () -> new NotFoundException("Non è stato possibile trovare un ordine con codice "
                                + codiceOrdine + "del Cliente con id " + idCliente)
                );

        return mapper.toDTO(trovato);
    }

    @Override
    public Collection<OrdineDTO> tuttiGliOrdini() {
        return mapper.toDTO(repositoryOrdine.findAll());
    }

    @Override
    public OrdineDTO aggiungiPizza(Long idCliente, String codiceOrdine, Long idPizza, Integer quantita) {
        PizzaDTO pizza = pizzaService.cercaPizza(idPizza);
        OrdineDTO ordine = cercaOrdine(idCliente, codiceOrdine);
        ordine.getPizzeOrdinate().add(
                new OrdinePizzaDTO(quantita, pizza)
        );

        Ordine saved = repositoryOrdine.save(mapper.toEntity(ordine));
        return mapper.toDTO(saved);
    }
}
