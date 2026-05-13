package it.itsacademy.pizzeriaexpress.utility.mapper;

import it.itsacademy.pizzeriaexpress.dto.ClienteDTO;
import it.itsacademy.pizzeriaexpress.entity.Cliente;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring", uses = OrdineMapper.class)
public interface ClienteMapper {
    Cliente toEntity(ClienteDTO dto);

    ClienteDTO toDTO(Cliente entity);

    Collection<ClienteDTO> toDTO(Collection<Cliente> entities);
}
