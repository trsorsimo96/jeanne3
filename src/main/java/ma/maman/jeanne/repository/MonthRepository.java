package ma.maman.jeanne.repository;

import ma.maman.jeanne.domain.Month;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Month entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MonthRepository extends JpaRepository<Month, Long> {

}
