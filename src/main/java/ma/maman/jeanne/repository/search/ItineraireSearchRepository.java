package ma.maman.jeanne.repository.search;

import ma.maman.jeanne.domain.Itineraire;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Itineraire entity.
 */
public interface ItineraireSearchRepository extends ElasticsearchRepository<Itineraire, Long> {
}
