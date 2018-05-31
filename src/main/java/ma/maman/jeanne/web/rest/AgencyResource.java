package ma.maman.jeanne.web.rest;

import com.codahale.metrics.annotation.Timed;
import ma.maman.jeanne.domain.Agency;

import ma.maman.jeanne.repository.AgencyRepository;
import ma.maman.jeanne.repository.search.AgencySearchRepository;
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
 * REST controller for managing Agency.
 */
@RestController
@RequestMapping("/api")
public class AgencyResource {

    private final Logger log = LoggerFactory.getLogger(AgencyResource.class);

    private static final String ENTITY_NAME = "agency";

    private final AgencyRepository agencyRepository;

    private final AgencySearchRepository agencySearchRepository;

    public AgencyResource(AgencyRepository agencyRepository, AgencySearchRepository agencySearchRepository) {
        this.agencyRepository = agencyRepository;
        this.agencySearchRepository = agencySearchRepository;
    }

    /**
     * POST  /agencies : Create a new agency.
     *
     * @param agency the agency to create
     * @return the ResponseEntity with status 201 (Created) and with body the new agency, or with status 400 (Bad Request) if the agency has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/agencies")
    @Timed
    public ResponseEntity<Agency> createAgency(@Valid @RequestBody Agency agency) throws URISyntaxException {
        log.debug("REST request to save Agency : {}", agency);
        if (agency.getId() != null) {
            throw new BadRequestAlertException("A new agency cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Agency result = agencyRepository.save(agency);
        agencySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/agencies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /agencies : Updates an existing agency.
     *
     * @param agency the agency to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated agency,
     * or with status 400 (Bad Request) if the agency is not valid,
     * or with status 500 (Internal Server Error) if the agency couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/agencies")
    @Timed
    public ResponseEntity<Agency> updateAgency(@Valid @RequestBody Agency agency) throws URISyntaxException {
        log.debug("REST request to update Agency : {}", agency);
        if (agency.getId() == null) {
            return createAgency(agency);
        }
        Agency result = agencyRepository.save(agency);
        agencySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, agency.getId().toString()))
            .body(result);
    }

    /**
     * GET  /agencies : get all the agencies.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of agencies in body
     */
    @GetMapping("/agencies")
    @Timed
    public List<Agency> getAllAgencies() {
        log.debug("REST request to get all Agencies");
        return agencyRepository.findAll();
        }

    /**
     * GET  /agencies/:id : get the "id" agency.
     *
     * @param id the id of the agency to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the agency, or with status 404 (Not Found)
     */
    @GetMapping("/agencies/{id}")
    @Timed
    public ResponseEntity<Agency> getAgency(@PathVariable Long id) {
        log.debug("REST request to get Agency : {}", id);
        Agency agency = agencyRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(agency));
    }

    /**
     * DELETE  /agencies/:id : delete the "id" agency.
     *
     * @param id the id of the agency to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/agencies/{id}")
    @Timed
    public ResponseEntity<Void> deleteAgency(@PathVariable Long id) {
        log.debug("REST request to delete Agency : {}", id);
        agencyRepository.delete(id);
        agencySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/agencies?query=:query : search for the agency corresponding
     * to the query.
     *
     * @param query the query of the agency search
     * @return the result of the search
     */
    @GetMapping("/_search/agencies")
    @Timed
    public List<Agency> searchAgencies(@RequestParam String query) {
        log.debug("REST request to search Agencies for query {}", query);
        return StreamSupport
            .stream(agencySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
