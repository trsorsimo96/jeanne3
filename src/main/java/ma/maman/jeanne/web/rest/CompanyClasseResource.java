package ma.maman.jeanne.web.rest;

import com.codahale.metrics.annotation.Timed;
import ma.maman.jeanne.domain.CompanyClasse;

import ma.maman.jeanne.repository.CompanyClasseRepository;
import ma.maman.jeanne.repository.search.CompanyClasseSearchRepository;
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
 * REST controller for managing CompanyClasse.
 */
@RestController
@RequestMapping("/api")
public class CompanyClasseResource {

    private final Logger log = LoggerFactory.getLogger(CompanyClasseResource.class);

    private static final String ENTITY_NAME = "companyClasse";

    private final CompanyClasseRepository companyClasseRepository;

    private final CompanyClasseSearchRepository companyClasseSearchRepository;

    public CompanyClasseResource(CompanyClasseRepository companyClasseRepository, CompanyClasseSearchRepository companyClasseSearchRepository) {
        this.companyClasseRepository = companyClasseRepository;
        this.companyClasseSearchRepository = companyClasseSearchRepository;
    }

    /**
     * POST  /company-classes : Create a new companyClasse.
     *
     * @param companyClasse the companyClasse to create
     * @return the ResponseEntity with status 201 (Created) and with body the new companyClasse, or with status 400 (Bad Request) if the companyClasse has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/company-classes")
    @Timed
    public ResponseEntity<CompanyClasse> createCompanyClasse(@RequestBody CompanyClasse companyClasse) throws URISyntaxException {
        log.debug("REST request to save CompanyClasse : {}", companyClasse);
        if (companyClasse.getId() != null) {
            throw new BadRequestAlertException("A new companyClasse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompanyClasse result = companyClasseRepository.save(companyClasse);
        companyClasseSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/company-classes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /company-classes : Updates an existing companyClasse.
     *
     * @param companyClasse the companyClasse to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated companyClasse,
     * or with status 400 (Bad Request) if the companyClasse is not valid,
     * or with status 500 (Internal Server Error) if the companyClasse couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/company-classes")
    @Timed
    public ResponseEntity<CompanyClasse> updateCompanyClasse(@RequestBody CompanyClasse companyClasse) throws URISyntaxException {
        log.debug("REST request to update CompanyClasse : {}", companyClasse);
        if (companyClasse.getId() == null) {
            return createCompanyClasse(companyClasse);
        }
        CompanyClasse result = companyClasseRepository.save(companyClasse);
        companyClasseSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, companyClasse.getId().toString()))
            .body(result);
    }

    /**
     * GET  /company-classes : get all the companyClasses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of companyClasses in body
     */
    @GetMapping("/company-classes")
    @Timed
    public List<CompanyClasse> getAllCompanyClasses() {
        log.debug("REST request to get all CompanyClasses");
        return companyClasseRepository.findAll();
        }

    /**
     * GET  /company-classes/:id : get the "id" companyClasse.
     *
     * @param id the id of the companyClasse to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the companyClasse, or with status 404 (Not Found)
     */
    @GetMapping("/company-classes/{id}")
    @Timed
    public ResponseEntity<CompanyClasse> getCompanyClasse(@PathVariable Long id) {
        log.debug("REST request to get CompanyClasse : {}", id);
        CompanyClasse companyClasse = companyClasseRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(companyClasse));
    }

    /**
     * DELETE  /company-classes/:id : delete the "id" companyClasse.
     *
     * @param id the id of the companyClasse to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/company-classes/{id}")
    @Timed
    public ResponseEntity<Void> deleteCompanyClasse(@PathVariable Long id) {
        log.debug("REST request to delete CompanyClasse : {}", id);
        companyClasseRepository.delete(id);
        companyClasseSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/company-classes?query=:query : search for the companyClasse corresponding
     * to the query.
     *
     * @param query the query of the companyClasse search
     * @return the result of the search
     */
    @GetMapping("/_search/company-classes")
    @Timed
    public List<CompanyClasse> searchCompanyClasses(@RequestParam String query) {
        log.debug("REST request to search CompanyClasses for query {}", query);
        return StreamSupport
            .stream(companyClasseSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
