package ma.maman.jeanne.web.rest;

import com.codahale.metrics.annotation.Timed;
import ma.maman.jeanne.domain.Fare;

import ma.maman.jeanne.repository.FareRepository;
import ma.maman.jeanne.repository.search.FareSearchRepository;
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
 * REST controller for managing Fare.
 */
@RestController
@RequestMapping("/api")
public class FareResource {

    private final Logger log = LoggerFactory.getLogger(FareResource.class);

    private static final String ENTITY_NAME = "fare";

    private final FareRepository fareRepository;

    private final FareSearchRepository fareSearchRepository;

    public FareResource(FareRepository fareRepository, FareSearchRepository fareSearchRepository) {
        this.fareRepository = fareRepository;
        this.fareSearchRepository = fareSearchRepository;
    }

    /**
     * POST  /fares : Create a new fare.
     *
     * @param fare the fare to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fare, or with status 400 (Bad Request) if the fare has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fares")
    @Timed
    public ResponseEntity<Fare> createFare(@Valid @RequestBody Fare fare) throws URISyntaxException {
        log.debug("REST request to save Fare : {}", fare);
        if (fare.getId() != null) {
            throw new BadRequestAlertException("A new fare cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Fare result = fareRepository.save(fare);
        fareSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/fares/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fares : Updates an existing fare.
     *
     * @param fare the fare to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fare,
     * or with status 400 (Bad Request) if the fare is not valid,
     * or with status 500 (Internal Server Error) if the fare couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fares")
    @Timed
    public ResponseEntity<Fare> updateFare(@Valid @RequestBody Fare fare) throws URISyntaxException {
        log.debug("REST request to update Fare : {}", fare);
        if (fare.getId() == null) {
            return createFare(fare);
        }
        Fare result = fareRepository.save(fare);
        fareSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fare.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fares : get all the fares.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of fares in body
     */
    @GetMapping("/fares")
    @Timed
    public List<Fare> getAllFares() {
        log.debug("REST request to get all Fares");
        return fareRepository.findAll();
        }

    /**
     * GET  /fares/:id : get the "id" fare.
     *
     * @param id the id of the fare to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fare, or with status 404 (Not Found)
     */
    @GetMapping("/fares/{id}")
    @Timed
    public ResponseEntity<Fare> getFare(@PathVariable Long id) {
        log.debug("REST request to get Fare : {}", id);
        Fare fare = fareRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fare));
    }

    /**
     * DELETE  /fares/:id : delete the "id" fare.
     *
     * @param id the id of the fare to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fares/{id}")
    @Timed
    public ResponseEntity<Void> deleteFare(@PathVariable Long id) {
        log.debug("REST request to delete Fare : {}", id);
        fareRepository.delete(id);
        fareSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/fares?query=:query : search for the fare corresponding
     * to the query.
     *
     * @param query the query of the fare search
     * @return the result of the search
     */
    @GetMapping("/_search/fares")
    @Timed
    public List<Fare> searchFares(@RequestParam String query) {
        log.debug("REST request to search Fares for query {}", query);
        return StreamSupport
            .stream(fareSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
