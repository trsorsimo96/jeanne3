package ma.maman.jeanne.web.rest;

import com.codahale.metrics.annotation.Timed;
import ma.maman.jeanne.domain.Segment;

import ma.maman.jeanne.repository.SegmentRepository;
import ma.maman.jeanne.repository.search.SegmentSearchRepository;
import ma.maman.jeanne.web.rest.errors.BadRequestAlertException;
import ma.maman.jeanne.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Segment.
 */
@RestController
@RequestMapping("/api")
public class SegmentResource {

    private final Logger log = LoggerFactory.getLogger(SegmentResource.class);

    private static final String ENTITY_NAME = "segment";

    private final SegmentRepository segmentRepository;

    private final SegmentSearchRepository segmentSearchRepository;

    public SegmentResource(SegmentRepository segmentRepository, SegmentSearchRepository segmentSearchRepository) {
        this.segmentRepository = segmentRepository;
        this.segmentSearchRepository = segmentSearchRepository;
    }

    /**
     * POST  /segments : Create a new segment.
     *
     * @param segment the segment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new segment, or with status 400 (Bad Request) if the segment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/segments")
    @Timed
    public ResponseEntity<Segment> createSegment(@Valid @RequestBody Segment segment) throws URISyntaxException {
        log.debug("REST request to save Segment : {}", segment);
        if (segment.getId() != null) {
            throw new BadRequestAlertException("A new segment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Segment result = segmentRepository.save(segment);
        segmentSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/segments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /segments : Updates an existing segment.
     *
     * @param segment the segment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated segment,
     * or with status 400 (Bad Request) if the segment is not valid,
     * or with status 500 (Internal Server Error) if the segment couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/segments")
    @Timed
    public ResponseEntity<Segment> updateSegment(@Valid @RequestBody Segment segment) throws URISyntaxException {
        log.debug("REST request to update Segment : {}", segment);
        if (segment.getId() == null) {
            return createSegment(segment);
        }
        Segment result = segmentRepository.save(segment);
        segmentSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, segment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /segments : get all the segments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of segments in body
     */
    @GetMapping("/segments")
    @Timed
    public List<Segment> getAllSegments() {
        log.debug("REST request to get all Segments");
        return segmentRepository.findAll();
        }

    /**
     * GET  /segments/:id : get the "id" segment.
     *
     * @param id the id of the segment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the segment, or with status 404 (Not Found)
     */
    @GetMapping("/segments/{id}")
    @Timed
    public ResponseEntity<Segment> getSegment(@PathVariable Long id) {
        log.debug("REST request to get Segment : {}", id);
        Segment segment = segmentRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(segment));
    }

    /**
     * DELETE  /segments/:id : delete the "id" segment.
     *
     * @param id the id of the segment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/segments/{id}")
    @Timed
    public ResponseEntity<Void> deleteSegment(@PathVariable Long id) {
        log.debug("REST request to delete Segment : {}", id);
        segmentRepository.delete(id);
        segmentSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/segments?query=:query : search for the segment corresponding
     * to the query.
     *
     * @param query the query of the segment search
     * @return the result of the search
     */
    @GetMapping("/_search/segments")
    @Timed
    public List<Segment> searchSegments(@RequestParam String query) {
        log.debug("REST request to search Segments for query {}", query);
        return StreamSupport
            .stream(segmentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
