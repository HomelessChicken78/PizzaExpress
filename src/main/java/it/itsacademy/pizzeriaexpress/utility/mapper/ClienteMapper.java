package it.itsacademy.pizzeriaexpress.utility.mapper;

import it.itsacademy.pizzeriaexpress.dto.ClienteDTO;
import it.itsacademy.pizzeriaexpress.dto.RegistraClienteDTO;
import it.itsacademy.pizzeriaexpress.entity.Cliente;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring", uses = OrdineMapper.class)
public interface ClienteMapper {
    Cliente toEntity(ClienteDTO dto);

    ClienteDTO toDTO(Cliente entity);

    Collection<ClienteDTO> toDTO(Collection<Cliente> entities);

    // Questo crea un Cliente con una lista di Ordini.
    // Ogni ordine al suo interno avrà una collezione di OrdinePizza, ma le entità Pizza
    // all'interno degli OrdinePizza risulteranno incomplete perché il mapper non può
    // interrogare il database per risolvere l'idPizza contenuto in AggiungiPizzaAllOrdineDTO.
    Cliente registraToEntity(RegistraClienteDTO nuovoCliente);
}
