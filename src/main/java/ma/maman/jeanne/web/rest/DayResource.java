package ma.maman.jeanne.web.rest;

import com.codahale.metrics.annotation.Timed;
import ma.maman.jeanne.domain.Day;

import ma.maman.jeanne.repository.DayRepository;
import ma.maman.jeanne.repository.search.DaySearchRepository;
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
 * REST controller for managing Day.
 */
@RestController
@RequestMapping("/api")
public class DayResource {

    private final Logger log = LoggerFactory.getLogger(DayResource.class);

    private static final String ENTITY_NAME = "day";

    private final DayRepository dayRepository;

    private final DaySearchRepository daySearchRepository;

    public DayResource(DayRepository dayRepository, DaySearchRepository daySearchRepository) {
        this.dayRepository = dayRepository;
        this.daySearchRepository = daySearchRepository;
    }

    /**
     * POST  /days : Create a new day.
     *
     * @param day the day to create
     * @return the ResponseEntity with status 201 (Created) and with body the new day, or with status 400 (Bad Request) if the day has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/days")
    @Timed
    public ResponseEntity<Day> createDay(@Valid @RequestBody Day day) throws URISyntaxException {
        log.debug("REST request to save Day : {}", day);
        if (day.getId() != null) {
            throw new BadRequestAlertException("A new day cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Day result = dayRepository.save(day);
        daySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/days/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /days : Updates an existing day.
     *
     * @param day the day to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated day,
     * or with status 400 (Bad Request) if the day is not valid,
     * or with status 500 (Internal Server Error) if the day couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/days")
    @Timed
    public ResponseEntity<Day> updateDay(@Valid @RequestBody Day day) throws URISyntaxException {
        log.debug("REST request to update Day : {}", day);
        if (day.getId() == null) {
            return createDay(day);
        }
        Day result = dayRepository.save(day);
        daySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, day.getId().toString()))
            .body(result);
    }

    /**
     * GET  /days : get all the days.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of days in body
     */
    @GetMapping("/days")
    @Timed
    public List<Day> getAllDays() {
        log.debug("REST request to get all Days");
        return dayRepository.findAll();
        }

    /**
     * GET  /days/:id : get the "id" day.
     *
     * @param id the id of the day to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the day, or with status 404 (Not Found)
     */
    @GetMapping("/days/{id}")
    @Timed
    public ResponseEntity<Day> getDay(@PathVariable Long id) {
        log.debug("REST request to get Day : {}", id);
        Day day = dayRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(day));
    }

    /**
     * DELETE  /days/:id : delete the "id" day.
     *
     * @param id the id of the day to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/days/{id}")
    @Timed
    public ResponseEntity<Void> deleteDay(@PathVariable Long id) {
        log.debug("REST request to delete Day : {}", id);
        dayRepository.delete(id);
        daySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/days?query=:query : search for the day corresponding
     * to the query.
     *
     * @param query the query of the day search
     * @return the result of the search
     */
    @GetMapping("/_search/days")
    @Timed
    public List<Day> searchDays(@RequestParam String query) {
        log.debug("REST request to search Days for query {}", query);
        return StreamSupport
            .stream(daySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
