package it.itsacademy.pizzeriaexpress.dto;

import lombok.*;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdineDTO {
    private String codice;

    private Collection<OrdinePizzaDTO> pizzeOrdinate;

    private RiderDTO rider;
}
