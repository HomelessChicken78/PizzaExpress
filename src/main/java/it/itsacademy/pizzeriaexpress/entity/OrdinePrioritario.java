package it.itsacademy.pizzeriaexpress.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("prioritario") // a quale valore della colonna "tipo_ordine" corrisponde questa entità
public class OrdinePrioritario extends Ordine {
    @Column(nullable = false)
    private Double sovraprezzo;
}
