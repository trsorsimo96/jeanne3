package ma.maman.jeanne.web.rest;

import com.codahale.metrics.annotation.Timed;
import ma.maman.jeanne.domain.Routes;

import ma.maman.jeanne.repository.RoutesRepository;
import ma.maman.jeanne.repository.search.RoutesSearchRepository;
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
 * REST controller for managing Routes.
 */
@RestController
@RequestMapping("/api")
public class RoutesResource {

    private final Logger log = LoggerFactory.getLogger(RoutesResource.class);

    private static final String ENTITY_NAME = "routes";

    private final RoutesRepository routesRepository;

    private final RoutesSearchRepository routesSearchRepository;

    public RoutesResource(RoutesRepository routesRepository, RoutesSearchRepository routesSearchRepository) {
        this.routesRepository = routesRepository;
        this.routesSearchRepository = routesSearchRepository;
    }

    /**
     * POST  /routes : Create a new routes.
     *
     * @param routes the routes to create
     * @return the ResponseEntity with status 201 (Created) and with body the new routes, or with status 400 (Bad Request) if the routes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/routes")
    @Timed
    public ResponseEntity<Routes> createRoutes(@Valid @RequestBody Routes routes) throws URISyntaxException {
        log.debug("REST request to save Routes : {}", routes);
        if (routes.getId() != null) {
            throw new BadRequestAlertException("A new routes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Routes result = routesRepository.save(routes);
        routesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/routes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /routes : Updates an existing routes.
     *
     * @param routes the routes to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated routes,
     * or with status 400 (Bad Request) if the routes is not valid,
     * or with status 500 (Internal Server Error) if the routes couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/routes")
    @Timed
    public ResponseEntity<Routes> updateRoutes(@Valid @RequestBody Routes routes) throws URISyntaxException {
        log.debug("REST request to update Routes : {}", routes);
        if (routes.getId() == null) {
            return createRoutes(routes);
        }
        Routes result = routesRepository.save(routes);
        routesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, routes.getId().toString()))
            .body(result);
    }

    /**
     * GET  /routes : get all the routes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of routes in body
     */
    @GetMapping("/routes")
    @Timed
    public List<Routes> getAllRoutes() {
        log.debug("REST request to get all Routes");
        return routesRepository.findAll();
        }

    /**
     * GET  /routes/:id : get the "id" routes.
     *
     * @param id the id of the routes to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the routes, or with status 404 (Not Found)
     */
    @GetMapping("/routes/{id}")
    @Timed
    public ResponseEntity<Routes> getRoutes(@PathVariable Long id) {
        log.debug("REST request to get Routes : {}", id);
        Routes routes = routesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(routes));
    }

    /**
     * DELETE  /routes/:id : delete the "id" routes.
     *
     * @param id the id of the routes to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/routes/{id}")
    @Timed
    public ResponseEntity<Void> deleteRoutes(@PathVariable Long id) {
        log.debug("REST request to delete Routes : {}", id);
        routesRepository.delete(id);
        routesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/routes?query=:query : search for the routes corresponding
     * to the query.
     *
     * @param query the query of the routes search
     * @return the result of the search
     */
    @GetMapping("/_search/routes")
    @Timed
    public List<Routes> searchRoutes(@RequestParam String query) {
        log.debug("REST request to search Routes for query {}", query);
        return StreamSupport
            .stream(routesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
