package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.ClienteDTO;
import it.itsacademy.pizzeriaexpress.entity.Cliente;
import it.itsacademy.pizzeriaexpress.exception.NotFoundException;
import it.itsacademy.pizzeriaexpress.repository.ClienteRepository;
import it.itsacademy.pizzeriaexpress.utility.mapper.ClienteMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

public class ClienteServiceImpl implements ClienteService {
    @Autowired
    ClienteRepository repositoryCliente;

    @Autowired
    ClienteMapper mapper;

    @Override
    public ClienteDTO registraCliente(ClienteDTO nuovoCliente) {
        Cliente saved = repositoryCliente.save(mapper.toEntity(nuovoCliente));

        return mapper.toDTO(saved);
    }

    @Override
    public ClienteDTO cercaCliente(Long idCliente) {
        Cliente trovato = repositoryCliente.findById(idCliente).orElseThrow(
                () -> new NotFoundException("Non è stato possibile trovare un cliente con id " + idCliente)
        );

        return mapper.toDTO(trovato);
    }

    @Override
    public Collection<ClienteDTO> cercaTuttiIClienti() {
        return mapper.toDTO(repositoryCliente.findAll());
    }
}
