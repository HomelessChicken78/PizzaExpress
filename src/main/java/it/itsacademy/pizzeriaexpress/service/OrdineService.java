package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.OrdineDTO;

import java.util.Collection;

public interface OrdineService {
    public OrdineDTO creaOrdine(Long idCliente, OrdineDTO nuovoOrdine);

    public OrdineDTO modificaOrdine(Long idCliente, String codiceOrdine, OrdineDTO ordineCambiato);

    public OrdineDTO cercaOrdine(Long idCliente, String codiceOrdine);

    public Collection<OrdineDTO> tuttiGliOrdini();

    public OrdineDTO aggiungiPizza(Long idCliente, String codiceOrdine, Long idPizza, Integer quantita);
}
