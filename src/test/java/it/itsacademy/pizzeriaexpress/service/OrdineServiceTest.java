package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.*;
import it.itsacademy.pizzeriaexpress.entity.*;
import it.itsacademy.pizzeriaexpress.exception.BadRequestException;
import it.itsacademy.pizzeriaexpress.exception.NotFoundException;
import it.itsacademy.pizzeriaexpress.repository.ClienteRepository;
import it.itsacademy.pizzeriaexpress.repository.OrdineRepository;
import it.itsacademy.pizzeriaexpress.repository.RiderRepository;
import it.itsacademy.pizzeriaexpress.mapper.OrdineMapperImpl;
import it.itsacademy.pizzeriaexpress.mapper.OrdinePizzaMapperImpl;
import it.itsacademy.pizzeriaexpress.mapper.PizzaMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    @Mock
    private RiderRepository riderRepository;

    @Spy
    private final OrdinePizzaMapperImpl ordinePizzaMapper = new OrdinePizzaMapperImpl();

    @Spy
    private final PizzaMapperImpl pizzaMapper = new PizzaMapperImpl();

    @Spy /* Questo fa si che uso il mapper vero.
    Uso il mapper vero, perché la logica è semplice e la dipendenza con il mapper è troppo stressa
    per esser mock-ata: tutte le funzioni della service usano il mapper almeno nel return devi usare la new*/
    private final OrdineMapperImpl ordineMapper = new OrdineMapperImpl();

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

    private RegistraOrdineDTO creaNuovoOrdineDTO(String codice, Collection<AggiungiPizzaAllOrdineDTO> pizzeOrdinate) {
        return new RegistraOrdineDTO(codice, pizzeOrdinate, 1L);
    }

    private RegistraOrdinePrioritarioDTO creaNuovoOrdinePrioritarioDTO(Double sovrapprezzo, String codice, Collection<AggiungiPizzaAllOrdineDTO> pizzeOrdinate) {
        return new RegistraOrdinePrioritarioDTO() // Se uso il builder dovrei usare il costruttore zero args
                .builder()
                .sovrapprezzo(sovrapprezzo)
                .codice(codice)
                .pizzeOrdinate(pizzeOrdinate)
                .rider(1L)
                .build();
    }

    private Ordine creaOrdineEntity(String codice, Rider rider, Collection<OrdinePizza> pizzeOrdinate) {
        return new Ordine(codice, pizzeOrdinate, rider);
    }

    @Test
    public void creaOrdine_whenValid_thenOrdineReturned() {
        // Creazione del Cliente che la repository di cliente ritornerà
        Cliente clienteTrovato = creaNuovoCliente(1L, "Mario Mela");

        // Creazione dell'OrdinePizza e della Pizza correlati
        PizzaDTO margherita = creaNuovaPizzaDTO(11L, "Margherita");
        AggiungiPizzaAllOrdineDTO op = new AggiungiPizzaAllOrdineDTO();
        op.setQuantita(2);
        op.setIdPizza(margherita.getIdPizza());
        Collection<AggiungiPizzaAllOrdineDTO> pizzeOrdinate = new ArrayList<>();
        pizzeOrdinate.add(op);

        RegistraOrdineDTO ordineDaCreare = creaNuovoOrdineDTO("468", pizzeOrdinate);

        when(ordineRepository.save(any(Ordine.class))).thenAnswer(i -> i.getArgument(0)); // Restituisce esattamente lo stesso oggetto passato al metodo save
        when(clienteRepository.findByIdOrThrow(1L)).thenReturn(clienteTrovato);
        when(riderRepository.findByIdOrThrow(1L)).thenReturn(new Rider(1L, "Lorenzo Purebirra"));

        // Chiamata del metodo da testare
        OrdineDTO creato = ordineService.creaOrdine(1L, ordineDaCreare);

        // Verifiche
        assertNotNull(creato);
        assertNotNull(creato.getRider());
        assertEquals("468", creato.getCodice());
        assertNotNull(creato.getRider(), "L'ordine non ha alcun rider anche se richiesto");
        assertEquals("Lorenzo Purebirra", creato.getRider().getNome(), "Il nome del rider dell'ordine non è quello atteso");
    }

    @Test
    public void creaOrdine_whenNoRiderPassed_thenOrdineReturnedWithoutRider() {
        // Creazione del Cliente che la repository di cliente ritornerà
        Cliente clienteTrovato = creaNuovoCliente(2L, "Luigi Pera");

        // Creazione dell'OrdinePizza e della Pizza correlati
        PizzaDTO margherita = creaNuovaPizzaDTO(12L, "Diavola");
        AggiungiPizzaAllOrdineDTO op = new AggiungiPizzaAllOrdineDTO();
        op.setQuantita(1);
        op.setIdPizza(margherita.getIdPizza());
        Collection<AggiungiPizzaAllOrdineDTO> pizzeOrdinate = new ArrayList<>();
        pizzeOrdinate.add(op);

        RegistraOrdineDTO ordineDaCreare = creaNuovoOrdineDTO("2200", pizzeOrdinate);
        ordineDaCreare.setRider(null); // Metti l'id del Rider a null siccome il metodo creaNuovoOrdineDTO crea un rider

        when(ordineRepository.save(any(Ordine.class))).thenAnswer(i -> i.getArgument(0)); // Restituisce esattamente lo stesso oggetto passato al metodo save
        when(clienteRepository.findByIdOrThrow(2L)).thenReturn(clienteTrovato);
        // Non ci sta bisogno di fare lo stubbing del RiderRepository.findByIdOrThrow visto che non vi è un Rider

        // Chiamata del metodo da testare + verifica che non lanci eccezioni
        OrdineDTO creato = assertDoesNotThrow(() -> ordineService.creaOrdine(2L, ordineDaCreare));

        // Verifiche
        assertNotNull(creato);
        // Non dobbiamo fare il verify never: nel caso il codice funziona tramite try catch fallirebbe il test
        assertEquals("2200", creato.getCodice());
        assertNull(creato.getRider(), "L'ordine ha alcun rider anche se non richiesto");
    }

    @Test
    public void creaOrdinePrioritario_whenValid_thenOrdineReturned() {
        // Creazione del Cliente che la repository di cliente ritornerà
        Cliente clienteTrovato = creaNuovoCliente(1L, "Alberto Albero");

        // Creazione dell'OrdinePizza e della Pizza correlati
        PizzaDTO margherita = creaNuovaPizzaDTO(11L, "Diavola");
        AggiungiPizzaAllOrdineDTO op = new AggiungiPizzaAllOrdineDTO();
        op.setQuantita(3);
        op.setIdPizza(margherita.getIdPizza());
        Collection<AggiungiPizzaAllOrdineDTO> pizzeOrdinate = new ArrayList<>();
        pizzeOrdinate.add(op);

        RegistraOrdinePrioritarioDTO ordineDaCreare = creaNuovoOrdinePrioritarioDTO(2.0,"468", pizzeOrdinate);
        System.out.println(ordineDaCreare);

        when(ordineRepository.save(any(OrdinePrioritario.class))).thenAnswer(i -> i.getArgument(0)); // Restituisce esattamente lo stesso oggetto passato al metodo save
        when(clienteRepository.findByIdOrThrow(1L)).thenReturn(clienteTrovato);
        when(riderRepository.findByIdOrThrow(1L)).thenReturn(new Rider(1L, "Lorenzo Purebirra"));

        // Chiamata del metodo da testare
        OrdinePrioritarioDTO creato = ordineService.creaOrdinePrioritario(1L, ordineDaCreare);

        // Verifiche
        assertNotNull(creato);
        assertNotNull(creato.getRider());
        assertEquals("468", creato.getCodice());
        assertEquals(2.0, creato.getSovrapprezzo());
    }

    @Test
    public void creaOrdinePrioritario_whenNoRiderPassed_thenOrdineReturnedWithoutRider() {
        // Creazione del Cliente che la repository di cliente ritornerà
        Cliente clienteTrovato = creaNuovoCliente(77L, "Alessandro Quadrati");

        // Creazione dell'OrdinePizza e della Pizza correlati
        PizzaDTO margherita = creaNuovaPizzaDTO(15L, "Funghi");
        AggiungiPizzaAllOrdineDTO op = new AggiungiPizzaAllOrdineDTO();
        op.setQuantita(1);
        op.setIdPizza(margherita.getIdPizza());
        Collection<AggiungiPizzaAllOrdineDTO> pizzeOrdinate = new ArrayList<>();
        pizzeOrdinate.add(op);

        RegistraOrdinePrioritarioDTO ordineDaCreare = creaNuovoOrdinePrioritarioDTO(3.50, "2200", pizzeOrdinate);
        ordineDaCreare.setRider(null); // Metti l'id del Rider a null siccome il metodo creaNuovoOrdineDTO crea un rider

        when(ordineRepository.save(any(Ordine.class))).thenAnswer(i -> i.getArgument(0)); // Restituisce esattamente lo stesso oggetto passato al metodo save
        when(clienteRepository.findByIdOrThrow(77L)).thenReturn(clienteTrovato);
        // Non ci sta bisogno di fare lo stubbing del RiderRepository.findByIdOrThrow visto che non vi è un Rider

        // Chiamata del metodo da testare + verifica che non lanci eccezioni
        OrdinePrioritarioDTO creato = assertDoesNotThrow(() -> ordineService.creaOrdinePrioritario(77L, ordineDaCreare));

        // Verifiche
        assertNotNull(creato);
        // Non dobbiamo fare il verify never: nel caso il codice funziona tramite try catch fallirebbe il test
        assertEquals("2200", creato.getCodice());
        assertNull(creato.getRider(), "L'ordine ha alcun rider anche se non richiesto");
        assertEquals(3.50, creato.getSovrapprezzo());
    }

    @Test
    public void creaOrdine_whenClienteDoesNotExist_thenNotFoundExceptionIsThrown() {
        // Creazione dei DTO dell'Ordine e del Rider per la chiamata del metodo testato
        RegistraOrdineDTO ordineDTO = creaNuovoOrdineDTO("123", List.of(new AggiungiPizzaAllOrdineDTO(1L, 2)));

        // Stubbing del metodo .findByIdOrThrow
        when(clienteRepository.findByIdOrThrow(any(Long.class))).thenThrow(NotFoundException.class);

        // Verifica che rilanci l'eccezione
        assertThrows(NotFoundException.class, () -> ordineService.creaOrdine(1L, ordineDTO));
    }

    @Test
    public void creaOrdine_whenNoOrderedPizza_thenBadRequestExceptionIsThrown() {
        // Creazione dei DTO dell'Ordine e del Rider per la chiamata del metodo testato
        Cliente clienteTrovato = creaNuovoCliente(1L, "Marcello Muovileoni");
        RegistraOrdineDTO ordineDaCreare = creaNuovoOrdineDTO("123", null);

        // Tralasciamo di proposito la creazione della Pizza e dell'OrdinePizza

        // Stubbing dei metodi delle repository
        // Non c'è bisogno di fare lo stubbing del repositoryOrdine.save perché lancia l'exception prima
        // Per clienteRepository.findByIdOrThrow usiamo lenient. Nel caso l'esistenza del cliente venga verificata prima
        // della delle pizze, verrà usato. Se invece l'ordine è inverso non darà unnecessary stubbing
        lenient().when(clienteRepository.findByIdOrThrow(1L)).thenReturn(clienteTrovato);

        assertThrows(BadRequestException.class, () -> ordineService.creaOrdine(1L, ordineDaCreare),
                "La service non esegue il controllo del fatto che un ordine non può non avere pizze oppure lancia l'eccezione sbagliata");
    }

    @Test
    public void creaOrdine_whenAPizzaInCollectionOrderPizzaDoesNotExist_thenNotFoundExceptionIsThrown() {
        // Creazione dei DTO dell'Ordine e del Rider per la chiamata del metodo testato
        RegistraOrdineDTO ordineDTO = creaNuovoOrdineDTO("123", List.of(new AggiungiPizzaAllOrdineDTO(1L, 2)));

        // Stubbing dei metodi
        when(clienteRepository.findByIdOrThrow(any(Long.class))).thenReturn(
                new Cliente(2L, "Francesco Bianchi", "Via Bergamotti 12", "+39 477 2467", null)
        );
        when(pizzaService.cercaPizza(any(Long.class))).thenThrow(NotFoundException.class); // Simula che la pizza non sia trovata

        // Verifica che l'exception sia rilanciata
        assertThrows(NotFoundException.class, () -> ordineService.creaOrdine(1L, ordineDTO));
    }

    /*@Test
    public void testModificaOrdine() {
        // Creazione nuovo Ordine + Rider per il Cliente
        Rider riderOrdine = new Rider(1L, "Simone Dragoncelli");
        Ordine vecchioOrdine = creaOrdineEntity("123", riderOrdine, null);

        // Creazione del Cliente che la repository di cliente ritornerà
        Cliente clienteTrovato = creaNuovoCliente(1L, "Mario Mela");

        // Collega l'ordine al Cliente
        ArrayList<Ordine> ordiniCliente = new ArrayList<>();
        ordiniCliente.add(vecchioOrdine);
        clienteTrovato.setOrdini(ordiniCliente);

        // Stubbing dei metodi delle repository
        when(ordineRepository.save(any(Ordine.class))).thenAnswer(i -> i.getArgument(0)); // Restituisce esattamente lo stesso oggetto passato al metodo save
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteTrovato));

        // Creazione DTO ordine modificato
        OrdineDTO ordineModificato = new OrdineDTO("NEW123", new ArrayList<>(), null);

        OrdineDTO risultato = ordineService.modificaOrdine(1L, "123", ordineModificato);

        assertNotNull(risultato);
        assertEquals("123", risultato.getCodice());
        assertNull(risultato.getRider());
    }*/

    @Test
    public void cercaOrdine_whenExists_thenOrdineReturned() {
        // Creazione nuovo Ordine + Rider per il Cliente
        Rider riderOrdine = new Rider(30L, "Simone Dragoncelli");
        Ordine ordineCercato = creaOrdineEntity("123", riderOrdine, null);

        // Creazione del Cliente che la repository di cliente ritornerà
        Cliente clienteTrovato = creaNuovoCliente(1L, "Mario Mela");

        // Collega l'ordine al Cliente
        ArrayList<Ordine> ordiniCliente = new ArrayList<>();
        ordiniCliente.add(ordineCercato);
        clienteTrovato.setOrdini(ordiniCliente);

        when(clienteRepository.findByIdOrThrow(1L)).thenReturn(clienteTrovato);

        OrdineDTO risultato = ordineService.cercaOrdine(1L, "123");

        assertNotNull(risultato);
        assertEquals("123", risultato.getCodice());
        assertEquals(30L, risultato.getRider().getIdRider());
    }

    @Test
    public void cercaOrdine_whenDoesNotExist_thenNotFoundExceptionIsThrown() {
        // Creazione del Cliente che la repository di cliente ritornerà
        Cliente clienteTrovato = creaNuovoCliente(124L, "Leandro Impazienza");

        // Stubbing del metodo della repository
        when(clienteRepository.findByIdOrThrow(124L)).thenReturn(clienteTrovato);

        // Verifiche
        assertThrows(NotFoundException.class, () -> ordineService.cercaOrdine(124L, "CIAO"));
    }

    @Test
    public void tuttiGliOrdini_thenAllOrdersReturned() {
        List<Ordine> tuttiGliOrdini = List.of(
                creaOrdineEntity("N1 FRA", null, null),
                creaOrdineEntity("N2 LDS", null, null)
        );

        when(ordineRepository.findAll()).thenReturn(tuttiGliOrdini);

        Collection<OrdineDTO> risultato = ordineService.tuttiGliOrdini();

        assertNotNull(risultato);
        assertEquals(2, risultato.size());
        verify(ordineRepository, times(1)).findAll();
    }

    @Test
    public void aggiungiPizza_whenValid_thenPizzaIsAddedToTheOrder() {
        // DTO della pizza da aggiungere
        PizzaDTO pizzaCreataDTO = creaNuovaPizzaDTO(13L, "Diavola");

        // Creazione ordine iniziale
        Ordine ordineInizialeEntity = creaOrdineEntity("LOL",null, new ArrayList<>());

        // Creazione del Cliente che la repository di cliente ritornerà
        Cliente clienteTrovato = creaNuovoCliente(1L, "Pierre Il Grande");

        // Collega l'ordine al Cliente
        ArrayList<Ordine> ordiniCliente = new ArrayList<>();
        ordiniCliente.add(ordineInizialeEntity);
        clienteTrovato.setOrdini(ordiniCliente);

        // Stubbing
        when(pizzaService.cercaPizza(11L)).thenReturn(pizzaCreataDTO);
        when(clienteRepository.findByIdOrThrow(1L)).thenReturn(clienteTrovato);
        when(ordineRepository.save(any(Ordine.class))).thenAnswer(i -> i.getArguments()[0]);

        OrdineDTO risultato = ordineService.aggiungiPizza(1L, "LOL", 11L, 2);

        // Verifiche
        assertNotNull(risultato);
        assertEquals(1, risultato.getPizzeOrdinate().size());

        // Visto che pizzeOrdinate è una Collection, dobbiamo accedere alla prima pizza attraverso stream
        OrdinePizzaDTO pizzaAggiunta = risultato.getPizzeOrdinate().stream()
                .findFirst()
                .orElseThrow();

        assertEquals("Diavola", pizzaAggiunta.getPizza().getNome());
        assertEquals(2, pizzaAggiunta.getQuantita());

        verify(ordineRepository).save(any(Ordine.class));
    }
}