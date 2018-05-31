package ma.maman.jeanne.repository.search;

import ma.maman.jeanne.domain.Routes;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Routes entity.
 */
public interface RoutesSearchRepository extends ElasticsearchRepository<Routes, Long> {
}
