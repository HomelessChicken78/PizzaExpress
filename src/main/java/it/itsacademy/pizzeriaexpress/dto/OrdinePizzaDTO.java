package it.itsacademy.pizzeriaexpress.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdinePizzaDTO {
    private Integer quantita;

    private PizzaDTO pizza;
}
