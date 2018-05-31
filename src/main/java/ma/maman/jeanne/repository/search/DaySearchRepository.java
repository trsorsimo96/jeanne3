package ma.maman.jeanne.repository.search;

import ma.maman.jeanne.domain.Day;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Day entity.
 */
public interface DaySearchRepository extends ElasticsearchRepository<Day, Long> {
}
