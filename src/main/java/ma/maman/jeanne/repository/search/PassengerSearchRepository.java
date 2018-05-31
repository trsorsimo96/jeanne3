package ma.maman.jeanne.repository.search;

import ma.maman.jeanne.domain.Passenger;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Passenger entity.
 */
public interface PassengerSearchRepository extends ElasticsearchRepository<Passenger, Long> {
}
