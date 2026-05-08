package it.itsacademy.pizzeriaexpress.utility.mapper;

import it.itsacademy.pizzeriaexpress.dto.OrdineDTO;
import it.itsacademy.pizzeriaexpress.entity.Ordine;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring", uses = {OrdinePizzaMapper.class})
public interface OrdineMapper {
    Ordine toEntity(OrdineDTO dto);

    OrdineDTO toDTO(Ordine entity);

    Collection<OrdineDTO> toDTO(Collection<Ordine> entities);
}
