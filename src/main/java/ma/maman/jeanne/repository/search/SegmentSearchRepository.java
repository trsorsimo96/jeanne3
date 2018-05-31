package ma.maman.jeanne.repository.search;

import ma.maman.jeanne.domain.Segment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Segment entity.
 */
public interface SegmentSearchRepository extends ElasticsearchRepository<Segment, Long> {
}
