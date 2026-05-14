package it.itsacademy.pizzeriaexpress.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistraClienteDTO {
    private Long idCliente;

    @NotNull(message = "Il nome è obbligatorio")
    @NotBlank(message = "Il nome è obbligatorio")
    private String nome;

    private String indirizzo;

    @Pattern(regexp = "(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}", message = "Il numero di telefono non è valido")
    private String telefono;

    @NotNull(message = "La lista di ordini è obbligatoria")
    @Valid
    private Collection<RegistraOrdineDTO> ordini;
}
