package ma.maman.jeanne.web.rest;

import com.codahale.metrics.annotation.Timed;
import ma.maman.jeanne.domain.Classe;

import ma.maman.jeanne.repository.ClasseRepository;
import ma.maman.jeanne.repository.search.ClasseSearchRepository;
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
 * REST controller for managing Classe.
 */
@RestController
@RequestMapping("/api")
public class ClasseResource {

    private final Logger log = LoggerFactory.getLogger(ClasseResource.class);

    private static final String ENTITY_NAME = "classe";

    private final ClasseRepository classeRepository;

    private final ClasseSearchRepository classeSearchRepository;

    public ClasseResource(ClasseRepository classeRepository, ClasseSearchRepository classeSearchRepository) {
        this.classeRepository = classeRepository;
        this.classeSearchRepository = classeSearchRepository;
    }

    /**
     * POST  /classes : Create a new classe.
     *
     * @param classe the classe to create
     * @return the ResponseEntity with status 201 (Created) and with body the new classe, or with status 400 (Bad Request) if the classe has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/classes")
    @Timed
    public ResponseEntity<Classe> createClasse(@Valid @RequestBody Classe classe) throws URISyntaxException {
        log.debug("REST request to save Classe : {}", classe);
        if (classe.getId() != null) {
            throw new BadRequestAlertException("A new classe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Classe result = classeRepository.save(classe);
        classeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/classes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /classes : Updates an existing classe.
     *
     * @param classe the classe to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated classe,
     * or with status 400 (Bad Request) if the classe is not valid,
     * or with status 500 (Internal Server Error) if the classe couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/classes")
    @Timed
    public ResponseEntity<Classe> updateClasse(@Valid @RequestBody Classe classe) throws URISyntaxException {
        log.debug("REST request to update Classe : {}", classe);
        if (classe.getId() == null) {
            return createClasse(classe);
        }
        Classe result = classeRepository.save(classe);
        classeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, classe.getId().toString()))
            .body(result);
    }

    /**
     * GET  /classes : get all the classes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of classes in body
     */
    @GetMapping("/classes")
    @Timed
    public List<Classe> getAllClasses() {
        log.debug("REST request to get all Classes");
        return classeRepository.findAll();
        }

    /**
     * GET  /classes/:id : get the "id" classe.
     *
     * @param id the id of the classe to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the classe, or with status 404 (Not Found)
     */
    @GetMapping("/classes/{id}")
    @Timed
    public ResponseEntity<Classe> getClasse(@PathVariable Long id) {
        log.debug("REST request to get Classe : {}", id);
        Classe classe = classeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(classe));
    }

    /**
     * DELETE  /classes/:id : delete the "id" classe.
     *
     * @param id the id of the classe to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/classes/{id}")
    @Timed
    public ResponseEntity<Void> deleteClasse(@PathVariable Long id) {
        log.debug("REST request to delete Classe : {}", id);
        classeRepository.delete(id);
        classeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/classes?query=:query : search for the classe corresponding
     * to the query.
     *
     * @param query the query of the classe search
     * @return the result of the search
     */
    @GetMapping("/_search/classes")
    @Timed
    public List<Classe> searchClasses(@RequestParam String query) {
        log.debug("REST request to search Classes for query {}", query);
        return StreamSupport
            .stream(classeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
