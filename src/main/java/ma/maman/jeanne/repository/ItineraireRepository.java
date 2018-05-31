package ma.maman.jeanne.repository;

import ma.maman.jeanne.domain.Itineraire;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Itineraire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItineraireRepository extends JpaRepository<Itineraire, Long> {

}
