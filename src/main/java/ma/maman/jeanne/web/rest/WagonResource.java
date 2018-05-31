package ma.maman.jeanne.web.rest;

import com.codahale.metrics.annotation.Timed;
import ma.maman.jeanne.domain.Wagon;

import ma.maman.jeanne.repository.WagonRepository;
import ma.maman.jeanne.repository.search.WagonSearchRepository;
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
 * REST controller for managing Wagon.
 */
@RestController
@RequestMapping("/api")
public class WagonResource {

    private final Logger log = LoggerFactory.getLogger(WagonResource.class);

    private static final String ENTITY_NAME = "wagon";

    private final WagonRepository wagonRepository;

    private final WagonSearchRepository wagonSearchRepository;

    public WagonResource(WagonRepository wagonRepository, WagonSearchRepository wagonSearchRepository) {
        this.wagonRepository = wagonRepository;
        this.wagonSearchRepository = wagonSearchRepository;
    }

    /**
     * POST  /wagons : Create a new wagon.
     *
     * @param wagon the wagon to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wagon, or with status 400 (Bad Request) if the wagon has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wagons")
    @Timed
    public ResponseEntity<Wagon> createWagon(@Valid @RequestBody Wagon wagon) throws URISyntaxException {
        log.debug("REST request to save Wagon : {}", wagon);
        if (wagon.getId() != null) {
            throw new BadRequestAlertException("A new wagon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Wagon result = wagonRepository.save(wagon);
        wagonSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/wagons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wagons : Updates an existing wagon.
     *
     * @param wagon the wagon to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wagon,
     * or with status 400 (Bad Request) if the wagon is not valid,
     * or with status 500 (Internal Server Error) if the wagon couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wagons")
    @Timed
    public ResponseEntity<Wagon> updateWagon(@Valid @RequestBody Wagon wagon) throws URISyntaxException {
        log.debug("REST request to update Wagon : {}", wagon);
        if (wagon.getId() == null) {
            return createWagon(wagon);
        }
        Wagon result = wagonRepository.save(wagon);
        wagonSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wagon.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wagons : get all the wagons.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of wagons in body
     */
    @GetMapping("/wagons")
    @Timed
    public List<Wagon> getAllWagons() {
        log.debug("REST request to get all Wagons");
        return wagonRepository.findAll();
        }

    /**
     * GET  /wagons/:id : get the "id" wagon.
     *
     * @param id the id of the wagon to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wagon, or with status 404 (Not Found)
     */
    @GetMapping("/wagons/{id}")
    @Timed
    public ResponseEntity<Wagon> getWagon(@PathVariable Long id) {
        log.debug("REST request to get Wagon : {}", id);
        Wagon wagon = wagonRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(wagon));
    }

    /**
     * DELETE  /wagons/:id : delete the "id" wagon.
     *
     * @param id the id of the wagon to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wagons/{id}")
    @Timed
    public ResponseEntity<Void> deleteWagon(@PathVariable Long id) {
        log.debug("REST request to delete Wagon : {}", id);
        wagonRepository.delete(id);
        wagonSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/wagons?query=:query : search for the wagon corresponding
     * to the query.
     *
     * @param query the query of the wagon search
     * @return the result of the search
     */
    @GetMapping("/_search/wagons")
    @Timed
    public List<Wagon> searchWagons(@RequestParam String query) {
        log.debug("REST request to search Wagons for query {}", query);
        return StreamSupport
            .stream(wagonSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
