package ma.maman.jeanne.repository.search;

import ma.maman.jeanne.domain.ModelCar;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ModelCar entity.
 */
public interface ModelCarSearchRepository extends ElasticsearchRepository<ModelCar, Long> {
}
