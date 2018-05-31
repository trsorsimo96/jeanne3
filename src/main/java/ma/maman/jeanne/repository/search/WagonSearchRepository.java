package ma.maman.jeanne.repository.search;

import ma.maman.jeanne.domain.Wagon;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Wagon entity.
 */
public interface WagonSearchRepository extends ElasticsearchRepository<Wagon, Long> {
}
