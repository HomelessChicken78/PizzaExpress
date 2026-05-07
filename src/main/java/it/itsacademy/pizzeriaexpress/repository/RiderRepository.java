package it.itsacademy.pizzeriaexpress.repository;

import it.itsacademy.pizzeriaexpress.entity.Rider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiderRepository extends JpaRepository<Rider, Long> {
}
