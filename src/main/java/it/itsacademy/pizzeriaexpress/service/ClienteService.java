package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.ClienteDTO;
import it.itsacademy.pizzeriaexpress.dto.RegistraClienteDTO;

import java.util.Collection;

public interface ClienteService {
    ClienteDTO registraCliente(RegistraClienteDTO nuovoCliente);

    ClienteDTO cercaCliente(Long idCliente);

    Collection<ClienteDTO> cercaTuttiIClienti();
}
