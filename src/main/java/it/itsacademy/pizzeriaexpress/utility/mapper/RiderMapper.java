package it.itsacademy.pizzeriaexpress.utility.mapper;

import it.itsacademy.pizzeriaexpress.dto.RiderDTO;
import it.itsacademy.pizzeriaexpress.entity.Rider;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface RiderMapper {
    public Rider toEntity(RiderDTO dto);

    public RiderDTO toDTO(Rider entity);

    public Collection<RiderDTO> toDTO(Collection<Rider> entities);
}
