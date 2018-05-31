package ma.maman.jeanne.repository.search;

import ma.maman.jeanne.domain.Fare;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Fare entity.
 */
public interface FareSearchRepository extends ElasticsearchRepository<Fare, Long> {
}
