package ma.maman.jeanne.web.rest;

import com.codahale.metrics.annotation.Timed;
import ma.maman.jeanne.domain.Booking;

import ma.maman.jeanne.repository.BookingRepository;
import ma.maman.jeanne.repository.search.BookingSearchRepository;
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
 * REST controller for managing Booking.
 */
@RestController
@RequestMapping("/api")
public class BookingResource {

    private final Logger log = LoggerFactory.getLogger(BookingResource.class);

    private static final String ENTITY_NAME = "booking";

    private final BookingRepository bookingRepository;

    private final BookingSearchRepository bookingSearchRepository;

    public BookingResource(BookingRepository bookingRepository, BookingSearchRepository bookingSearchRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingSearchRepository = bookingSearchRepository;
    }

    /**
     * POST  /bookings : Create a new booking.
     *
     * @param booking the booking to create
     * @return the ResponseEntity with status 201 (Created) and with body the new booking, or with status 400 (Bad Request) if the booking has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bookings")
    @Timed
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody Booking booking) throws URISyntaxException {
        log.debug("REST request to save Booking : {}", booking);
        if (booking.getId() != null) {
            throw new BadRequestAlertException("A new booking cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Booking result = bookingRepository.save(booking);
        bookingSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/bookings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bookings : Updates an existing booking.
     *
     * @param booking the booking to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated booking,
     * or with status 400 (Bad Request) if the booking is not valid,
     * or with status 500 (Internal Server Error) if the booking couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bookings")
    @Timed
    public ResponseEntity<Booking> updateBooking(@Valid @RequestBody Booking booking) throws URISyntaxException {
        log.debug("REST request to update Booking : {}", booking);
        if (booking.getId() == null) {
            return createBooking(booking);
        }
        Booking result = bookingRepository.save(booking);
        bookingSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, booking.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bookings : get all the bookings.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of bookings in body
     */
    @GetMapping("/bookings")
    @Timed
    public List<Booking> getAllBookings() {
        log.debug("REST request to get all Bookings");
        return bookingRepository.findAll();
        }

    /**
     * GET  /bookings/:id : get the "id" booking.
     *
     * @param id the id of the booking to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the booking, or with status 404 (Not Found)
     */
    @GetMapping("/bookings/{id}")
    @Timed
    public ResponseEntity<Booking> getBooking(@PathVariable Long id) {
        log.debug("REST request to get Booking : {}", id);
        Booking booking = bookingRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(booking));
    }

    /**
     * DELETE  /bookings/:id : delete the "id" booking.
     *
     * @param id the id of the booking to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bookings/{id}")
    @Timed
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        log.debug("REST request to delete Booking : {}", id);
        bookingRepository.delete(id);
        bookingSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/bookings?query=:query : search for the booking corresponding
     * to the query.
     *
     * @param query the query of the booking search
     * @return the result of the search
     */
    @GetMapping("/_search/bookings")
    @Timed
    public List<Booking> searchBookings(@RequestParam String query) {
        log.debug("REST request to search Bookings for query {}", query);
        return StreamSupport
            .stream(bookingSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
