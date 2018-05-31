package ma.maman.jeanne.repository.search;

import ma.maman.jeanne.domain.Classe;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Classe entity.
 */
public interface ClasseSearchRepository extends ElasticsearchRepository<Classe, Long> {
}
