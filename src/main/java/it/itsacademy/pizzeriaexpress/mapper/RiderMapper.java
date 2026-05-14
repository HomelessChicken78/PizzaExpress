package it.itsacademy.pizzeriaexpress.mapper;

import it.itsacademy.pizzeriaexpress.dto.RiderDTO;
import it.itsacademy.pizzeriaexpress.entity.Rider;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface RiderMapper {
    Rider toEntity(RiderDTO dto);

    RiderDTO toDTO(Rider entity);

    Collection<RiderDTO> toDTO(Collection<Rider> entities);
}
