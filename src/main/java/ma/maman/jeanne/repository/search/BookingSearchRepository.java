package ma.maman.jeanne.repository.search;

import ma.maman.jeanne.domain.Booking;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Booking entity.
 */
public interface BookingSearchRepository extends ElasticsearchRepository<Booking, Long> {
}
