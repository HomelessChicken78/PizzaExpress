package it.itsacademy.pizzeriaexpress.repository;

import it.itsacademy.pizzeriaexpress.entity.Ordine;
import it.itsacademy.pizzeriaexpress.entity.OrdinePizza;
import it.itsacademy.pizzeriaexpress.entity.Pizza;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrdineRepositoryTest {

    @Autowired
    private OrdineRepository ordineRepository;

    @Autowired
    private PizzaRepository pizzaRepository;

    @Test
    public void testOrdineSave() {
        // Crea la pizza
        Pizza margherita = new Pizza();
        margherita.setNome("Margherita");
        margherita.setDescrizione("Molto buona");
        margherita.setPrezzo(9.0);

        // Salva la pizza nel sistema
        // così che si possa collegare la entity Pizza in stato managed all'ordine pizza transient
        Pizza savedPizza = pizzaRepository.saveAndFlush(margherita);
        assertNotNull(savedPizza, "SETUP FALLITO: non è stato possibile salvare la pizza nel sistema");

        // Crea l'ordine pizza e mettilo in una collection
        OrdinePizza op = new OrdinePizza();
        op.setQuantita(2);
        op.setPizza(savedPizza); // Collega l'ordine pizza alla pizza creata
        Collection<OrdinePizza> pizzeOrdinate = new ArrayList<>();
        pizzeOrdinate.add(op);

        // Crea l'ordine
        Ordine ordine = new Ordine();
        ordine.setCodice("A75");
        ordine.setPizzeOrdinate(pizzeOrdinate); // Collega l'ordine all'ordine pizza

        Ordine savedOrdine = ordineRepository.save(ordine);

        // Verifiche
        assertNotNull(savedOrdine, "L'ordine non è stato ritornato dopo esser stato salvato. Null è il ritorno");
        assertEquals("A75", savedOrdine.getCodice(), "Il codice dell'ordine da salvare e quello ritornato non coincidono");
        OrdinePizza ordinePizzaSalvato = savedOrdine.getPizzeOrdinate()
                .stream()
                .findFirst()
                .orElse(null);
        assertNotNull(ordinePizzaSalvato, "L'ordine pizza non è stato salvato o collegato con l'ordine");
        assertNotNull(ordinePizzaSalvato.getId(), "Errore nella generazione dell'id per ordine pizza"); // <-- qui da l'errore
        assertEquals(2, ordinePizzaSalvato.getQuantita(), "La quantità dell'ordine da salvare e quello effettivamente salvato non coincidono");
        assertNotNull(ordinePizzaSalvato.getPizza(), "L'ordine pizza non contiene alcuna pizza all'interno");
        assertEquals("Margherita", ordinePizzaSalvato.getPizza().getNome(), "La pizza nell'ordine pizza non si chiama margherita");
    }


    @Test
    public void testCancellaOrdine() {
        // Crea l'ordine e le entità correlate (Pizza e OrdinePizza)
        Pizza margherita = new Pizza();
        margherita.setNome("Margherita");
        margherita.setDescrizione("Molto buona");
        margherita.setPrezzo(9.0);
        Pizza savedPizza = pizzaRepository.saveAndFlush(margherita);
        assertNotNull(savedPizza, "SETUP FALLITO: non è stato possibile salvare la pizza nel sistema");
        Long idPizza = savedPizza.getIdPizza(); // Prendi l'id della pizza per fare il findById dopo e controllare che non sia stata rimossa
        assertNotNull(idPizza, "SETUP FALLITO: id della pizza salvata è null");


        OrdinePizza op = new OrdinePizza();
        op.setQuantita(2);
        op.setPizza(savedPizza);
        Collection<OrdinePizza> pizzeOrdinate = new ArrayList<>();
        pizzeOrdinate.add(op);

        Ordine ordine = new Ordine();
        ordine.setCodice("A45");
        ordine.setPizzeOrdinate(pizzeOrdinate);

        Ordine savedOrdine = ordineRepository.save(ordine);
        assertNotNull(savedOrdine, "SETUP FALLITO: L'ordine non è stato creato correttamente");

        // Prova a cancellare l'ordine
        ordineRepository.deleteById("A45");

        // Verifiche
        assertNull(ordineRepository.findById("A45").orElse(null), "L'ordine non è stato eliminato correttamente");
        assertNotNull(pizzaRepository.findById(idPizza), "La pizza è stata rimossa assieme all'ordine. Solo l'ordine deve esser cancellato");
    }

    @Test
    public void testCercaOrdine() {
        // Crea l'ordine e le entità correlate (Pizza e OrdinePizza)
        Pizza margherita = new Pizza();
        margherita.setNome("Margherita");
        margherita.setDescrizione("Molto buona");
        margherita.setPrezzo(9.0);
        Pizza savedPizza = pizzaRepository.saveAndFlush(margherita);
        assertNotNull(savedPizza, "SETUP FALLITO: non è stato possibile salvare la pizza nel sistema");

        OrdinePizza op = new OrdinePizza();
        op.setQuantita(2);
        op.setPizza(savedPizza);
        Collection<OrdinePizza> pizzeOrdinate = new ArrayList<>();
        pizzeOrdinate.add(op);

        Ordine ordine = new Ordine();
        ordine.setCodice("QI5");
        ordine.setPizzeOrdinate(pizzeOrdinate);

        ordineRepository.save(ordine);

        // Verifiche
        Ordine trovato = ordineRepository.findById("QI5").orElse(null);
        assertNotNull(trovato, "La ricerca della pizza non è andata a buon fine");

        OrdinePizza ordinePizza = trovato.getPizzeOrdinate()
                .stream()
                .findFirst()
                .orElse(null);
        assertNotNull(ordinePizza, "L'ordine non ritorna correttamente la lista delle pizze ordinate (OrdinePizza)");

        Pizza pizzaOrdinata = ordinePizza.getPizza();
        assertNotNull(pizzaOrdinata, "L'ordine ritorna una lista di OrdinePizza, ma gli OrdinePizza hanno Pizza a null");
        assertNotNull(pizzaRepository.findById(pizzaOrdinata.getIdPizza()).orElse(null), "L'ordine contiene delle pizze inesistenti nel sistema");

    }
}
