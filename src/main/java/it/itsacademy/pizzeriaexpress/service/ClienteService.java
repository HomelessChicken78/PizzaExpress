package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.ClienteDTO;

import java.util.Collection;

public interface ClienteService {
    ClienteDTO registraCliente(ClienteDTO nuovoCliente);

    ClienteDTO cercaCliente(Long idCliente);

    Collection<ClienteDTO> cercaTuttiIClienti();
}
