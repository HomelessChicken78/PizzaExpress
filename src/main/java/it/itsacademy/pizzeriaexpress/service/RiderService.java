package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.RiderDTO;

import java.util.Collection;

public interface RiderService {
    RiderDTO registraRider(RiderDTO nuovoRider);

    RiderDTO licensiaRider(Long idRider);

    RiderDTO cercaRider(Long idRider);

    Collection<RiderDTO> tuttiIRider();
}
