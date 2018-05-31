package ma.maman.jeanne.web.rest;

import com.codahale.metrics.annotation.Timed;
import ma.maman.jeanne.domain.Month;

import ma.maman.jeanne.repository.MonthRepository;
import ma.maman.jeanne.repository.search.MonthSearchRepository;
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
 * REST controller for managing Month.
 */
@RestController
@RequestMapping("/api")
public class MonthResource {

    private final Logger log = LoggerFactory.getLogger(MonthResource.class);

    private static final String ENTITY_NAME = "month";

    private final MonthRepository monthRepository;

    private final MonthSearchRepository monthSearchRepository;

    public MonthResource(MonthRepository monthRepository, MonthSearchRepository monthSearchRepository) {
        this.monthRepository = monthRepository;
        this.monthSearchRepository = monthSearchRepository;
    }

    /**
     * POST  /months : Create a new month.
     *
     * @param month the month to create
     * @return the ResponseEntity with status 201 (Created) and with body the new month, or with status 400 (Bad Request) if the month has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/months")
    @Timed
    public ResponseEntity<Month> createMonth(@Valid @RequestBody Month month) throws URISyntaxException {
        log.debug("REST request to save Month : {}", month);
        if (month.getId() != null) {
            throw new BadRequestAlertException("A new month cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Month result = monthRepository.save(month);
        monthSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/months/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /months : Updates an existing month.
     *
     * @param month the month to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated month,
     * or with status 400 (Bad Request) if the month is not valid,
     * or with status 500 (Internal Server Error) if the month couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/months")
    @Timed
    public ResponseEntity<Month> updateMonth(@Valid @RequestBody Month month) throws URISyntaxException {
        log.debug("REST request to update Month : {}", month);
        if (month.getId() == null) {
            return createMonth(month);
        }
        Month result = monthRepository.save(month);
        monthSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, month.getId().toString()))
            .body(result);
    }

    /**
     * GET  /months : get all the months.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of months in body
     */
    @GetMapping("/months")
    @Timed
    public List<Month> getAllMonths() {
        log.debug("REST request to get all Months");
        return monthRepository.findAll();
        }

    /**
     * GET  /months/:id : get the "id" month.
     *
     * @param id the id of the month to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the month, or with status 404 (Not Found)
     */
    @GetMapping("/months/{id}")
    @Timed
    public ResponseEntity<Month> getMonth(@PathVariable Long id) {
        log.debug("REST request to get Month : {}", id);
        Month month = monthRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(month));
    }

    /**
     * DELETE  /months/:id : delete the "id" month.
     *
     * @param id the id of the month to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/months/{id}")
    @Timed
    public ResponseEntity<Void> deleteMonth(@PathVariable Long id) {
        log.debug("REST request to delete Month : {}", id);
        monthRepository.delete(id);
        monthSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/months?query=:query : search for the month corresponding
     * to the query.
     *
     * @param query the query of the month search
     * @return the result of the search
     */
    @GetMapping("/_search/months")
    @Timed
    public List<Month> searchMonths(@RequestParam String query) {
        log.debug("REST request to search Months for query {}", query);
        return StreamSupport
            .stream(monthSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
