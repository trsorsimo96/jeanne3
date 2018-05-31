package ma.maman.jeanne.repository;

import ma.maman.jeanne.domain.ModelCar;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ModelCar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModelCarRepository extends JpaRepository<ModelCar, Long> {

}
