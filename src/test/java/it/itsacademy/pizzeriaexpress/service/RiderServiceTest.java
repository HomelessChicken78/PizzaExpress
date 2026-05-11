package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.RiderDTO;
import it.itsacademy.pizzeriaexpress.entity.Pizza;
import it.itsacademy.pizzeriaexpress.entity.Rider;
import it.itsacademy.pizzeriaexpress.exception.NotFoundException;
import it.itsacademy.pizzeriaexpress.repository.RiderRepository;
import it.itsacademy.pizzeriaexpress.utility.mapper.RiderMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RiderServiceTest {
    @Mock
    private RiderRepository riderRepository; // non è la vera repository, non si connette col db

    @Spy /* Questo fa si che uso il mapper vero.
    Uso il mapper vero, perché la logica è semplice e la dipendenza con il mapper è troppo stressa
    per esser mock-ata: tutte le funzioni della service usano il mapper almeno nel return devi usare la new*/
    private RiderMapperImpl riderMapper = new RiderMapperImpl();

    @InjectMocks
    RiderServiceImpl riderService;

    @Test
    public void testCercaPizza() {
        Rider nuovoEntityRider = new Rider(
                1L, "Assert Falsest"
        );

        when(riderRepository.findById(1L)).thenReturn(Optional.of(nuovoEntityRider));
        assertEquals("Assert Falsest", riderService.cercaRider(1L).getNome());
    }

    @Test
    public void testCercaPizzaMaNonTrovata() {
        when(riderRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {riderService.cercaRider(1L);});
    }

    @Test
    public void testCercaTutteLePizzeNelSistema() {
        List<Rider> listaRiderNellaRepository = List.of(
                new Rider(4L, "Assert Falser"),
                new Rider(5L, "Ferdinando Quadrati"),
                new Rider(6L, "Nicol Rossi")
        );
        List<RiderDTO> risultatoAtteso = List.of(
                new RiderDTO(4L, "Assert Falser"),
                new RiderDTO(5L, "Ferdinando Quadrati"),
                new RiderDTO(6L, "Nicol Rossi")
        );
        when(riderRepository.findAll()).thenReturn(listaRiderNellaRepository);
        assertEquals(riderService.tuttiIRider(), risultatoAtteso);
    }

    @Test
    public void testEliminaPizzaRitornaPizza() {
        when(riderRepository.findById(1L)).thenReturn(Optional.of(new Rider(1L, "Pierre Alberghieri")));
        assertEquals(1L, riderService.licenziaRider(1L).getIdRider());
    }

    @Test
    public void testCreaPizza() {
        RiderDTO nuovoRider = new RiderDTO(10L, "Margherita");
        Rider riderEntity = new Rider(7L, "Margherita");

        when(riderRepository.save(riderMapper.toEntity(nuovoRider))).thenReturn(riderEntity);

        RiderDTO pizzaCreata = riderService.registraRider(nuovoRider);

        assertEquals(7L, pizzaCreata.getIdRider());
        assertEquals("Margherita", pizzaCreata.getNome());
    }
}
