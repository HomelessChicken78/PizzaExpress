package it.itsacademy.pizzeriaexpress.dto;

import lombok.*;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO {
    private Long idCliente;

    private String nome;

    private String indirizzo;

    private String telefono;

    private Collection<OrdineDTO> ordini;
}
