package it.itsacademy.pizzeriaexpress.mapper;

import it.itsacademy.pizzeriaexpress.dto.AggiungiPizzaAllOrdineDTO;
import it.itsacademy.pizzeriaexpress.dto.OrdinePizzaDTO;
import it.itsacademy.pizzeriaexpress.entity.OrdinePizza;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PizzaMapper.class})
public interface OrdinePizzaMapper {
    @Mapping(target = "id", ignore = true)
    OrdinePizza toEntity(OrdinePizzaDTO dto);

    OrdinePizza toEntity(AggiungiPizzaAllOrdineDTO dto);

    OrdinePizzaDTO toDTO(OrdinePizza entity);
}