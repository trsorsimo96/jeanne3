package ma.maman.jeanne.repository.search;

import ma.maman.jeanne.domain.Email;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Email entity.
 */
public interface EmailSearchRepository extends ElasticsearchRepository<Email, Long> {
}
