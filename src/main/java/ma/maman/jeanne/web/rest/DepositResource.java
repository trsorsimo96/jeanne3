package ma.maman.jeanne.web.rest;

import com.codahale.metrics.annotation.Timed;
import ma.maman.jeanne.domain.Deposit;

import ma.maman.jeanne.repository.DepositRepository;
import ma.maman.jeanne.repository.search.DepositSearchRepository;
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
 * REST controller for managing Deposit.
 */
@RestController
@RequestMapping("/api")
public class DepositResource {

    private final Logger log = LoggerFactory.getLogger(DepositResource.class);

    private static final String ENTITY_NAME = "deposit";

    private final DepositRepository depositRepository;

    private final DepositSearchRepository depositSearchRepository;

    public DepositResource(DepositRepository depositRepository, DepositSearchRepository depositSearchRepository) {
        this.depositRepository = depositRepository;
        this.depositSearchRepository = depositSearchRepository;
    }

    /**
     * POST  /deposits : Create a new deposit.
     *
     * @param deposit the deposit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new deposit, or with status 400 (Bad Request) if the deposit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/deposits")
    @Timed
    public ResponseEntity<Deposit> createDeposit(@Valid @RequestBody Deposit deposit) throws URISyntaxException {
        log.debug("REST request to save Deposit : {}", deposit);
        if (deposit.getId() != null) {
            throw new BadRequestAlertException("A new deposit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Deposit result = depositRepository.save(deposit);
        depositSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/deposits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /deposits : Updates an existing deposit.
     *
     * @param deposit the deposit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated deposit,
     * or with status 400 (Bad Request) if the deposit is not valid,
     * or with status 500 (Internal Server Error) if the deposit couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/deposits")
    @Timed
    public ResponseEntity<Deposit> updateDeposit(@Valid @RequestBody Deposit deposit) throws URISyntaxException {
        log.debug("REST request to update Deposit : {}", deposit);
        if (deposit.getId() == null) {
            return createDeposit(deposit);
        }
        Deposit result = depositRepository.save(deposit);
        depositSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, deposit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /deposits : get all the deposits.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of deposits in body
     */
    @GetMapping("/deposits")
    @Timed
    public List<Deposit> getAllDeposits() {
        log.debug("REST request to get all Deposits");
        return depositRepository.findAll();
        }

    /**
     * GET  /deposits/:id : get the "id" deposit.
     *
     * @param id the id of the deposit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the deposit, or with status 404 (Not Found)
     */
    @GetMapping("/deposits/{id}")
    @Timed
    public ResponseEntity<Deposit> getDeposit(@PathVariable Long id) {
        log.debug("REST request to get Deposit : {}", id);
        Deposit deposit = depositRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(deposit));
    }

    /**
     * DELETE  /deposits/:id : delete the "id" deposit.
     *
     * @param id the id of the deposit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/deposits/{id}")
    @Timed
    public ResponseEntity<Void> deleteDeposit(@PathVariable Long id) {
        log.debug("REST request to delete Deposit : {}", id);
        depositRepository.delete(id);
        depositSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/deposits?query=:query : search for the deposit corresponding
     * to the query.
     *
     * @param query the query of the deposit search
     * @return the result of the search
     */
    @GetMapping("/_search/deposits")
    @Timed
    public List<Deposit> searchDeposits(@RequestParam String query) {
        log.debug("REST request to search Deposits for query {}", query);
        return StreamSupport
            .stream(depositSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
