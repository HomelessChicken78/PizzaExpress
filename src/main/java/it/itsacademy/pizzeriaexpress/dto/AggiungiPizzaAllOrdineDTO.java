package it.itsacademy.pizzeriaexpress.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AggiungiPizzaAllOrdineDTO {
    @NotNull(message = "L'ordine deve avere un idPizza collegato")
    private Long idPizza;

    @Positive(message = "Il valore per la quantità deve esser positivo")
    @NotNull(message = "La quantità è obbligatoria")
    private Integer quantita;
}