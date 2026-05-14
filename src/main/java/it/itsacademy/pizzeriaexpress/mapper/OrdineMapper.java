package it.itsacademy.pizzeriaexpress.mapper;

import it.itsacademy.pizzeriaexpress.dto.OrdineDTO;
import it.itsacademy.pizzeriaexpress.dto.RegistraOrdineDTO;
import it.itsacademy.pizzeriaexpress.entity.Ordine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;

@Mapper(componentModel = "spring", uses = {OrdinePizzaMapper.class})
public interface OrdineMapper {
    Ordine toEntity(OrdineDTO dto);

    @Mapping(target = "rider", ignore = true)
    OrdineDTO toCompleteOrdine(RegistraOrdineDTO nuovoOrdine);

    OrdineDTO toDTO(Ordine entity);

    Collection<OrdineDTO> toDTO(Collection<Ordine> entities);
}
