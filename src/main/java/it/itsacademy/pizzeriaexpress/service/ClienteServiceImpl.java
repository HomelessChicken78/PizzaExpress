package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.ClienteDTO;
import it.itsacademy.pizzeriaexpress.dto.RegistraClienteDTO;
import it.itsacademy.pizzeriaexpress.dto.RegistraOrdineDTO;
import it.itsacademy.pizzeriaexpress.entity.Cliente;
import it.itsacademy.pizzeriaexpress.exception.BadRequestException;
import it.itsacademy.pizzeriaexpress.exception.NotFoundException;
import it.itsacademy.pizzeriaexpress.repository.ClienteRepository;
import it.itsacademy.pizzeriaexpress.utility.mapper.ClienteMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {
    @Autowired
    ClienteRepository repositoryCliente;

    @Autowired
    OrdineService serviceOrdine;

    @Autowired
    ClienteMapper mapper;

    @Override
    public ClienteDTO registraCliente(RegistraClienteDTO nuovoCliente) {
        // Controlla che venga registrato almeno un ordine
        if(nuovoCliente.getOrdini() == null || nuovoCliente.getOrdini().isEmpty())
            throw new BadRequestException("Un cliente deve essere registrato con almeno già un ordine");

        nuovoCliente.setIdCliente(null); // Ignora l'id passato dal dto

        // Creiamo l'entità Cliente senza ordini e salviamola: questo ci consente di ottenere l'id del cliente.
        // Possiamo usare il mapper per convertire il DTO di scrittura in una entity
        Cliente clienteEntity = mapper.registraToEntity(nuovoCliente);
        clienteEntity.setOrdini(new ArrayList<>()); // Ignoriamo gli ordini collegati dal mapper in quanto non sono completi
                                                    // (Non hanno un idCliente e una lista di pizze ordinate)
        Cliente saved = repositoryCliente.saveAndFlush(clienteEntity); // Salva per generare l'id con auto-increment.
        // Dobbiamo dire ad Hibernate di salvare le modifiche subito per generare l'id

        // Possiamo chiamare il service degli ordini
        // così che lui si occupi di creare i singoli ordini e di collegarli alle pizze
        for (RegistraOrdineDTO ordineDTO : nuovoCliente.getOrdini()) {
            // Usiamo l'id appena generato
            serviceOrdine.creaOrdine(saved.getIdCliente(), ordineDTO);
        }

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
