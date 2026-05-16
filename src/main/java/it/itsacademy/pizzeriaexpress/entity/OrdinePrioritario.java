package it.itsacademy.pizzeriaexpress.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("prioritario") // a quale valore della colonna "tipo_ordine" corrisponde questa entità
@SuperBuilder // Necessario per permettere l'inizializzazione degli attributi nelle sottoclassi
public class OrdinePrioritario extends Ordine {
    @Column(nullable = true)
    private Double sovrapprezzo;
}
