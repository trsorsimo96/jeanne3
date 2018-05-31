package ma.maman.jeanne.web.rest;

import com.codahale.metrics.annotation.Timed;
import ma.maman.jeanne.domain.Voyage;

import ma.maman.jeanne.repository.VoyageRepository;
import ma.maman.jeanne.repository.search.VoyageSearchRepository;
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
 * REST controller for managing Voyage.
 */
@RestController
@RequestMapping("/api")
public class VoyageResource {

    private final Logger log = LoggerFactory.getLogger(VoyageResource.class);

    private static final String ENTITY_NAME = "voyage";

    private final VoyageRepository voyageRepository;

    private final VoyageSearchRepository voyageSearchRepository;

    public VoyageResource(VoyageRepository voyageRepository, VoyageSearchRepository voyageSearchRepository) {
        this.voyageRepository = voyageRepository;
        this.voyageSearchRepository = voyageSearchRepository;
    }

    /**
     * POST  /voyages : Create a new voyage.
     *
     * @param voyage the voyage to create
     * @return the ResponseEntity with status 201 (Created) and with body the new voyage, or with status 400 (Bad Request) if the voyage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/voyages")
    @Timed
    public ResponseEntity<Voyage> createVoyage(@Valid @RequestBody Voyage voyage) throws URISyntaxException {
        log.debug("REST request to save Voyage : {}", voyage);
        if (voyage.getId() != null) {
            throw new BadRequestAlertException("A new voyage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Voyage result = voyageRepository.save(voyage);
        voyageSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/voyages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /voyages : Updates an existing voyage.
     *
     * @param voyage the voyage to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated voyage,
     * or with status 400 (Bad Request) if the voyage is not valid,
     * or with status 500 (Internal Server Error) if the voyage couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/voyages")
    @Timed
    public ResponseEntity<Voyage> updateVoyage(@Valid @RequestBody Voyage voyage) throws URISyntaxException {
        log.debug("REST request to update Voyage : {}", voyage);
        if (voyage.getId() == null) {
            return createVoyage(voyage);
        }
        Voyage result = voyageRepository.save(voyage);
        voyageSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, voyage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /voyages : get all the voyages.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of voyages in body
     */
    @GetMapping("/voyages")
    @Timed
    public List<Voyage> getAllVoyages() {
        log.debug("REST request to get all Voyages");
        return voyageRepository.findAll();
        }

    /**
     * GET  /voyages/:id : get the "id" voyage.
     *
     * @param id the id of the voyage to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the voyage, or with status 404 (Not Found)
     */
    @GetMapping("/voyages/{id}")
    @Timed
    public ResponseEntity<Voyage> getVoyage(@PathVariable Long id) {
        log.debug("REST request to get Voyage : {}", id);
        Voyage voyage = voyageRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(voyage));
    }

    /**
     * DELETE  /voyages/:id : delete the "id" voyage.
     *
     * @param id the id of the voyage to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/voyages/{id}")
    @Timed
    public ResponseEntity<Void> deleteVoyage(@PathVariable Long id) {
        log.debug("REST request to delete Voyage : {}", id);
        voyageRepository.delete(id);
        voyageSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/voyages?query=:query : search for the voyage corresponding
     * to the query.
     *
     * @param query the query of the voyage search
     * @return the result of the search
     */
    @GetMapping("/_search/voyages")
    @Timed
    public List<Voyage> searchVoyages(@RequestParam String query) {
        log.debug("REST request to search Voyages for query {}", query);
        return StreamSupport
            .stream(voyageSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
