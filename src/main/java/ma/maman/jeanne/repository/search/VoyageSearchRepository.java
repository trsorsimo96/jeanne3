package ma.maman.jeanne.repository.search;

import ma.maman.jeanne.domain.Voyage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Voyage entity.
 */
public interface VoyageSearchRepository extends ElasticsearchRepository<Voyage, Long> {
}
