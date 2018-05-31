package ma.maman.jeanne.repository.search;

import ma.maman.jeanne.domain.Month;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Month entity.
 */
public interface MonthSearchRepository extends ElasticsearchRepository<Month, Long> {
}
