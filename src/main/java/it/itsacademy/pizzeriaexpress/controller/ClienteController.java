package it.itsacademy.pizzeriaexpress.controller;

import it.itsacademy.pizzeriaexpress.dto.ClienteDTO;
import it.itsacademy.pizzeriaexpress.dto.RegistraClienteDTO;
import it.itsacademy.pizzeriaexpress.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path = "/clienti")
public class ClienteController {
    private static final String json = "application/json";

    @Autowired
    private ClienteService clienteService;

    @PostMapping(consumes = json, produces = json)
    public ClienteDTO registraCliente(@RequestBody RegistraClienteDTO nuovoCliente) {
        return clienteService.registraCliente(nuovoCliente);
    }

    @GetMapping(path = "/{idCliente}", produces = json)
    public ClienteDTO cercaCliente(@PathVariable Long idCliente) {
        return clienteService.cercaCliente(idCliente);
    }

    @GetMapping(produces = json)
    public Collection<ClienteDTO> tuttiIClienti() {
        return clienteService.cercaTuttiIClienti();
    }
}
