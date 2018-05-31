package ma.maman.jeanne.repository;

import ma.maman.jeanne.domain.Fare;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Fare entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FareRepository extends JpaRepository<Fare, Long> {

}
