package it.itsacademy.pizzeriaexpress.controller;

import it.itsacademy.pizzeriaexpress.dto.RiderDTO;
import it.itsacademy.pizzeriaexpress.service.RiderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path = "/riders")
public class RiderController {
    private static final String json = "application/json";

    @Autowired
    private RiderService riderService;

    @PostMapping(consumes = json, produces = json)
    @ResponseStatus(HttpStatus.CREATED)
    public RiderDTO registraRider(@RequestBody @Valid RiderDTO nuovoRiderDTO) {
        return riderService.registraRider(nuovoRiderDTO);
    }

    @DeleteMapping(path = "/{idRider}", produces = json)
    public RiderDTO licenziaRider(@PathVariable Long idRider) {
        return riderService.licenziaRider(idRider);
    }

    @GetMapping(path = "/{idRider}", produces = json)
    public RiderDTO cercaRider(@PathVariable Long idRider) {
        return riderService.cercaRider(idRider);
    }

    @GetMapping(produces = json)
    public Collection<RiderDTO> cercaTuttiIRider() {
        return riderService.tuttiIRider();
    }
}