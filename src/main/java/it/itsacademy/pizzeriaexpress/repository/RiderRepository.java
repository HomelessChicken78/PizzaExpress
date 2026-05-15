package it.itsacademy.pizzeriaexpress.repository;

import it.itsacademy.pizzeriaexpress.entity.Rider;
import it.itsacademy.pizzeriaexpress.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiderRepository extends JpaRepository<Rider, Long> {
    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be null
     * @return the entity with the given id
     * @throws IllegalArgumentException if id is null
     * @throws NotFoundException if no entity with that id is found
     */
    default Rider findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(
                        () -> new NotFoundException("Non è stato possibile trovare un rider con id " + id)
                );
    }
}
