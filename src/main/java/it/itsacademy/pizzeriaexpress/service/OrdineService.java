package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.OrdineDTO;
import it.itsacademy.pizzeriaexpress.dto.OrdinePrioritarioDTO;
import it.itsacademy.pizzeriaexpress.dto.RegistraOrdineDTO;
import it.itsacademy.pizzeriaexpress.dto.RegistraOrdinePrioritarioDTO;

import java.util.Collection;

public interface OrdineService {
    OrdineDTO creaOrdine(Long idCliente, RegistraOrdineDTO nuovoOrdine);

    OrdinePrioritarioDTO creaOrdinePrioritario(Long idCliente, RegistraOrdinePrioritarioDTO nuovoOrdine);

    OrdineDTO cercaOrdine(Long idCliente, String codiceOrdine);

    Collection<OrdineDTO> tuttiGliOrdini();

    Collection<OrdineDTO> tuttiGliOrdiniNonPrioritari();

    Collection<OrdinePrioritarioDTO> tuttiGliOrdiniPrioritari();

    OrdineDTO aggiungiPizza(Long idCliente, String codiceOrdine, Long idPizza, Integer quantita);

    OrdineDTO cambiaRider(Long idCliente, String codiceOrdine, Long idRider);
}
