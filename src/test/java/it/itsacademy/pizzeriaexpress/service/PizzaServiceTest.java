package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.PizzaDTO;
import it.itsacademy.pizzeriaexpress.entity.Pizza;
import it.itsacademy.pizzeriaexpress.exception.NotFoundException;
import it.itsacademy.pizzeriaexpress.repository.PizzaRepository;
import it.itsacademy.pizzeriaexpress.mapper.PizzaMapper;
import it.itsacademy.pizzeriaexpress.mapper.PizzaMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PizzaServiceTest {
    @Mock
    private PizzaRepository pizzaRepository; // non è la vera repository, non si connette col db

    @Spy /* Questo fa si che uso il mapper vero.
    Uso il mapper vero, perché la logica è semplice e la dipendenza con il mapper è troppo stressa
    per esser mock-ata: tutte le funzioni della service usano il mapper almeno nel return devi usare la new*/
    private PizzaMapper pizzaMapper = new PizzaMapperImpl();

    @InjectMocks
    PizzaServiceImpl pizzaService;

    @Test
    public void testCercaPizza() {
        Pizza nuovaEntityPizza = new Pizza(
                1L, "Margherita", "Pomodoro, Mozzarella, Basilico", 9.00
        );

        when(pizzaRepository.findByIdOrThrow(1L)).thenReturn(nuovaEntityPizza);
        assertDoesNotThrow(() -> pizzaService.cercaPizza(1L),
                "La pizza non viene trovata anche quando esiste");
        assertEquals("Margherita", pizzaService.cercaPizza(1L).getNome(),
                "La pizza trovata non è uguale a quella salvata inizialmente");
    }

    @Test
    public void testCercaPizzaMaNonTrovata() {
        when(pizzaRepository.findByIdOrThrow(1L)).thenThrow(NotFoundException.class);

        // Verifiche
        assertThrows(NotFoundException.class, () -> pizzaService.cercaPizza(1L),
                "Mancata propagazione dell'exception"); // Rilancia l'exception
        verify(pizzaRepository).findByIdOrThrow(1L);
    }

    @Test
    public void testCercaTutteLePizzeNelSistema() {
        List<Pizza> listaPizzeNellaRepository = List.of(
                new Pizza(1L, "Margherita", "Pomodoro, Mozzarella", 7.50),
                new Pizza(2L, "Diavola", "Pomodoro, Mozzarella, Salame piccante", 8.50),
                new Pizza(3L, "Bufalina", "Pomodoro, Mozzarella di Bufala", 10.00)
        );
        List<PizzaDTO> risultatoAttesoListaPizze = List.of(
                new PizzaDTO(1L, "Margherita", "Pomodoro, Mozzarella", 7.50),
                new PizzaDTO(2L, "Diavola", "Pomodoro, Mozzarella, Salame piccante", 8.50),
                new PizzaDTO(3L, "Bufalina", "Pomodoro, Mozzarella di Bufala", 10.00)
        );
        when(pizzaRepository.findAll()).thenReturn(listaPizzeNellaRepository);
        assertEquals(pizzaService.tutteLePizze(), risultatoAttesoListaPizze);
    }

    @Test
    public void testCreaPizza() {
        PizzaDTO margherita = new PizzaDTO(10L, "Margherita", "Pomodoro, Mozzarella", 7.50);
        Pizza margheritaEntity = new Pizza(1L, "Margherita", "Pomodoro, Mozzarella", 7.50);

        when(pizzaRepository.save(any(Pizza.class))).thenReturn(margheritaEntity);

        PizzaDTO pizzaCreata = pizzaService.creaPizza(margherita);

        assertEquals(pizzaCreata.getIdPizza(), margheritaEntity.getIdPizza());
        assertEquals(pizzaCreata.getNome(), margheritaEntity.getNome());
    }

    @Test
    public void testEliminaPizzaRitornaPizza() {
        when(pizzaRepository.findByIdOrThrow(1L)).thenReturn(
                new Pizza(1L, "Margherita", "Pomodoro, Mozzarella", 7.50));
        assertEquals(1L, pizzaService.eliminaPizza(1L).getIdPizza());
    }

    @Test
    public void testEliminaMaNonEsiste() {
        when(pizzaRepository.findByIdOrThrow(123L)).thenThrow(NotFoundException.class);

        // Verifica che l'exception venga propagata
        assertThrows(NotFoundException.class, () -> pizzaService.eliminaPizza(123L),
                "La pizza viene cancellata anche se non esiste o l'eccezione non è lanciata correttamente");
        verify((pizzaRepository)).findByIdOrThrow(any(Long.class));
    }

    @Test void testModificaPizza() {
        Long idOriginale = 1L;
        Pizza pizzaOriginale = new Pizza(idOriginale, "Funghi Porcini", "Non lo so", 70.20);
        PizzaDTO pizzaModificata = new PizzaDTO(93L, "Margherita", "Pomodoro, Mozzarella", 7.50);
        Pizza pizzaSalvata = new Pizza(idOriginale, "Margherita", "Pomodoro, Mozzarella", 7.50);

        when(pizzaRepository.findByIdOrThrow(idOriginale)).thenReturn(pizzaOriginale);
        when(pizzaRepository.save(any(Pizza.class))).thenReturn(pizzaSalvata);

        PizzaDTO result = pizzaService.modificaPizza(idOriginale, pizzaModificata);

        verify(pizzaRepository).findByIdOrThrow(idOriginale);
        assertEquals(idOriginale, result.getIdPizza());
        assertEquals("Margherita", result.getNome());
        assertEquals(7.5, result.getPrezzo());
    }
}
