package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.RiderDTO;
import it.itsacademy.pizzeriaexpress.entity.Rider;
import it.itsacademy.pizzeriaexpress.repository.RiderRepository;
import it.itsacademy.pizzeriaexpress.mapper.RiderMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Transactional
public class RiderServiceImpl implements RiderService {
    @Autowired
    RiderRepository repositoryRider;

    @Autowired
    RiderMapper mapper;

    @Override
    public RiderDTO registraRider(RiderDTO nuovoRider) {
        nuovoRider.setIdRider(null); // Ignora l'id passato dal json

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
        Rider trovato = repositoryRider.findByIdOrThrow(idRider);

        return mapper.toDTO(trovato);
    }

    @Override
    public Collection<RiderDTO> tuttiIRider() {
        return mapper.toDTO(repositoryRider.findAll());
    }
}
