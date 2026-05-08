package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.RiderDTO;
import it.itsacademy.pizzeriaexpress.entity.Rider;
import it.itsacademy.pizzeriaexpress.exception.NotFoundException;
import it.itsacademy.pizzeriaexpress.repository.RiderRepository;
import it.itsacademy.pizzeriaexpress.utility.mapper.RiderMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

public class RiderServiceImpl implements RiderService {
    @Autowired
    RiderRepository repositoryRider;

    @Autowired
    RiderMapper mapper;

    @Override
    public RiderDTO registraRider(RiderDTO nuovoRider) {
        Rider saved = repositoryRider.save(mapper.toEntity(nuovoRider));

        return mapper.toDTO(saved);
    }

    @Override
    public RiderDTO licenziaRider(Long idRider) {
        RiderDTO trovato = cercaRider(idRider);

        repositoryRider.deleteById(idRider);

        return trovato;
    }

    @Override
    public RiderDTO cercaRider(Long idRider) {
        Rider trovato = repositoryRider.findById(idRider).orElseThrow(
                () -> new NotFoundException("Non è stato possibile trovare un Rider con id " + idRider)
        );

        return mapper.toDTO(trovato);
    }

    @Override
    public Collection<RiderDTO> tuttiIRider() {
        return mapper.toDTO(repositoryRider.findAll());
    }
}
