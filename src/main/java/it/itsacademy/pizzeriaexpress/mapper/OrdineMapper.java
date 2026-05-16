package it.itsacademy.pizzeriaexpress.mapper;

import it.itsacademy.pizzeriaexpress.dto.OrdineDTO;
import it.itsacademy.pizzeriaexpress.dto.OrdinePrioritarioDTO;
import it.itsacademy.pizzeriaexpress.dto.RegistraOrdineDTO;
import it.itsacademy.pizzeriaexpress.dto.RegistraOrdinePrioritarioDTO;
import it.itsacademy.pizzeriaexpress.entity.Ordine;
import it.itsacademy.pizzeriaexpress.entity.OrdinePrioritario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", uses = {OrdinePizzaMapper.class})
public interface OrdineMapper {
    Ordine toEntity(OrdineDTO dto);

    OrdinePrioritario toEntityPrio(OrdinePrioritarioDTO dto);

    @Mapping(target = "rider", ignore = true)
    OrdineDTO toCompleteOrdine(RegistraOrdineDTO nuovoOrdine);

    @Mapping(target = "rider", ignore = true)
    OrdinePrioritarioDTO toCompleteOrdine(RegistraOrdinePrioritarioDTO nuovoOrdine);

    OrdineDTO toDTO(Ordine entity);

    OrdinePrioritarioDTO toDTO(OrdinePrioritario entity);

    Collection<OrdineDTO> toDTO(Collection<Ordine> entities);

    Collection<OrdinePrioritarioDTO> toDTO(List<OrdinePrioritario> entities);
}
