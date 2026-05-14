package it.itsacademy.pizzeriaexpress.interceptors;

import it.itsacademy.pizzeriaexpress.dto.GeneralErrorResponseDTO;
import it.itsacademy.pizzeriaexpress.dto.ValidationErrorResponseDTO;
import it.itsacademy.pizzeriaexpress.exception.BadRequestException;
import it.itsacademy.pizzeriaexpress.exception.ConflictException;
import it.itsacademy.pizzeriaexpress.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<GeneralErrorResponseDTO> error400Handler(BadRequestException err400) {
        return ResponseEntity
                .badRequest()
                .body(new GeneralErrorResponseDTO(err400.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<GeneralErrorResponseDTO> error404Handler(NotFoundException err404) {
       return ResponseEntity
               .status(HttpStatus.NOT_FOUND) // Usando .notFound non posso mettere un body con .body perché non ritorna
                                            // un BodyBuilder (usato per il body) ma un HeadersBuilder (usato per gli headers)
               .body(new GeneralErrorResponseDTO(err404.getMessage(), 404));
    }

    @ExceptionHandler
    public ResponseEntity<GeneralErrorResponseDTO> error409Handler(ConflictException err409) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // Non esiste .conflict
                .body(new GeneralErrorResponseDTO(err409.getMessage(), 409));
    }

    @ExceptionHandler
    public ResponseEntity<ValidationErrorResponseDTO> errorValidationHandler(MethodArgumentNotValidException exceptionRaised) {
        ValidationErrorResponseDTO responseDTO = new ValidationErrorResponseDTO(
                exceptionRaised.getFieldErrors()
                        .stream()
                        .collect(Collectors.toMap(
                                err -> err.getField(),
                                err -> err.getDefaultMessage(),
                                (existing, replacement) -> existing // Prendi sempre quello già esistente in caso field duplicati
                            ))
        );

        return ResponseEntity
                .badRequest()
                .body(responseDTO);
    }
}
