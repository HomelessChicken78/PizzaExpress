package it.itsacademy.pizzeriaexpress.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCliente;

    @Column(nullable = false)
    private String nome;

    private String indirizzo;

    private String telefono;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_ordini")
    @Column(nullable = false)
    private Collection<Ordine> ordini;
}
