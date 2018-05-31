package ma.maman.jeanne.repository;

import ma.maman.jeanne.domain.Segment;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Segment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SegmentRepository extends JpaRepository<Segment, Long> {

}
