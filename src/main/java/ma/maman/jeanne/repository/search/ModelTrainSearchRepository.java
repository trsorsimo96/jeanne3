package ma.maman.jeanne.repository.search;

import ma.maman.jeanne.domain.ModelTrain;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ModelTrain entity.
 */
public interface ModelTrainSearchRepository extends ElasticsearchRepository<ModelTrain, Long> {
}
