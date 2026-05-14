package it.itsacademy.pizzeriaexpress.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PizzaDTO {
    private Long idPizza;

    @NotNull(message = "Il nome è obbligatorio")
    @NotBlank(message = "Il nome è obbligatorio")
    private String nome;

    @NotNull(message = "La descrizione è obbligatoria")
    @NotBlank(message = "La descrizione è obbligatoria")
    private String descrizione;

    @NotNull(message = "Il prezzo è obbligatorio")
    @Positive(message = "Il prezzo deve essere un numero positivo")
    private Double prezzo;
}
