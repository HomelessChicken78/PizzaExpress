package it.itsacademy.pizzeriaexpress.repository;

import it.itsacademy.pizzeriaexpress.entity.Rider;
import it.itsacademy.pizzeriaexpress.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RiderRepositoryTest {

    @Autowired
    private RiderRepository riderRepository;

    @Test
    public void testRiderSave() {
        Rider rider = new Rider();
        rider.setNome("Francesco Giacomini");

        Rider savedRider = riderRepository.save(rider);
        assertNotNull(savedRider, "Il rider non è stato ritornato dopo esser stato salvato. Null è il ritorno");
        assertNotNull(savedRider.getIdRider(), "L'id del nuovo rider non è stato generato correttamente");
        assertEquals("Francesco Giacomini", savedRider.getNome(), "Il nome del rider da salvare e quello ritornato non coincidono");
    }

    @Test
    public void testRiderCancella() {
        Rider rider = new Rider();
        rider.setNome("Francesco Giacomini");

        Rider savedRider = riderRepository.save(rider);
        Long idNuovoRider = savedRider.getIdRider();
        assertNotNull(idNuovoRider, "SETUP FALLITO: Non è stato possibile generare un id");

        riderRepository.deleteById(idNuovoRider);
        assertTrue(riderRepository.findById(idNuovoRider).isEmpty(), "Il rider non è stato rimosso correttamente e risulta ancora nel sistema");
    }

    @Test
    public void testCercaRider() {
        Rider rider = new Rider();
        rider.setNome("Francesco Giacomini");

        Rider savedRider = riderRepository.save(rider);
        Long idNuovoRider = savedRider.getIdRider();
        assertNotNull(idNuovoRider, "SETUP FALLITO: Non è stato possibile generare un id");

        assertTrue(riderRepository.findById(idNuovoRider).isPresent(), "Il metodo findById non ha trovato alcun rider");
    }

    @Test
    public void testFindByIdOrThrow_trovato() {
        Rider rider = new Rider();
        rider.setNome("Francesco Giacomini");

        Rider savedRider = riderRepository.save(rider);
        Long idNuovoRider = savedRider.getIdRider();
        assertNotNull(idNuovoRider, "SETUP FALLITO: Non è stato possibile generare un id");

        assertDoesNotThrow(() -> riderRepository.findByIdOrThrow(idNuovoRider), "Il metodo non ha trovato alcun rider anche se esiste");
    }

    @Test
    public void testFindByIdOrThrow_non_trovato() {
        assertThrows(NotFoundException.class, () -> riderRepository.findByIdOrThrow(23L),
                "Il metodo ha trovato un rider anche se non esiste o l'exception non è lanciata correttamente");
    }
}
