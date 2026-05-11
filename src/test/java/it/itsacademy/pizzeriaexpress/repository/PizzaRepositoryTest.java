package it.itsacademy.pizzeriaexpress.repository;

import it.itsacademy.pizzeriaexpress.entity.Pizza;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.mockito.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PizzaRepositoryTest {

    @Autowired
    private PizzaRepository pizzaRepository;

    @BeforeEach
    public void datiIniziali() {
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
        System.out.println(pizzaRepository.findAll()); // auto-increment non viene resettato tra i test
        Pizza pizza = new Pizza();
        pizza.setNome("Margherita");
        pizza.setDescrizione("Pomodoro, Mozzarella, Basilico");
        pizza.setPrezzo(5.99);
        Pizza savedPizza = pizzaRepository.save(pizza);

        Long idNuovaPizza = savedPizza.getIdPizza();

        assertTrue(pizzaRepository.findById(idNuovaPizza).isPresent());
        pizzaRepository.deleteById(idNuovaPizza);
        assertTrue(pizzaRepository.findById(idNuovaPizza).isEmpty());
    }
}
