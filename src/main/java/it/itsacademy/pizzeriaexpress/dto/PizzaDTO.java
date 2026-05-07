package it.itsacademy.pizzeriaexpress.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PizzaDTO {
    private Long idPizza;
    private String nome;
    private String descrizione;
    private Double prezzo;
}
