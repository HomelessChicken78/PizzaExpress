package it.itsacademy.pizzeriaexpress.dto;

import it.itsacademy.pizzeriaexpress.entity.Rider;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdineDTO {
    private String codice;

    private Collection<OrdinePizzaDTO> pizzeOrdinate;

    private Rider rider;
}
