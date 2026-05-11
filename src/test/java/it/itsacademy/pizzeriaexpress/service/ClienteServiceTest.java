package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.ClienteDTO;
import it.itsacademy.pizzeriaexpress.entity.*;
import it.itsacademy.pizzeriaexpress.exception.NotFoundException;
import it.itsacademy.pizzeriaexpress.repository.ClienteRepository;
import it.itsacademy.pizzeriaexpress.utility.mapper.ClienteMapper;
import it.itsacademy.pizzeriaexpress.utility.mapper.ClienteMapperImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

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
        assertThrows(NotFoundException.class, () -> {clienteService.cercaCliente(87L);});
    }

    @Test
    public void testRegistraCliente() {
        // Creazione della entity Cliente per il ritorno dello stubbing della repository .save
        Cliente clienteCreato = new Cliente();
        clienteCreato.setIdCliente(2L);
        clienteCreato.setNome("Giacomo Coccodrillini");
        clienteCreato.setIndirizzo("Via Coccodrilli 62, Fiumicino");
        clienteCreato.setTelefono("3985682254");

        // Stub del metodo .save nella repository
        when(clienteRepository.save(clienteCreato)).thenReturn(
                clienteCreato
        );

        /* Creazione del DTO Cliente da passare al metodo con gli stessi attributi della entity
        eccetto l' ID per la simulazione della auto generazione */
        ClienteDTO clienteDaCreare = new ClienteDTO();
        clienteDaCreare.setIdCliente(2L);
        clienteDaCreare.setNome("Giacomo Coccodrillini");
        clienteDaCreare.setIndirizzo("Via Coccodrilli 62, Fiumicino");
        clienteDaCreare.setTelefono("3985682254");

        // Verifica
        ClienteDTO risultato = clienteService.registraCliente(clienteDaCreare);
        assertNotNull(risultato); // Evitare i null
        assertEquals(2L, risultato.getIdCliente());
        assertEquals("Giacomo Coccodrillini", risultato.getNome());
    }
}
