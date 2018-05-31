package ma.maman.jeanne.repository.search;

import ma.maman.jeanne.domain.CompanyClasse;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CompanyClasse entity.
 */
public interface CompanyClasseSearchRepository extends ElasticsearchRepository<CompanyClasse, Long> {
}
