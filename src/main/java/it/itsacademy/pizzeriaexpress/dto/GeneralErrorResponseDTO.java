package it.itsacademy.pizzeriaexpress.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneralErrorResponseDTO {
    private String message;

    private LocalDate timestamp = LocalDate.now();

    private Integer status = 400;

    public GeneralErrorResponseDTO(String message) {
        this.message = message;
    }

    public GeneralErrorResponseDTO(String message, Integer status) {
        this.message = message;
        this.status = status;
    }
}