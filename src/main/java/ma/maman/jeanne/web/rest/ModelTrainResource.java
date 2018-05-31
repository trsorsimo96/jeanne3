package ma.maman.jeanne.web.rest;

import com.codahale.metrics.annotation.Timed;
import ma.maman.jeanne.domain.ModelTrain;

import ma.maman.jeanne.repository.ModelTrainRepository;
import ma.maman.jeanne.repository.search.ModelTrainSearchRepository;
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
 * REST controller for managing ModelTrain.
 */
@RestController
@RequestMapping("/api")
public class ModelTrainResource {

    private final Logger log = LoggerFactory.getLogger(ModelTrainResource.class);

    private static final String ENTITY_NAME = "modelTrain";

    private final ModelTrainRepository modelTrainRepository;

    private final ModelTrainSearchRepository modelTrainSearchRepository;

    public ModelTrainResource(ModelTrainRepository modelTrainRepository, ModelTrainSearchRepository modelTrainSearchRepository) {
        this.modelTrainRepository = modelTrainRepository;
        this.modelTrainSearchRepository = modelTrainSearchRepository;
    }

    /**
     * POST  /model-trains : Create a new modelTrain.
     *
     * @param modelTrain the modelTrain to create
     * @return the ResponseEntity with status 201 (Created) and with body the new modelTrain, or with status 400 (Bad Request) if the modelTrain has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/model-trains")
    @Timed
    public ResponseEntity<ModelTrain> createModelTrain(@Valid @RequestBody ModelTrain modelTrain) throws URISyntaxException {
        log.debug("REST request to save ModelTrain : {}", modelTrain);
        if (modelTrain.getId() != null) {
            throw new BadRequestAlertException("A new modelTrain cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ModelTrain result = modelTrainRepository.save(modelTrain);
        modelTrainSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/model-trains/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /model-trains : Updates an existing modelTrain.
     *
     * @param modelTrain the modelTrain to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated modelTrain,
     * or with status 400 (Bad Request) if the modelTrain is not valid,
     * or with status 500 (Internal Server Error) if the modelTrain couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/model-trains")
    @Timed
    public ResponseEntity<ModelTrain> updateModelTrain(@Valid @RequestBody ModelTrain modelTrain) throws URISyntaxException {
        log.debug("REST request to update ModelTrain : {}", modelTrain);
        if (modelTrain.getId() == null) {
            return createModelTrain(modelTrain);
        }
        ModelTrain result = modelTrainRepository.save(modelTrain);
        modelTrainSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, modelTrain.getId().toString()))
            .body(result);
    }

    /**
     * GET  /model-trains : get all the modelTrains.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of modelTrains in body
     */
    @GetMapping("/model-trains")
    @Timed
    public List<ModelTrain> getAllModelTrains() {
        log.debug("REST request to get all ModelTrains");
        return modelTrainRepository.findAll();
        }

    /**
     * GET  /model-trains/:id : get the "id" modelTrain.
     *
     * @param id the id of the modelTrain to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the modelTrain, or with status 404 (Not Found)
     */
    @GetMapping("/model-trains/{id}")
    @Timed
    public ResponseEntity<ModelTrain> getModelTrain(@PathVariable Long id) {
        log.debug("REST request to get ModelTrain : {}", id);
        ModelTrain modelTrain = modelTrainRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(modelTrain));
    }

    /**
     * DELETE  /model-trains/:id : delete the "id" modelTrain.
     *
     * @param id the id of the modelTrain to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/model-trains/{id}")
    @Timed
    public ResponseEntity<Void> deleteModelTrain(@PathVariable Long id) {
        log.debug("REST request to delete ModelTrain : {}", id);
        modelTrainRepository.delete(id);
        modelTrainSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/model-trains?query=:query : search for the modelTrain corresponding
     * to the query.
     *
     * @param query the query of the modelTrain search
     * @return the result of the search
     */
    @GetMapping("/_search/model-trains")
    @Timed
    public List<ModelTrain> searchModelTrains(@RequestParam String query) {
        log.debug("REST request to search ModelTrains for query {}", query);
        return StreamSupport
            .stream(modelTrainSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
