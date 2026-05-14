package it.itsacademy.pizzeriaexpress.repository;

import it.itsacademy.pizzeriaexpress.entity.Pizza;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PizzaRepositoryTest {

    @Autowired
    private PizzaRepository pizzaRepository;

    // Permette di interagire con il contesto di persistenza
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void datiIniziali() {
        // Resetta l'auto increment attraverso una query nativa
        entityManager.createNativeQuery("ALTER TABLE pizza AUTO_INCREMENT = 1").executeUpdate();

        // Aggiungi da capo le pizze
        Pizza pizza1 = new Pizza(null, "Diavola", "Pomodoro, mozzarella e salame piccante", 8.50);
        Pizza pizza2 = new Pizza(null, "Quattro Formaggi", "Mozzarella, gorgonzola, fontina e parmigiano", 9.50);
        Pizza pizza3 = new Pizza(null, "Capricciosa", "Pomodoro, mozzarella, prosciutto cotto, funghi, carciofi e olive", 9.00);
        Pizza pizza4 = new Pizza(null, "Prosciutto e Funghi", "Pomodoro, mozzarella, prosciutto cotto e funghi", 8.00);

        pizzaRepository.save(pizza1);
        pizzaRepository.save(pizza2);
        pizzaRepository.save(pizza3);
        pizzaRepository.save(pizza4);
    }

    @Test
    public void testPizzaSave() {
        Pizza pizza = new Pizza();
        pizza.setNome("Margherita");
        pizza.setDescrizione("Pomodoro, Mozzarella, Basilico");
        pizza.setPrezzo(5.99);

        Pizza savedPizza = pizzaRepository.save(pizza);
        assertNotNull(savedPizza);
        assertEquals("Margherita",savedPizza.getNome());
    }

    @Test
    public void testPizzaCancella() {

        assertTrue(pizzaRepository.findById(1L).isPresent());
        pizzaRepository.deleteById(1L);
        assertTrue(pizzaRepository.findById(1L).isEmpty());
    }

    @Test
    public void testCercaPizza() {
        Pizza pizza = pizzaRepository.findById(1L).orElse(null);

        assertNotNull(pizza);
        assertEquals("Diavola", pizza.getNome());
    }
}
