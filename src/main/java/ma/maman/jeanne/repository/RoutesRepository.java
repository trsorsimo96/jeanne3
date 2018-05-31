package ma.maman.jeanne.repository;

import ma.maman.jeanne.domain.Routes;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Routes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoutesRepository extends JpaRepository<Routes, Long> {

}
