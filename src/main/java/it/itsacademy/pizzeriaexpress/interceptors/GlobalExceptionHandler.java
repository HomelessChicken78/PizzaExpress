package it.itsacademy.pizzeriaexpress.interceptors;

import it.itsacademy.pizzeriaexpress.dto.ErrorResponseDTO;
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
    public ResponseEntity<ErrorResponseDTO> error400Handler(BadRequestException err400) {
        return new ResponseEntity<>(new ErrorResponseDTO(err400.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDTO> error404Handler(NotFoundException err404) {
       return new ResponseEntity<>(new ErrorResponseDTO(err404.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDTO> error409Handler(ConflictException err409) {
        return new ResponseEntity<>(new ErrorResponseDTO(err409.getMessage()), HttpStatus.CONFLICT);
    }
}
