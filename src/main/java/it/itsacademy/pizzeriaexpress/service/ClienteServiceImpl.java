package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.ClienteDTO;
import it.itsacademy.pizzeriaexpress.entity.Cliente;
import it.itsacademy.pizzeriaexpress.exception.BadRequestException;
import it.itsacademy.pizzeriaexpress.exception.NotFoundException;
import it.itsacademy.pizzeriaexpress.repository.ClienteRepository;
import it.itsacademy.pizzeriaexpress.utility.mapper.ClienteMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {
    @Autowired
    ClienteRepository repositoryCliente;

    @Autowired
    ClienteMapper mapper;

    @Override
    public ClienteDTO registraCliente(ClienteDTO nuovoCliente) {
        // Controlla che venga registrato almeno un ordine
        if(nuovoCliente.getOrdini() == null || nuovoCliente.getOrdini().isEmpty())
            throw new BadRequestException("Un cliente deve essere registrato con almeno già un ordine");

        nuovoCliente.setIdCliente(null); // Ignora l'id passato dal dto

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
