package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.RiderDTO;
import it.itsacademy.pizzeriaexpress.entity.Rider;
import it.itsacademy.pizzeriaexpress.exception.NotFoundException;
import it.itsacademy.pizzeriaexpress.repository.RiderRepository;
import it.itsacademy.pizzeriaexpress.mapper.RiderMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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
    public void cercaRider_whenExists_thenRiderReturned() {
        Rider nuovoEntityRider = new Rider(
                1L, "Assert Falsest"
        );

        when(riderRepository.findByIdOrThrow(1L)).thenReturn(nuovoEntityRider);

        assertEquals("Assert Falsest", riderService.cercaRider(1L).getNome());
        verify(riderRepository).findByIdOrThrow(1L);
    }

    @Test
    public void cercaRider_whenDoesNotExist_thenNotFoundExceptionIsThrown() {
        when(riderRepository.findByIdOrThrow(1L)).thenThrow(NotFoundException.class);

        // Verifiche
        assertThrows(NotFoundException.class, () -> riderService.cercaRider(1L),
                "L'exception non è propagata"); // Rilancia l'exception
        verify(riderRepository).findByIdOrThrow(1L);
    }

    @Test
    public void tuttiIRider_thenAllRidersReturned() {
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
    public void licenziaRider_whenExists_thenRiderReturned() {
        when(riderRepository.findByIdOrThrow(1L)).thenReturn(new Rider(1L, "Pierre Alberghieri"));

        // Verifiche
        assertEquals(1L, riderService.licenziaRider(1L).getIdRider());
        verify(riderRepository).findByIdOrThrow(1L);
    }

    @Test
    public void registraRider_whenValid_thenRiderReturned() {
        RiderDTO nuovoRider = new RiderDTO(10L, "Margherita");
        Rider riderEntity = new Rider(7L, "Margherita");

        when(riderRepository.save(any(Rider.class))).thenReturn(riderEntity);

        RiderDTO riderCreato = riderService.registraRider(nuovoRider);

        assertEquals(7L, riderCreato.getIdRider());
        assertEquals("Margherita", riderCreato.getNome());
    }
}
