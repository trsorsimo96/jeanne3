package ma.maman.jeanne.repository;

import ma.maman.jeanne.domain.Voyage;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Voyage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VoyageRepository extends JpaRepository<Voyage, Long> {

}
