package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.OrdineDTO;
import it.itsacademy.pizzeriaexpress.repository.OrdineRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class OrdineServiceImpl implements OrdineService {
    @Autowired
    OrdineRepository repositoryOrdine;

    /*@Autowired
    OrdineMapper mapper;*/

    @Override
    public OrdineDTO creaOrdine(OrdineDTO nuovoOrdine) {
        return null;
    }

    @Override
    public OrdineDTO modificaOrdine(OrdineDTO ordineCambiato) {
        return null;
    }

    @Override
    public OrdineDTO cercaOrdine(String codiceOrdine) {
        return null;
    }

    @Override
    public Collection<OrdineDTO> tuttiGliOrdini() {
        return List.of();
    }

    @Override
    public OrdineDTO aggiungiPizza(String codiceOrdine, Long idPizza) {
        return null;
    }
}
