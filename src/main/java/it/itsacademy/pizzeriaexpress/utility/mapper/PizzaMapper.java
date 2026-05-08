package it.itsacademy.pizzeriaexpress.utility.mapper;

import it.itsacademy.pizzeriaexpress.dto.PizzaDTO;
import it.itsacademy.pizzeriaexpress.entity.Pizza;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface PizzaMapper {
    public Pizza toEntity(PizzaDTO dto);

    public PizzaDTO toDTO(Pizza entity);

    public Collection<PizzaDTO> toDTO(Collection<Pizza> entities);
}
