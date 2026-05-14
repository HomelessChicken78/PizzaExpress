package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.*;
import it.itsacademy.pizzeriaexpress.entity.*;
import it.itsacademy.pizzeriaexpress.exception.BadRequestException;
import it.itsacademy.pizzeriaexpress.exception.NotFoundException;
import it.itsacademy.pizzeriaexpress.repository.ClienteRepository;
import it.itsacademy.pizzeriaexpress.utility.mapper.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {
    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private OrdineService ordineService;

    @Spy /* Questo fa si che uso il mapper vero.
    Uso il mapper vero, perché la logica è semplice e la dipendenza con il mapper è troppo stressa
    per esser mock-ata: tutte le funzioni della service usano il mapper almeno nel return devi usare la new*/
    private ClienteMapper clienteMapper = new ClienteMapperImpl();

    @Spy
    private OrdineMapper ordineMapper = new OrdineMapperImpl();

    @Spy
    private OrdinePizzaMapper ordinePizzaMapper = new OrdinePizzaMapperImpl();

    @Spy
    private PizzaMapper pizzaMapper = new PizzaMapperImpl();

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @BeforeEach
    void setUp() {
        // Colleghiamo manualmente i mapper
        // Poiché i mapper di MapStruct hanno campi privati,
        // usiamo ReflectionTestUtils di Spring
        ReflectionTestUtils.setField(
                clienteMapper,
                "ordineMapper",
                ordineMapper
        );
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

    @Test
    public void testCercaClienteEsiste() {
        // Creazione della entity che il metodo clienteRepository.findById ritornerà
        Cliente clienteCreato = new Cliente();
        clienteCreato.setIdCliente(1L);
        clienteCreato.setNome("Mario Mela");
        clienteCreato.setIndirizzo("Via Coccodrilli 42, Fiumicino");
        clienteCreato.setTelefono("337596639");

        // Stubbing del metodo clienteRepository.findById
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(
                clienteCreato
        ));

        ClienteDTO trovato = clienteService.cercaCliente(1L);

        // Verifica
        assertNotNull(trovato);
        assertEquals(1L, trovato.getIdCliente());
        assertEquals("Mario Mela", trovato.getNome());
    }

    @Test
    public void testCercaClienteInesistente() {
        // Verifica
        assertThrows(NotFoundException.class, () -> clienteService.cercaCliente(87L));
    }

    @Test
    public void testRegistraCliente() {
        // Stub del metodo clienteService.saveAndFlush
        when(clienteRepository.saveAndFlush(any(Cliente.class))).thenAnswer(i -> {
            Cliente cliente = i.getArgument(0);
            cliente.setIdCliente(2L); // Ritorna esattamente lo stesso cliente passato ma con id = 2L per simulare l'auto-increment
            return cliente;
        });


        /* Creazione del DTO Cliente da passare al metodo con gli stessi attributi della entity
        eccetto l' ID per la simulazione della auto generazione */
        RegistraClienteDTO clienteDaCreare = new RegistraClienteDTO();
        clienteDaCreare.setIdCliente(2L);
        clienteDaCreare.setNome("Giacomo Coccodrillini");
        clienteDaCreare.setIndirizzo("Via Coccodrilli 62, Fiumicino");
        clienteDaCreare.setTelefono("3985682254");
        clienteDaCreare.setOrdini(
                List.of(
                    new RegistraOrdineDTO(
                            "A23", new ArrayList<>(), null
                    )
                )
        ); // Metti un OrdineDTO "finto" (senza pizza ordinate) al Cliente

        // Verifica
        ClienteDTO risultato = clienteService.registraCliente(clienteDaCreare);
        assertNotNull(risultato, "La service ritorna null"); // Evitare i null
        assertEquals(2L, risultato.getIdCliente());
        assertEquals("Giacomo Coccodrillini", risultato.getNome());
    }

    @Test
    public void testRegistraClienteSenzaUnOrdine() {
        // Creazione della entity Cliente per il ritorno dello stubbing della repository .save
        Cliente clienteCreato = new Cliente();
        clienteCreato.setIdCliente(2L);
        clienteCreato.setNome("Giacomo Coccodrillini");
        clienteCreato.setIndirizzo("Via Coccodrilli 62, Fiumicino");
        clienteCreato.setTelefono("3985682254");

        // Aggiunta di un ordine
        Collection<Ordine> ordiniEntity = new ArrayList<>();
        ordiniEntity.add(new Ordine());
        clienteCreato.setOrdini(ordiniEntity); // Collego l'ordine al cliente

        /* Creazione del DTO Cliente da passare al metodo con gli stessi attributi della entity
        eccetto l' ID per la simulazione della auto generazione */
        RegistraClienteDTO clienteDaCreare = new RegistraClienteDTO();
        clienteDaCreare.setIdCliente(2L);
        clienteDaCreare.setNome("Giacomo Coccodrillini");
        clienteDaCreare.setIndirizzo("Via Coccodrilli 62, Fiumicino");
        clienteDaCreare.setTelefono("3985682254");
        // Ometto di proposito di mettere un ordine

        // Verifica
        assertThrows(BadRequestException.class, () -> clienteService.registraCliente(clienteDaCreare),
                "La service non controlla che il dto contenga la collection di ordini " +
                        "o lo controlla ma non lancia l'exception corretta");

        // Provo a inserire un arraylist vuoto
        clienteDaCreare.setOrdini(new ArrayList<>());

        // Verifica
        assertThrows(BadRequestException.class, () -> clienteService.registraCliente(clienteDaCreare),
                "La service non controlla che la collection di ordini non sia vuota " +
                        "o la controlla ma non lancia l'exception corretta");

    }

    @Test
    public void testCercaTuttiGliOrdiniDiUnCliente() {
        // Creazione dei Rider
        Rider rider1 = new Rider(110L, "Franceso Mangio");
        Rider rider2 = new Rider(111L, "Dario Montagna");

        // Creazione delle Pizze
        Pizza margherita = new Pizza(1L, "Margherita", "Pomodoro, mozzarella, basilico", 5.0);
        Pizza diavola = new Pizza(2L, "Diavola", "Pomodoro, mozzarella, salame piccante", 6.5);
        Pizza quattroFormaggi = new Pizza(3L, "Quattro Formaggi", "Mozzarella, gorgonzola, emmental, parmigiano", 7.0);

        // Creazione degli OrdinePizza
        OrdinePizza op1 = new OrdinePizza(1L, 2, margherita);
        OrdinePizza op2 = new OrdinePizza(2L, 1, diavola);
        OrdinePizza op3 = new OrdinePizza(3L, 3, quattroFormaggi);
        OrdinePizza op4 = new OrdinePizza(20L, 1, margherita);
        OrdinePizza op5 = new OrdinePizza(21L, 2, diavola);

        // Creazione degli ordini
        Ordine ordine1 = new Ordine("Cool001",
                Arrays.asList(op1, op2, op3),
                rider1);
        Ordine ordine2 = new Ordine("Cool002",
                Arrays.asList(op4, op5),
                rider2);

        // Lista di ordini
        List<Ordine> listaOrdini = new ArrayList<>();
        listaOrdini.add(ordine1);
        listaOrdini.add(ordine2);

        // Stubbing del metodo
        when(clienteRepository.findById(1L)).thenReturn(
                Optional.of(new Cliente(37L, "Leandro Impazienza", "Via Cesare Pavese 123, Roma", "351 4561 2098", listaOrdini))
        );

        Collection<OrdineDTO> risultato = clienteService.tuttiGliOrdiniDelCliente(1L);

        // Verifiche
        assertNotNull(risultato, "Non è stato ritornato alcun risultato dal test");
        assertEquals(2, risultato.size(), "Numero di ordini del cliente diverso da quello atteso");
        assertNotNull(risultato.stream().findFirst().orElse(null), "La lista contiene tornata contiene ordini null");
        assertEquals("Cool001", risultato.stream().findFirst().orElse(null).getCodice(), "Il codice di uno degli ordini ritornato non è quello atteso");
    }
}
