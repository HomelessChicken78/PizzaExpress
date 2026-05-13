package it.itsacademy.pizzeriaexpress.controller;

import it.itsacademy.pizzeriaexpress.dto.ClienteDTO;
import it.itsacademy.pizzeriaexpress.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/clienti")
public class ClienteController {
    private static final String json = "application/json";

    @Autowired
    private ClienteService clienteService;

    @PostMapping(consumes = json, produces = json)
    public ClienteDTO registraCliente(@RequestBody ClienteDTO nuovoCliente) {
        return clienteService.registraCliente(nuovoCliente);
    }
}
