package ma.maman.jeanne.web.rest;

import com.codahale.metrics.annotation.Timed;
import ma.maman.jeanne.domain.Train;

import ma.maman.jeanne.repository.TrainRepository;
import ma.maman.jeanne.repository.search.TrainSearchRepository;
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
 * REST controller for managing Train.
 */
@RestController
@RequestMapping("/api")
public class TrainResource {

    private final Logger log = LoggerFactory.getLogger(TrainResource.class);

    private static final String ENTITY_NAME = "train";

    private final TrainRepository trainRepository;

    private final TrainSearchRepository trainSearchRepository;

    public TrainResource(TrainRepository trainRepository, TrainSearchRepository trainSearchRepository) {
        this.trainRepository = trainRepository;
        this.trainSearchRepository = trainSearchRepository;
    }

    /**
     * POST  /trains : Create a new train.
     *
     * @param train the train to create
     * @return the ResponseEntity with status 201 (Created) and with body the new train, or with status 400 (Bad Request) if the train has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/trains")
    @Timed
    public ResponseEntity<Train> createTrain(@Valid @RequestBody Train train) throws URISyntaxException {
        log.debug("REST request to save Train : {}", train);
        if (train.getId() != null) {
            throw new BadRequestAlertException("A new train cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Train result = trainRepository.save(train);
        trainSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/trains/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /trains : Updates an existing train.
     *
     * @param train the train to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated train,
     * or with status 400 (Bad Request) if the train is not valid,
     * or with status 500 (Internal Server Error) if the train couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/trains")
    @Timed
    public ResponseEntity<Train> updateTrain(@Valid @RequestBody Train train) throws URISyntaxException {
        log.debug("REST request to update Train : {}", train);
        if (train.getId() == null) {
            return createTrain(train);
        }
        Train result = trainRepository.save(train);
        trainSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, train.getId().toString()))
            .body(result);
    }

    /**
     * GET  /trains : get all the trains.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of trains in body
     */
    @GetMapping("/trains")
    @Timed
    public List<Train> getAllTrains() {
        log.debug("REST request to get all Trains");
        return trainRepository.findAll();
        }

    /**
     * GET  /trains/:id : get the "id" train.
     *
     * @param id the id of the train to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the train, or with status 404 (Not Found)
     */
    @GetMapping("/trains/{id}")
    @Timed
    public ResponseEntity<Train> getTrain(@PathVariable Long id) {
        log.debug("REST request to get Train : {}", id);
        Train train = trainRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(train));
    }

    /**
     * DELETE  /trains/:id : delete the "id" train.
     *
     * @param id the id of the train to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/trains/{id}")
    @Timed
    public ResponseEntity<Void> deleteTrain(@PathVariable Long id) {
        log.debug("REST request to delete Train : {}", id);
        trainRepository.delete(id);
        trainSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/trains?query=:query : search for the train corresponding
     * to the query.
     *
     * @param query the query of the train search
     * @return the result of the search
     */
    @GetMapping("/_search/trains")
    @Timed
    public List<Train> searchTrains(@RequestParam String query) {
        log.debug("REST request to search Trains for query {}", query);
        return StreamSupport
            .stream(trainSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
