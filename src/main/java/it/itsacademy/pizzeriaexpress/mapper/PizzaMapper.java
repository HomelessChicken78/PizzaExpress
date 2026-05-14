package it.itsacademy.pizzeriaexpress.mapper;

import it.itsacademy.pizzeriaexpress.dto.PizzaDTO;
import it.itsacademy.pizzeriaexpress.entity.Pizza;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface PizzaMapper {
    Pizza toEntity(PizzaDTO dto);

    PizzaDTO toDTO(Pizza entity);

    Collection<PizzaDTO> toDTO(Collection<Pizza> entities);
}
