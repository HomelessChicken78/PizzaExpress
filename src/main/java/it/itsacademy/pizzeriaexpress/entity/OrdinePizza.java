package it.itsacademy.pizzeriaexpress.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ordine_pizza")
public class OrdinePizza {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private Integer quantita;

    @ManyToOne
    @JoinColumn(name = "fk_pizza")
    private Pizza pizza;
}
