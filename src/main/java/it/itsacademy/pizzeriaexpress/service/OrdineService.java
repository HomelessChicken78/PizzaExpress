package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.OrdineDTO;
import it.itsacademy.pizzeriaexpress.dto.RegistraOrdineDTO;

import java.util.Collection;

public interface OrdineService {
    public OrdineDTO creaOrdine(Long idCliente, RegistraOrdineDTO nuovoOrdine);

    public OrdineDTO cercaOrdine(Long idCliente, String codiceOrdine);

    public Collection<OrdineDTO> tuttiGliOrdini();

    public OrdineDTO aggiungiPizza(Long idCliente, String codiceOrdine, Long idPizza, Integer quantita);

    OrdineDTO cambiaRider(Long idCliente, String codiceOrdine, Long idRider);
}
