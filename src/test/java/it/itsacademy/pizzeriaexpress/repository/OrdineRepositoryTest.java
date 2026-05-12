package it.itsacademy.pizzeriaexpress.repository;

import it.itsacademy.pizzeriaexpress.entity.Ordine;
import it.itsacademy.pizzeriaexpress.entity.OrdinePizza;
import it.itsacademy.pizzeriaexpress.entity.Pizza;
import it.itsacademy.pizzeriaexpress.entity.Rider;
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
    public void testRiderSave() {
        Pizza margherita = new Pizza();
        margherita.setNome("Margherita");
        margherita.setDescrizione("Molto buona");
        margherita.setPrezzo(9.0);
        Pizza savedPizza = pizzaRepository.saveAndFlush(margherita);
        assertNotNull(savedPizza, "SETUP FALLITO: non è stato possibile salvare la pizza nel sistema");

        OrdinePizza op = new OrdinePizza();
        op.setQuantita(2);
        op.setPizza(margherita);
        Collection<OrdinePizza> pizzeOrdinate = new ArrayList<>();
        pizzeOrdinate.add(op);

        Ordine ordine = new Ordine();
        ordine.setCodice("A75");
        ordine.setPizzeOrdinate(pizzeOrdinate);

        Ordine savedOrdine = ordineRepository.save(ordine);
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
}
