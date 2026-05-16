package it.itsacademy.pizzeriaexpress.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Tutte le sotto-classi vengono mappate in una singola tabella
@DiscriminatorColumn(name = "tipo_ordine", // definisce quale colonna a livello di db permette di distinguere a
                                            // quale classe java corrisponde
        discriminatorType = DiscriminatorType.STRING)
@SuperBuilder // Necessario per permettere l'inizializzazione degli attributi nelle sottoclassi
public class Ordine {
    @Id
    private String codice;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}) // Quando l'id è una stringa oppure è un Long/Integer ma non ha autoincrement, jpa usa Merge non persist
    @JoinColumn(name = "fk_pizza_ord", nullable = false)
    private Collection<OrdinePizza> pizzeOrdinate;

    @ManyToOne
    @JoinColumn(name = "fk_rider")
    private Rider rider;
}
