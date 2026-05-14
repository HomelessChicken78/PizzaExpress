package it.itsacademy.pizzeriaexpress.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistraOrdineDTO {
    @NotNull(message = "Il codice è obbligatorio")
    @NotBlank(message = "Il codice è obbligatorio")
    private String codice;

    @Valid
    @NotNull(message = "La lista di pizze ordinate non può esser vuota")
    private Collection<AggiungiPizzaAllOrdineDTO> pizzeOrdinate;

    @Valid
    private Long rider;
}