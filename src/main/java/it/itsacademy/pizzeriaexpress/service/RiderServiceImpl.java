package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.RiderDTO;
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
        return null;
    }

    @Override
    public RiderDTO licensiaRider(Long idRider) {
        return null;
    }

    @Override
    public RiderDTO cercaRider(Long idRider) {
        return null;
    }

    @Override
    public Collection<RiderDTO> tuttiIRider() {
        return List.of();
    }
}
