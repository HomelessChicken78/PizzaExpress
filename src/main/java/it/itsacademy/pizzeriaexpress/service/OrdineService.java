package it.itsacademy.pizzeriaexpress.service;

import it.itsacademy.pizzeriaexpress.dto.OrdineDTO;

import java.util.Collection;

public interface OrdineService {
    public OrdineDTO creaOrdine(OrdineDTO nuovoOrdine);

    public OrdineDTO modificaOrdine(String codiceOrdine, OrdineDTO ordineCambiato);

    public OrdineDTO cercaOrdine(String codiceOrdine);

    public Collection<OrdineDTO> tuttiGliOrdini();

    public OrdineDTO aggiungiPizza(String codiceOrdine, Long idPizza, Integer quantita);
}
