package it.itsacademy.pizzeriaexpress.interceptors;

import it.itsacademy.pizzeriaexpress.dto.ErrorDTO;
import it.itsacademy.pizzeriaexpress.exception.BadRequestException;
import it.itsacademy.pizzeriaexpress.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorDTO> error400Handler(BadRequestException err400) {
        return new ResponseEntity<>(new ErrorDTO(err400.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> error404Handler(NotFoundException err404) {
       return new ResponseEntity<>(new ErrorDTO(err404.getMessage()), HttpStatus.NOT_FOUND);
    }
}
