package it.itsacademy.pizzeriaexpress.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO {
    private String message;

    private LocalDate timestamp = LocalDate.now();

    private Integer status = 400;

    public ErrorResponseDTO(String message) {
        this.message = message;
    }

    public ErrorResponseDTO(String message, Integer status) {
        this.message = message;
        this.status = status;
    }
}