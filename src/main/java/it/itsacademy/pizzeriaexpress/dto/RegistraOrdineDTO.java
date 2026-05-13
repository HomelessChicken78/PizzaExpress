package it.itsacademy.pizzeriaexpress.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistraOrdineDTO {
    private String codice;

    private Collection<AggiungiPizzaAllOrdineDTO> pizzeOrdinate;

    private Long rider;
}