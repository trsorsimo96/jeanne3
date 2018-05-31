package ma.maman.jeanne.repository.search;

import ma.maman.jeanne.domain.Car;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Car entity.
 */
public interface CarSearchRepository extends ElasticsearchRepository<Car, Long> {
}
