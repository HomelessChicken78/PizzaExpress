package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.*;
import it.itsacademy.pizzeriaexpress.entity.*;
import it.itsacademy.pizzeriaexpress.exception.BadRequestException;
import it.itsacademy.pizzeriaexpress.exception.NotFoundException;
import it.itsacademy.pizzeriaexpress.repository.ClienteRepository;
import it.itsacademy.pizzeriaexpress.utility.mapper.ClienteMapper;
import it.itsacademy.pizzeriaexpress.utility.mapper.ClienteMapperImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {
    @Mock
    private ClienteRepository clienteRepository;

    @Spy /* Questo fa si che uso il mapper vero.
    Uso il mapper vero, perché la logica è semplice e la dipendenza con il mapper è troppo stressa
    per esser mock-ata: tutte le funzioni della service usano il mapper almeno nel return devi usare la new*/
    private ClienteMapper clienteMapper = new ClienteMapperImpl();

    @InjectMocks
    private ClienteServiceImpl clienteService;

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

        // Stub del metodo .save nella repository
        when(clienteRepository.save(any(Cliente.class))).thenReturn(
                clienteCreato
        );

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
}
