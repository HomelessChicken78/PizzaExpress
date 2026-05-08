package it.itsacademy.pizzeriaexpress.utility.mapper;

import it.itsacademy.pizzeriaexpress.dto.OrdinePizzaDTO;
import it.itsacademy.pizzeriaexpress.entity.OrdinePizza;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PizzaMapper.class})
public interface OrdinePizzaMapper {
    @Mapping(target = "id", ignore = true)
    OrdinePizza toEntity(OrdinePizzaDTO dto);

    OrdinePizzaDTO toDTO(OrdinePizza entity);
}