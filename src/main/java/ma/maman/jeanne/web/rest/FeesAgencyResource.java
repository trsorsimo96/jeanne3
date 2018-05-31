package ma.maman.jeanne.web.rest;

import com.codahale.metrics.annotation.Timed;
import ma.maman.jeanne.domain.FeesAgency;

import ma.maman.jeanne.repository.FeesAgencyRepository;
import ma.maman.jeanne.repository.search.FeesAgencySearchRepository;
import ma.maman.jeanne.web.rest.errors.BadRequestAlertException;
import ma.maman.jeanne.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing FeesAgency.
 */
@RestController
@RequestMapping("/api")
public class FeesAgencyResource {

    private final Logger log = LoggerFactory.getLogger(FeesAgencyResource.class);

    private static final String ENTITY_NAME = "feesAgency";

    private final FeesAgencyRepository feesAgencyRepository;

    private final FeesAgencySearchRepository feesAgencySearchRepository;

    public FeesAgencyResource(FeesAgencyRepository feesAgencyRepository, FeesAgencySearchRepository feesAgencySearchRepository) {
        this.feesAgencyRepository = feesAgencyRepository;
        this.feesAgencySearchRepository = feesAgencySearchRepository;
    }

    /**
     * POST  /fees-agencies : Create a new feesAgency.
     *
     * @param feesAgency the feesAgency to create
     * @return the ResponseEntity with status 201 (Created) and with body the new feesAgency, or with status 400 (Bad Request) if the feesAgency has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fees-agencies")
    @Timed
    public ResponseEntity<FeesAgency> createFeesAgency(@RequestBody FeesAgency feesAgency) throws URISyntaxException {
        log.debug("REST request to save FeesAgency : {}", feesAgency);
        if (feesAgency.getId() != null) {
            throw new BadRequestAlertException("A new feesAgency cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FeesAgency result = feesAgencyRepository.save(feesAgency);
        feesAgencySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/fees-agencies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fees-agencies : Updates an existing feesAgency.
     *
     * @param feesAgency the feesAgency to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated feesAgency,
     * or with status 400 (Bad Request) if the feesAgency is not valid,
     * or with status 500 (Internal Server Error) if the feesAgency couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fees-agencies")
    @Timed
    public ResponseEntity<FeesAgency> updateFeesAgency(@RequestBody FeesAgency feesAgency) throws URISyntaxException {
        log.debug("REST request to update FeesAgency : {}", feesAgency);
        if (feesAgency.getId() == null) {
            return createFeesAgency(feesAgency);
        }
        FeesAgency result = feesAgencyRepository.save(feesAgency);
        feesAgencySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, feesAgency.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fees-agencies : get all the feesAgencies.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of feesAgencies in body
     */
    @GetMapping("/fees-agencies")
    @Timed
    public List<FeesAgency> getAllFeesAgencies() {
        log.debug("REST request to get all FeesAgencies");
        return feesAgencyRepository.findAll();
        }

    /**
     * GET  /fees-agencies/:id : get the "id" feesAgency.
     *
     * @param id the id of the feesAgency to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the feesAgency, or with status 404 (Not Found)
     */
    @GetMapping("/fees-agencies/{id}")
    @Timed
    public ResponseEntity<FeesAgency> getFeesAgency(@PathVariable Long id) {
        log.debug("REST request to get FeesAgency : {}", id);
        FeesAgency feesAgency = feesAgencyRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(feesAgency));
    }

    /**
     * DELETE  /fees-agencies/:id : delete the "id" feesAgency.
     *
     * @param id the id of the feesAgency to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fees-agencies/{id}")
    @Timed
    public ResponseEntity<Void> deleteFeesAgency(@PathVariable Long id) {
        log.debug("REST request to delete FeesAgency : {}", id);
        feesAgencyRepository.delete(id);
        feesAgencySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/fees-agencies?query=:query : search for the feesAgency corresponding
     * to the query.
     *
     * @param query the query of the feesAgency search
     * @return the result of the search
     */
    @GetMapping("/_search/fees-agencies")
    @Timed
    public List<FeesAgency> searchFeesAgencies(@RequestParam String query) {
        log.debug("REST request to search FeesAgencies for query {}", query);
        return StreamSupport
            .stream(feesAgencySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
