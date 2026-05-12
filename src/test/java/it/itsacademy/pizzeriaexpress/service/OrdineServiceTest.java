package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.*;
import it.itsacademy.pizzeriaexpress.entity.*;
import it.itsacademy.pizzeriaexpress.exception.BadRequestException;
import it.itsacademy.pizzeriaexpress.exception.NotFoundException;
import it.itsacademy.pizzeriaexpress.repository.ClienteRepository;
import it.itsacademy.pizzeriaexpress.repository.OrdineRepository;
import it.itsacademy.pizzeriaexpress.utility.mapper.OrdineMapperImpl;
import it.itsacademy.pizzeriaexpress.utility.mapper.OrdinePizzaMapperImpl;
import it.itsacademy.pizzeriaexpress.utility.mapper.PizzaMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrdineServiceTest {
    @Mock
    private OrdineRepository ordineRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private PizzaService pizzaService;

    @Spy
    private OrdinePizzaMapperImpl ordinePizzaMapper = new OrdinePizzaMapperImpl();

    @Spy
    private PizzaMapperImpl pizzaMapper = new PizzaMapperImpl();

    @Spy /* Questo fa si che uso il mapper vero.
    Uso il mapper vero, perché la logica è semplice e la dipendenza con il mapper è troppo stressa
    per esser mock-ata: tutte le funzioni della service usano il mapper almeno nel return devi usare la new*/
    private OrdineMapperImpl ordineMapper = new OrdineMapperImpl();

    @InjectMocks
    private OrdineServiceImpl ordineService;

    @BeforeEach
    void setUp() {
        // Colleghiamo manualmente i mapper
        // Poiché i mapper di MapStruct hanno campi privati,
        // usiamo ReflectionTestUtils di Spring
        ReflectionTestUtils.setField(
                ordineMapper,
                "ordinePizzaMapper",
                ordinePizzaMapper
        );

        ReflectionTestUtils.setField(
                ordinePizzaMapper,
                "pizzaMapper",
                pizzaMapper
        );
    }

    // Metodi privati per semplificare il codice

    private Cliente creaNuovoCliente(Long id, String nome) {
        Cliente nuovoCliente = new Cliente();
        nuovoCliente.setIdCliente(id);
        nuovoCliente.setNome(nome);
        nuovoCliente.setIndirizzo("Via Coccodrilli 42, Fiumicino");
        nuovoCliente.setTelefono("337596639");
        nuovoCliente.setOrdini(new ArrayList<>());
        return nuovoCliente;
    }

    private PizzaDTO creaNuovaPizzaDTO(Long id, String nome) {
        return new PizzaDTO(id, nome, "Molto buona", 9.0);
    }

    private OrdineDTO creaNuovoOrdineDTO(String codice, Collection<OrdinePizzaDTO> pizzeOrdinate) {
        RiderDTO riderOrdine = new RiderDTO(1L, "Simone Dragoncelli");
        return new OrdineDTO(codice, pizzeOrdinate, riderOrdine);
    }

    private Ordine creaOrdineEntity(String codice, Rider rider, Collection<OrdinePizza> pizzeOrdinate) {
        return new Ordine(codice, pizzeOrdinate, rider);
    }

    @Test
    public void testCreaOrdine() {
        // Creazione nuovo Ordine + Rider per lo stubbing del metodo ordineRepository.save
        Rider riderOrdine = new Rider(1L, "Simone Dragoncelli");
        Ordine nuovoOrdine = new Ordine("123", new ArrayList<>(), riderOrdine);

        // Creazione del Cliente che la repository di cliente ritornerà
        Cliente clienteTrovato = new Cliente();
        clienteTrovato.setIdCliente(1L);
        clienteTrovato.setNome("Mario Mela");
        clienteTrovato.setIndirizzo("Via Coccodrilli 42, Fiumicino");
        clienteTrovato.setTelefono("337596639");
        clienteTrovato.setOrdini(new ArrayList<>());

        // Creazione dell'OrdinePizza e della Pizza correlati
        PizzaDTO margherita = new PizzaDTO();
        margherita.setNome("Margherita");
        margherita.setDescrizione("Molto buona");
        margherita.setPrezzo(9.0);

        OrdinePizzaDTO op = new OrdinePizzaDTO();
        op.setQuantita(2);
        op.setPizza(margherita);
        Collection<OrdinePizzaDTO> pizzeOrdinate = new ArrayList<>();
        pizzeOrdinate.add(op);

        // Creazione dei DTO dell'Ordine e del Rider per la chiamata del metodo testato
        RiderDTO riderOrdineDaCreare = new RiderDTO(1L, "Simone Dragoncelli");
        OrdineDTO ordineDaCreare = new OrdineDTO("123", pizzeOrdinate, riderOrdineDaCreare);

        // Stubbing dei metodi delle repository
        when(ordineRepository.save(any(Ordine.class))).thenReturn(nuovoOrdine);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteTrovato));

        // Chiamata del metodo da testare
        OrdineDTO creato = ordineService.creaOrdine(1L, ordineDaCreare);

        // Verifica
        assertNotNull(creato);
        assertEquals("123", creato.getCodice());
        assertEquals(1L, creato.getRider().getIdRider());
        verify(ordineRepository, times(1)).save(any(Ordine.class));
    }

    @Test
    public void testCreaOrdineClienteNonEsiste() {
        // Creazione dei DTO dell'Ordine e del Rider per la chiamata del metodo testato
        RiderDTO riderOrdineDaCreare = new RiderDTO(1L, "Simone Dragoncelli");
        OrdineDTO ordineDaCreare = new OrdineDTO("123", null, riderOrdineDaCreare);

        // Verifica
        assertThrows(NotFoundException.class, () -> ordineService.creaOrdine(1L, ordineDaCreare));
    }

    @Test
    public void testCreaOrdineSenzaPizze() {
        // Creazione dei DTO dell'Ordine e del Rider per la chiamata del metodo testato
        RiderDTO riderOrdineDaCreare = new RiderDTO(1L, "Simone Dragoncelli");
        OrdineDTO ordineDaCreare = new OrdineDTO("123", null, riderOrdineDaCreare);

        // Creazione del Cliente che la repository di cliente ritornerà
        Cliente clienteTrovato = new Cliente();
        clienteTrovato.setIdCliente(1L);
        clienteTrovato.setNome("Mario Mela");
        clienteTrovato.setIndirizzo("Via Coccodrilli 42, Fiumicino");
        clienteTrovato.setTelefono("337596639");
        clienteTrovato.setOrdini(new ArrayList<>());

        // Tralasciamo di proposito la creazione della Pizza e dell'OrdinePizza

        // Stubbing dei metodi delle repository
        // Non c'è bisogno di fare lo stubbing del repositoryOrdine.save perché lancia l'exception prima
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteTrovato));

        assertThrows(BadRequestException.class, () -> ordineService.creaOrdine(1L, ordineDaCreare)
        , "La service non esegue il controllo del fatto che un ordine non può non avere pizze oppure lancia l'eccezione sbagliata");
    }


    @Test
    public void testModificaOrdine() {
        // Creazione nuovo Ordine + Rider per il Cliente
        Rider riderOrdine = new Rider(1L, "Simone Dragoncelli");
        Ordine vecchioOrdine = new Ordine("123", new ArrayList<>(), riderOrdine);

        // Creazione del Cliente che la repository di cliente ritornerà
        Cliente clienteTrovato = new Cliente();
        clienteTrovato.setIdCliente(1L);
        clienteTrovato.setNome("Mario Mela");
        clienteTrovato.setIndirizzo("Via Coccodrilli 42, Fiumicino");
        clienteTrovato.setTelefono("337596639");

        // Collega l'ordine al Cliente
        ArrayList<Ordine> ordiniCliente = new ArrayList<>();
        ordiniCliente.add(vecchioOrdine);
        clienteTrovato.setOrdini(ordiniCliente);

        // Stubbing dei metodi delle repository
        when(ordineRepository.save(any(Ordine.class))).thenAnswer(i -> i.getArguments()[0]);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteTrovato));

        // Creazione DTO ordine modificato
        OrdineDTO ordineModificato = new OrdineDTO("NEW123", new ArrayList<>(), null);

        // Chiamata metodo da testare
        OrdineDTO risultato = ordineService.modificaOrdine(1L, "123", ordineModificato);

        // Verifica
        assertNotNull(risultato);
        assertEquals("123", risultato.getCodice());
        assertNull(risultato.getRider());
    }

    @Test
    public void testCercaOrdineEsistente() {
        // Creazione nuovo Ordine + Rider per il Cliente
        Rider riderOrdine = new Rider(30L, "Simone Dragoncelli");
        Ordine ordineCercato = new Ordine("123", new ArrayList<>(), riderOrdine);

        // Creazione del Cliente che la repository di cliente ritornerà
        Cliente clienteTrovato = new Cliente();
        clienteTrovato.setIdCliente(1L);
        clienteTrovato.setNome("Mario Mela");
        clienteTrovato.setIndirizzo("Via Coccodrilli 42, Fiumicino");
        clienteTrovato.setTelefono("337596639");

        // Collega l'ordine al Cliente
        ArrayList<Ordine> ordiniCliente = new ArrayList<>();
        ordiniCliente.add(ordineCercato);
        clienteTrovato.setOrdini(ordiniCliente);

        // Stubbing del metodo della repository
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteTrovato));

        // Chiamata metodo da testare
        OrdineDTO risultato = ordineService.cercaOrdine(1L, "123");

        // Verifiche
        assertNotNull(risultato);
        assertEquals("123", risultato.getCodice());
        assertNotNull(risultato.getRider());
        assertEquals(30L, risultato.getRider().getIdRider());
        verify(clienteRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void testCercaOrdineInesistente() {
        // Creazione del Cliente che la repository di cliente ritornerà
        Cliente clienteTrovato = new Cliente();
        clienteTrovato.setIdCliente(124L);
        clienteTrovato.setNome("Mario Mela");
        clienteTrovato.setIndirizzo("Via Coccodrilli 42, Fiumicino");
        clienteTrovato.setTelefono("337596639");
        clienteTrovato.setOrdini(new ArrayList<>());

        // Stubbing del metodo della repository
        when(clienteRepository.findById(124L)).thenReturn(Optional.of(clienteTrovato));

        // Verifica
        assertThrows(NotFoundException.class, () -> ordineService.cercaOrdine(124L, "CIAO"));
    }

    @Test
    public void testCercaOrdineClienteNonEsiste() {
        // Stubbing del metodo della repository
        when(clienteRepository.findById(564L)).thenReturn(Optional.empty());

        // Verifica
        assertThrows(NotFoundException.class, () -> ordineService.cercaOrdine(564L, "123"));
    }

    @Test
    public void testTuttiGliOrdini() {
        List<Ordine> tuttiGliOrdini = List.of(
                new Ordine("N1 FRA", new ArrayList<>(), null),
                new Ordine("N2 LDS", new ArrayList<>(), null)
        );

        when(ordineRepository.findAll()).thenReturn(tuttiGliOrdini);

        Collection<OrdineDTO> risultato = ordineService.tuttiGliOrdini();

        assertNotNull(risultato);
        assertEquals(2, risultato.size());
        verify(ordineRepository, times(1)).findAll();
    }

    @Test
    public void testAggiungiPizzaAllOrdine() {
        // DTO della pizza da aggiungere
        PizzaDTO pizzaCreataDTO = new PizzaDTO(11L, "Margherita", "Pomodoro, Mozzarella, Basilico", 9.00);

        // Creazione ordine iniziale
        Ordine ordineInizialeEntity = new Ordine("LOL", new ArrayList<>(), null);

        // Creazione del Cliente che la repository di cliente ritornerà
        Cliente clienteTrovato = new Cliente();
        clienteTrovato.setIdCliente(1L);
        clienteTrovato.setNome("Mario Mela");
        clienteTrovato.setIndirizzo("Via Coccodrilli 42, Fiumicino");
        clienteTrovato.setTelefono("337596639");

        // Collega l'ordine al Cliente
        ArrayList<Ordine> ordiniCliente = new ArrayList<>();
        ordiniCliente.add(ordineInizialeEntity);
        clienteTrovato.setOrdini(ordiniCliente);

        // Stubbing
        when(pizzaService.cercaPizza(11L)).thenReturn(pizzaCreataDTO);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteTrovato));
        when(ordineRepository.save(any(Ordine.class))).thenAnswer(i -> i.getArguments()[0]);

        OrdineDTO risultato = ordineService.aggiungiPizza(1L, "LOL", 11L, 2);

        // Verifiche
        assertNotNull(risultato);
        assertEquals(1, risultato.getPizzeOrdinate().size());

        // Visto che pizzeOrdinate è una Collection, dobbiamo accedere alla prima pizza attraverso stream
        OrdinePizzaDTO pizzaAggiunta = risultato.getPizzeOrdinate().stream()
                .findFirst()
                .orElseThrow();

        assertEquals("Margherita", pizzaAggiunta.getPizza().getNome());
        assertEquals(2, pizzaAggiunta.getQuantita());

        verify(ordineRepository).save(any(Ordine.class));
    }
}
