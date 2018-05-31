package ma.maman.jeanne.repository;

import ma.maman.jeanne.domain.Wagon;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Wagon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WagonRepository extends JpaRepository<Wagon, Long> {

}
