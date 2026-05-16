package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.*;
import it.itsacademy.pizzeriaexpress.entity.*;
import it.itsacademy.pizzeriaexpress.exception.BadRequestException;
import it.itsacademy.pizzeriaexpress.exception.ConflictException;
import it.itsacademy.pizzeriaexpress.exception.NotFoundException;
import it.itsacademy.pizzeriaexpress.repository.*;
import it.itsacademy.pizzeriaexpress.mapper.OrdineMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional
public class OrdineServiceImpl implements OrdineService {
    @Autowired
    OrdineRepository repositoryOrdine;

    @Autowired
    ClienteRepository repositoryCliente;

    @Autowired
    RiderRepository repositoryRider;

    @Autowired
    PizzaService pizzaService;

    @Autowired
    OrdineMapper mapper;

    private void validaRegoleDiBusiness(RegistraOrdineDTO ordineDaValidare) {
        // Controlla che il codice dell'ordine non sia duplicato
        if (repositoryOrdine.existsById(ordineDaValidare.getCodice()))
            throw new ConflictException("Esiste già un ordine con codice " + ordineDaValidare.getCodice());

        // Controlla che l'ordine abbia almeno una pizza
        if (ordineDaValidare.getPizzeOrdinate() == null || ordineDaValidare.getPizzeOrdinate().isEmpty())
            throw new BadRequestException("L'ordine appena creato deve contenere almeno una pizza");
    }

    @Override
    public OrdineDTO creaOrdine(Long idCliente, RegistraOrdineDTO nuovoOrdine) {
        validaRegoleDiBusiness(nuovoOrdine);

        // Controlla che il cliente ordinante esista
        Cliente clienteOrditore = repositoryCliente.findByIdOrThrow(idCliente); // prendi l'entity in stato managed

        // Visto che RegistraOrdineDTO ha solo gli id delle pizze dobbiamo fare un findById sulle pizze.
        // Tuttavia il mapper non puà fare il findById quindi metà della conversione deve esser fatta direttamente nel service
        // (il mapper può comunque occuparsi della conversione senza pizze)
        OrdineDTO daSalvare = mapper.toCompleteOrdine(nuovoOrdine);
        daSalvare.setPizzeOrdinate(new ArrayList<>());

        for (AggiungiPizzaAllOrdineDTO pizzaOrdinata : nuovoOrdine.getPizzeOrdinate()) {
            // Recuperiamo la pizza reale dal database (come DTO)
            PizzaDTO pizzaTrovata = pizzaService.cercaPizza(pizzaOrdinata.getIdPizza());

            // Colleghiamo la pizza presa al suo OrdinePizza
            OrdinePizzaDTO op = new OrdinePizzaDTO();
            op.setPizza(pizzaTrovata);
            op.setQuantita(pizzaOrdinata.getQuantita());

            // Colleghiamo all'ordine
            daSalvare.getPizzeOrdinate().add(op);
        }

        Ordine saved = repositoryOrdine.save(mapper.toEntity(daSalvare)); // salva l'ordine, senza il suo cliente
        clienteOrditore.getOrdini().add(saved); // collega il cliente al suo nuovo ordine

        // Cerca il rider
        if (nuovoOrdine.getRider() != null) {
            Rider riderTrovato = repositoryRider.findByIdOrThrow(nuovoOrdine.getRider());
            saved.setRider(riderTrovato);
        }

        return mapper.toDTO(saved);
    }

    @Override
    public OrdinePrioritarioDTO creaOrdinePrioritario(Long idCliente, RegistraOrdinePrioritarioDTO nuovoOrdine) {
        validaRegoleDiBusiness(nuovoOrdine);

        // Controlla che il cliente ordinante esista
        Cliente clienteOrditore = repositoryCliente.findByIdOrThrow(idCliente); // prendi l'entity in stato managed

        // Visto che RegistraOrdineDTO ha solo gli id delle pizze dobbiamo fare un findById sulle pizze.
        // Tuttavia il mapper non puà fare il findById quindi metà della conversione deve esser fatta direttamente nel service
        // (il mapper può comunque occuparsi della conversione senza pizze)
        OrdinePrioritarioDTO daSalvare = mapper.toCompleteOrdine(nuovoOrdine);
        daSalvare.setPizzeOrdinate(new ArrayList<>());

        for (AggiungiPizzaAllOrdineDTO pizzaOrdinata : nuovoOrdine.getPizzeOrdinate()) {
            // Recuperiamo la pizza reale dal database (come DTO)
            PizzaDTO pizzaTrovata = pizzaService.cercaPizza(pizzaOrdinata.getIdPizza());

            // Colleghiamo la pizza presa al suo OrdinePizza
            OrdinePizzaDTO op = new OrdinePizzaDTO();
            op.setPizza(pizzaTrovata);
            op.setQuantita(pizzaOrdinata.getQuantita());

            // Colleghiamo all'ordine
            daSalvare.getPizzeOrdinate().add(op);
        }

        OrdinePrioritario saved = repositoryOrdine.save(mapper.toEntityPrio(daSalvare)); // salva l'ordine, senza il suo cliente

        clienteOrditore.getOrdini().add(saved); // collega il cliente al suo nuovo ordine

        // Cerca il rider
        if (nuovoOrdine.getRider() != null) {
            Rider riderTrovato = repositoryRider.findByIdOrThrow(nuovoOrdine.getRider());
            saved.setRider(riderTrovato);
        }

        return mapper.toDTO(saved);
    }

    @Override
    public OrdineDTO cercaOrdine(Long idCliente, String codiceOrdine) {
        // cerca il cliente
        Cliente clienteOrditore = repositoryCliente.findByIdOrThrow(idCliente);

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
        // TODO questo dovrebbe ritornare i DTO completi
        return mapper.toDTO(repositoryOrdine.findAll());
    }

    //TODO implementare test
    @Override
    public Collection<OrdineDTO> tuttiGliOrdiniNonPrioritari() {
        return mapper.toDTO(repositoryOrdine.findAll()
                .stream()
                .filter((ord) -> !(ord instanceof OrdinePrioritario))
                .toList());
    }

    //TODO implementare test
    @Override
    public Collection<OrdinePrioritarioDTO> tuttiGliOrdiniPrioritari() {
        return mapper.toDTO(repositoryOrdine.findAll()
                .stream()
                .filter((ord) -> ord instanceof OrdinePrioritario)
                .map((ord) -> (OrdinePrioritario) ord)
                .toList());
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

    @Override
    public OrdineDTO cambiaRider(Long idCliente, String codiceOrdine, Long idRider) {
        Ordine ordine = mapper.toEntity(cercaOrdine(idCliente, codiceOrdine));

        // Cerca il rider
        Rider riderTrovato = repositoryRider.findByIdOrThrow(idRider);
        ordine.setRider(riderTrovato);

        Ordine saved = repositoryOrdine.save(ordine);
        return mapper.toDTO(saved);
    }
}
