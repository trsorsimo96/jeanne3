package ma.maman.jeanne.repository.search;

import ma.maman.jeanne.domain.Train;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Train entity.
 */
public interface TrainSearchRepository extends ElasticsearchRepository<Train, Long> {
}
