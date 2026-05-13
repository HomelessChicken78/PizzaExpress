package it.itsacademy.pizzeriaexpress.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AggiungiPizzaAllOrdineDTO {
    private Long idPizza;
    private Integer quantita;
}