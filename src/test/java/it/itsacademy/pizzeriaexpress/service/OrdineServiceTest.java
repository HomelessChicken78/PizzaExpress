package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.*;
import it.itsacademy.pizzeriaexpress.entity.*;
import it.itsacademy.pizzeriaexpress.exception.NotFoundException;
import it.itsacademy.pizzeriaexpress.repository.ClienteRepository;
import it.itsacademy.pizzeriaexpress.repository.OrdineRepository;
import it.itsacademy.pizzeriaexpress.utility.mapper.OrdineMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrdineServiceTest {
    @Mock
    private OrdineRepository ordineRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Spy /* Questo fa si che uso il mapper vero.
    Uso il mapper vero, perché la logica è semplice e la dipendenza con il mapper è troppo stressa
    per esser mock-ata: tutte le funzioni della service usano il mapper almeno nel return devi usare la new*/
    private OrdineMapperImpl ordineMapper = new OrdineMapperImpl();

    @InjectMocks
    private OrdineServiceImpl ordineService;

    @Test
    public void testCreaOrdine() {
        // Creazione nuovo Ordine + Rider per lo stubbing del metodo ordineRepository.save
        Rider riderOrdine = new Rider(1L, "Simone Dragoncelli");
        Ordine nuovoOrdine = new Ordine("123", new ArrayList<>(), riderOrdine);

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
        assertThrows(NotFoundException.class, () -> {ordineService.creaOrdine(1L, ordineDaCreare);});
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
        assertThrows(NotFoundException.class, () -> {ordineService.cercaOrdine(124L, "CIAO");});
    }

    @Test
    public void testCercaOrdineClienteNonEsiste() {
        // Stubbing del metodo della repository
        when(clienteRepository.findById(564L)).thenReturn(Optional.empty());

        // Verifica
        assertThrows(NotFoundException.class, () -> {ordineService.cercaOrdine(564L, "123");});
    }

    @Test
    public void testTuttiGliOrdini() {

    }

    @Test
    public void testAggiungiPizzaAllOrdine() {

    }
}
