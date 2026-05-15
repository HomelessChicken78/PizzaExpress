package it.itsacademy.pizzeriaexpress.repository;

import it.itsacademy.pizzeriaexpress.entity.Cliente;
import it.itsacademy.pizzeriaexpress.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be null
     * @return the entity with the given id
     * @throws IllegalArgumentException if id is null
     * @throws NotFoundException if no entity with that id is found
     */
    default Cliente findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(
                        () -> new NotFoundException("Non è stato possibile trovare un cliente con id " + id)
                );
    }
}
