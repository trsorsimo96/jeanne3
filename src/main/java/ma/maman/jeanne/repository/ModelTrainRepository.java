package ma.maman.jeanne.repository;

import ma.maman.jeanne.domain.ModelTrain;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ModelTrain entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModelTrainRepository extends JpaRepository<ModelTrain, Long> {

}
