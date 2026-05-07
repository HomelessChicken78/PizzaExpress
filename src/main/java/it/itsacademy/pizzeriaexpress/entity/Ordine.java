package it.itsacademy.pizzeriaexpress.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Tutte le sotto-classi vengono mappate in una singola tabella
@DiscriminatorColumn(name = "tipo_ordine", // definisce quale colonna a livello di db permette di distinguere a
                                            // quale classe java corrisponde
        discriminatorType = DiscriminatorType.STRING)
public class Ordine {
    @Id
    private String codice;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "fk_pizza", nullable = false)
    private Collection<ordinepizza> pizzeOrdinate;

    @ManyToOne
    @JoinColumn(name = "fk_rider")
    private Rider rider;
}
