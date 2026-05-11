package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.PizzaDTO;
import it.itsacademy.pizzeriaexpress.entity.Pizza;
import it.itsacademy.pizzeriaexpress.exception.NotFoundException;
import it.itsacademy.pizzeriaexpress.repository.PizzaRepository;
import it.itsacademy.pizzeriaexpress.utility.mapper.PizzaMapper;
import it.itsacademy.pizzeriaexpress.utility.mapper.PizzaMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

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

        when(pizzaRepository.findById(1L)).thenReturn(Optional.of(nuovaEntityPizza));
        assertEquals("Margherita", pizzaService.cercaPizza(1L).getNome());
    }

    @Test
    public void testCercaPizzaMaNonTrovata() {
        when(pizzaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {pizzaService.cercaPizza(1L);});
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
}
