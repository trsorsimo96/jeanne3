package ma.maman.jeanne.repository.search;

import ma.maman.jeanne.domain.FeesAgency;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the FeesAgency entity.
 */
public interface FeesAgencySearchRepository extends ElasticsearchRepository<FeesAgency, Long> {
}
