package ma.maman.jeanne.web.rest;

import com.codahale.metrics.annotation.Timed;
import ma.maman.jeanne.domain.Itineraire;

import ma.maman.jeanne.repository.ItineraireRepository;
import ma.maman.jeanne.repository.search.ItineraireSearchRepository;
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
 * REST controller for managing Itineraire.
 */
@RestController
@RequestMapping("/api")
public class ItineraireResource {

    private final Logger log = LoggerFactory.getLogger(ItineraireResource.class);

    private static final String ENTITY_NAME = "itineraire";

    private final ItineraireRepository itineraireRepository;

    private final ItineraireSearchRepository itineraireSearchRepository;

    public ItineraireResource(ItineraireRepository itineraireRepository, ItineraireSearchRepository itineraireSearchRepository) {
        this.itineraireRepository = itineraireRepository;
        this.itineraireSearchRepository = itineraireSearchRepository;
    }

    /**
     * POST  /itineraires : Create a new itineraire.
     *
     * @param itineraire the itineraire to create
     * @return the ResponseEntity with status 201 (Created) and with body the new itineraire, or with status 400 (Bad Request) if the itineraire has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/itineraires")
    @Timed
    public ResponseEntity<Itineraire> createItineraire(@Valid @RequestBody Itineraire itineraire) throws URISyntaxException {
        log.debug("REST request to save Itineraire : {}", itineraire);
        if (itineraire.getId() != null) {
            throw new BadRequestAlertException("A new itineraire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Itineraire result = itineraireRepository.save(itineraire);
        itineraireSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/itineraires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /itineraires : Updates an existing itineraire.
     *
     * @param itineraire the itineraire to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated itineraire,
     * or with status 400 (Bad Request) if the itineraire is not valid,
     * or with status 500 (Internal Server Error) if the itineraire couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/itineraires")
    @Timed
    public ResponseEntity<Itineraire> updateItineraire(@Valid @RequestBody Itineraire itineraire) throws URISyntaxException {
        log.debug("REST request to update Itineraire : {}", itineraire);
        if (itineraire.getId() == null) {
            return createItineraire(itineraire);
        }
        Itineraire result = itineraireRepository.save(itineraire);
        itineraireSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, itineraire.getId().toString()))
            .body(result);
    }

    /**
     * GET  /itineraires : get all the itineraires.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of itineraires in body
     */
    @GetMapping("/itineraires")
    @Timed
    public List<Itineraire> getAllItineraires() {
        log.debug("REST request to get all Itineraires");
        return itineraireRepository.findAll();
        }

    /**
     * GET  /itineraires/:id : get the "id" itineraire.
     *
     * @param id the id of the itineraire to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the itineraire, or with status 404 (Not Found)
     */
    @GetMapping("/itineraires/{id}")
    @Timed
    public ResponseEntity<Itineraire> getItineraire(@PathVariable Long id) {
        log.debug("REST request to get Itineraire : {}", id);
        Itineraire itineraire = itineraireRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(itineraire));
    }

    /**
     * DELETE  /itineraires/:id : delete the "id" itineraire.
     *
     * @param id the id of the itineraire to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/itineraires/{id}")
    @Timed
    public ResponseEntity<Void> deleteItineraire(@PathVariable Long id) {
        log.debug("REST request to delete Itineraire : {}", id);
        itineraireRepository.delete(id);
        itineraireSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/itineraires?query=:query : search for the itineraire corresponding
     * to the query.
     *
     * @param query the query of the itineraire search
     * @return the result of the search
     */
    @GetMapping("/_search/itineraires")
    @Timed
    public List<Itineraire> searchItineraires(@RequestParam String query) {
        log.debug("REST request to search Itineraires for query {}", query);
        return StreamSupport
            .stream(itineraireSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
