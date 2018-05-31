package ma.maman.jeanne.repository.search;

import ma.maman.jeanne.domain.Deposit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Deposit entity.
 */
public interface DepositSearchRepository extends ElasticsearchRepository<Deposit, Long> {
}
