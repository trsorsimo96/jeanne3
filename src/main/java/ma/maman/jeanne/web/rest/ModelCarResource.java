package ma.maman.jeanne.web.rest;

import com.codahale.metrics.annotation.Timed;
import ma.maman.jeanne.domain.ModelCar;

import ma.maman.jeanne.repository.ModelCarRepository;
import ma.maman.jeanne.repository.search.ModelCarSearchRepository;
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
 * REST controller for managing ModelCar.
 */
@RestController
@RequestMapping("/api")
public class ModelCarResource {

    private final Logger log = LoggerFactory.getLogger(ModelCarResource.class);

    private static final String ENTITY_NAME = "modelCar";

    private final ModelCarRepository modelCarRepository;

    private final ModelCarSearchRepository modelCarSearchRepository;

    public ModelCarResource(ModelCarRepository modelCarRepository, ModelCarSearchRepository modelCarSearchRepository) {
        this.modelCarRepository = modelCarRepository;
        this.modelCarSearchRepository = modelCarSearchRepository;
    }

    /**
     * POST  /model-cars : Create a new modelCar.
     *
     * @param modelCar the modelCar to create
     * @return the ResponseEntity with status 201 (Created) and with body the new modelCar, or with status 400 (Bad Request) if the modelCar has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/model-cars")
    @Timed
    public ResponseEntity<ModelCar> createModelCar(@Valid @RequestBody ModelCar modelCar) throws URISyntaxException {
        log.debug("REST request to save ModelCar : {}", modelCar);
        if (modelCar.getId() != null) {
            throw new BadRequestAlertException("A new modelCar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ModelCar result = modelCarRepository.save(modelCar);
        modelCarSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/model-cars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /model-cars : Updates an existing modelCar.
     *
     * @param modelCar the modelCar to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated modelCar,
     * or with status 400 (Bad Request) if the modelCar is not valid,
     * or with status 500 (Internal Server Error) if the modelCar couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/model-cars")
    @Timed
    public ResponseEntity<ModelCar> updateModelCar(@Valid @RequestBody ModelCar modelCar) throws URISyntaxException {
        log.debug("REST request to update ModelCar : {}", modelCar);
        if (modelCar.getId() == null) {
            return createModelCar(modelCar);
        }
        ModelCar result = modelCarRepository.save(modelCar);
        modelCarSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, modelCar.getId().toString()))
            .body(result);
    }

    /**
     * GET  /model-cars : get all the modelCars.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of modelCars in body
     */
    @GetMapping("/model-cars")
    @Timed
    public List<ModelCar> getAllModelCars() {
        log.debug("REST request to get all ModelCars");
        return modelCarRepository.findAll();
        }

    /**
     * GET  /model-cars/:id : get the "id" modelCar.
     *
     * @param id the id of the modelCar to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the modelCar, or with status 404 (Not Found)
     */
    @GetMapping("/model-cars/{id}")
    @Timed
    public ResponseEntity<ModelCar> getModelCar(@PathVariable Long id) {
        log.debug("REST request to get ModelCar : {}", id);
        ModelCar modelCar = modelCarRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(modelCar));
    }

    /**
     * DELETE  /model-cars/:id : delete the "id" modelCar.
     *
     * @param id the id of the modelCar to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/model-cars/{id}")
    @Timed
    public ResponseEntity<Void> deleteModelCar(@PathVariable Long id) {
        log.debug("REST request to delete ModelCar : {}", id);
        modelCarRepository.delete(id);
        modelCarSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/model-cars?query=:query : search for the modelCar corresponding
     * to the query.
     *
     * @param query the query of the modelCar search
     * @return the result of the search
     */
    @GetMapping("/_search/model-cars")
    @Timed
    public List<ModelCar> searchModelCars(@RequestParam String query) {
        log.debug("REST request to search ModelCars for query {}", query);
        return StreamSupport
            .stream(modelCarSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
