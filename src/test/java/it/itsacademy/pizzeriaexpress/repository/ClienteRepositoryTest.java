package it.itsacademy.pizzeriaexpress.repository;

import it.itsacademy.pizzeriaexpress.entity.Cliente;
import it.itsacademy.pizzeriaexpress.exception.NotFoundException;
import org.aspectj.weaver.ast.Not;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    public void testRegistraCliente() {

        Cliente c = new Cliente();

        c.setNome("Matt Murdock");
        c.setIndirizzo("via Laurentina 1");
        c.setTelefono("98498489489");

        Cliente saved = clienteRepository.save(c);

        assertNotNull(saved);
        assertEquals(c.getIdCliente(), saved.getIdCliente());

    }
    @Test
    public void testModificaCliente() {

        Cliente c = new Cliente();

        c.setNome("Adam Warlock");
        c.setIndirizzo("via Manzoni 12");
        c.setTelefono("16515613219841");

        Cliente saved = clienteRepository.save(c);

        saved.setNome("Maria");
        Cliente updated = clienteRepository.save(saved);

        assertEquals("Maria", updated.getNome());
    }

    @Test
    public void testCercaCliente() {

        Cliente c = new Cliente();

        c.setNome("Natasha Romanov");
        c.setIndirizzo("via Fermi 12");
        c.setTelefono("162325615616");

        Cliente saved = clienteRepository.save(c);

        Optional<Cliente> found = clienteRepository.findById(saved.getIdCliente());

        assertTrue(found.isPresent());
        assertEquals(saved.getIdCliente(), found.get().getIdCliente());
    }

    @Test
    public void testFindByIdOrThrow_trovato() {
        Cliente c = new Cliente();

        c.setNome("Persona Random");
        c.setIndirizzo("via In Movimento 12");
        c.setTelefono("78321646587");

        Cliente saved = clienteRepository.save(c);

        Cliente found = clienteRepository.findByIdOrThrow(c.getIdCliente());

        assertNotNull(found, "Il metodo ClienteRepository.findByIdOrThrow(Long) ritorna null");
        assertDoesNotThrow(() -> clienteRepository.findByIdOrThrow(c.getIdCliente()),
                "Il metodo ClienteRepository.findByIdOrThrow(Long) lancia l'eccezione quando il cliente esiste");
        assertEquals(saved.getIdCliente(), found.getIdCliente());
    }

    @Test
    public void testFindByIdOrThrow_nonTrovato() {
        assertThrows(NotFoundException.class, () -> clienteRepository.findByIdOrThrow(474L),
                "Il metodo ClienteRepository.findByIdOrThrow(Long) non lancia l'eccezione quando il cliente non esiste");
    }

    @Test
    public void testEliminaCliente() {

        Cliente c = new Cliente();

        c.setNome("Matt Murdock");
        c.setIndirizzo("via Laurentina 1");
        c.setTelefono("98498489489");

        Cliente saved = clienteRepository.save(c);

        clienteRepository.deleteById(saved.getIdCliente());
        Optional<Cliente> deleted = clienteRepository.findById(saved.getIdCliente());

        assertFalse(deleted.isPresent());
    }
    @Test
    public void testTuttiIClienti() {
        int numInizialeClienti = clienteRepository.findAll().size(); // Tiene in considerazione dello stato della repo prima del test
        Cliente c1 = new Cliente();

        c1.setNome("Natasha Romanov");
        c1.setIndirizzo("via Fermi 12");
        c1.setTelefono("162325615616");
        clienteRepository.save(c1);

        Cliente c2 = new Cliente();

        c2.setNome("Matt Murdock");
        c2.setIndirizzo("via Laurentina 1");
        c2.setTelefono("98498489489");
        clienteRepository.save(c2);


        Collection<Cliente> clienti = clienteRepository.findAll();


        Assert.assertNotNull(clienti);
        assertEquals(2+numInizialeClienti, clienti.size());
    }
}