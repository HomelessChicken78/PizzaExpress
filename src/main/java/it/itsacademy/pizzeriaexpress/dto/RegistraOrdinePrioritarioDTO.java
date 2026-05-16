package it.itsacademy.pizzeriaexpress.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
@SuperBuilder // Necessario per permettere l'inizializzazione degli attributi nelle sottoclassi
public class RegistraOrdinePrioritarioDTO extends RegistraOrdineDTO{
    @NotNull(message = "Il valore per il sovrapprezzo è obbligatorio")
    @Min(value = 0, message = "Il valore per il sovrapprezzo non può essere negativo")
    private Double sovrapprezzo;
}
