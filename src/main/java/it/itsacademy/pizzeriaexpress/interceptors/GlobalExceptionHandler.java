package it.itsacademy.pizzeriaexpress.interceptors;

import it.itsacademy.pizzeriaexpress.dto.GeneralErrorResponseDTO;
import it.itsacademy.pizzeriaexpress.exception.BadRequestException;
import it.itsacademy.pizzeriaexpress.exception.ConflictException;
import it.itsacademy.pizzeriaexpress.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<GeneralErrorResponseDTO> error400Handler(BadRequestException err400) {
        return new ResponseEntity<>(new GeneralErrorResponseDTO(err400.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<GeneralErrorResponseDTO> error404Handler(NotFoundException err404) {
       return new ResponseEntity<>(new GeneralErrorResponseDTO(err404.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<GeneralErrorResponseDTO> error409Handler(ConflictException err409) {
        return new ResponseEntity<>(new GeneralErrorResponseDTO(err409.getMessage()), HttpStatus.CONFLICT);
    }
}
