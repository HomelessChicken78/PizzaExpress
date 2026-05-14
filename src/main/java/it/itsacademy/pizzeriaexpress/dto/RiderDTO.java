package it.itsacademy.pizzeriaexpress.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RiderDTO {
    private Long idRider;

    @NotNull(message = "Il nome è obbligatorio")
    @NotBlank(message = "Il nome è obbligatorio")
    private String nome;
}
