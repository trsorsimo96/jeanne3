package ma.maman.jeanne.web.rest;

import ma.maman.jeanne.Jeanne3App;

import ma.maman.jeanne.domain.Passenger;
import ma.maman.jeanne.domain.Booking;
import ma.maman.jeanne.repository.PassengerRepository;
import ma.maman.jeanne.repository.search.PassengerSearchRepository;
import ma.maman.jeanne.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static ma.maman.jeanne.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ma.maman.jeanne.domain.enumeration.TypePassenger;
import ma.maman.jeanne.domain.enumeration.TitlePassenger;
/**
 * Test class for the PassengerResource REST controller.
 *
 * @see PassengerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Jeanne3App.class)
public class PassengerResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final String DEFAULT_CNI = "AAAAAAAAAA";
    private static final String UPDATED_CNI = "BBBBBBBBBB";

    private static final TypePassenger DEFAULT_TYPE = TypePassenger.ADT;
    private static final TypePassenger UPDATED_TYPE = TypePassenger.CHD;

    private static final TitlePassenger DEFAULT_TITLE = TitlePassenger.MR;
    private static final TitlePassenger UPDATED_TITLE = TitlePassenger.MRS;

    private static final String DEFAULT_TICKET_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TICKET_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_SEAT_NUMBER = 1;
    private static final Integer UPDATED_SEAT_NUMBER = 2;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private PassengerSearchRepository passengerSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPassengerMockMvc;

    private Passenger passenger;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PassengerResource passengerResource = new PassengerResource(passengerRepository, passengerSearchRepository);
        this.restPassengerMockMvc = MockMvcBuilders.standaloneSetup(passengerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Passenger createEntity(EntityManager em) {
        Passenger passenger = new Passenger()
            .name(DEFAULT_NAME)
            .surname(DEFAULT_SURNAME)
            .cni(DEFAULT_CNI)
            .type(DEFAULT_TYPE)
            .title(DEFAULT_TITLE)
            .ticketNumber(DEFAULT_TICKET_NUMBER)
            .seatNumber(DEFAULT_SEAT_NUMBER);
        // Add required entity
        Booking booking = BookingResourceIntTest.createEntity(em);
        em.persist(booking);
        em.flush();
        passenger.setBooking(booking);
        return passenger;
    }

    @Before
    public void initTest() {
        passengerSearchRepository.deleteAll();
        passenger = createEntity(em);
    }

    @Test
    @Transactional
    public void createPassenger() throws Exception {
        int databaseSizeBeforeCreate = passengerRepository.findAll().size();

        // Create the Passenger
        restPassengerMockMvc.perform(post("/api/passengers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isCreated());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeCreate + 1);
        Passenger testPassenger = passengerList.get(passengerList.size() - 1);
        assertThat(testPassenger.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPassenger.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testPassenger.getCni()).isEqualTo(DEFAULT_CNI);
        assertThat(testPassenger.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPassenger.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPassenger.getTicketNumber()).isEqualTo(DEFAULT_TICKET_NUMBER);
        assertThat(testPassenger.getSeatNumber()).isEqualTo(DEFAULT_SEAT_NUMBER);

        // Validate the Passenger in Elasticsearch
        Passenger passengerEs = passengerSearchRepository.findOne(testPassenger.getId());
        assertThat(passengerEs).isEqualToIgnoringGivenFields(testPassenger);
    }

    @Test
    @Transactional
    public void createPassengerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = passengerRepository.findAll().size();

        // Create the Passenger with an existing ID
        passenger.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPassengerMockMvc.perform(post("/api/passengers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isBadRequest());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = passengerRepository.findAll().size();
        // set the field null
        passenger.setName(null);

        // Create the Passenger, which fails.

        restPassengerMockMvc.perform(post("/api/passengers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isBadRequest());

        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSurnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = passengerRepository.findAll().size();
        // set the field null
        passenger.setSurname(null);

        // Create the Passenger, which fails.

        restPassengerMockMvc.perform(post("/api/passengers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isBadRequest());

        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = passengerRepository.findAll().size();
        // set the field null
        passenger.setType(null);

        // Create the Passenger, which fails.

        restPassengerMockMvc.perform(post("/api/passengers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isBadRequest());

        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPassengers() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        // Get all the passengerList
        restPassengerMockMvc.perform(get("/api/passengers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(passenger.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME.toString())))
            .andExpect(jsonPath("$.[*].cni").value(hasItem(DEFAULT_CNI.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].ticketNumber").value(hasItem(DEFAULT_TICKET_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].seatNumber").value(hasItem(DEFAULT_SEAT_NUMBER)));
    }

    @Test
    @Transactional
    public void getPassenger() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        // Get the passenger
        restPassengerMockMvc.perform(get("/api/passengers/{id}", passenger.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(passenger.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME.toString()))
            .andExpect(jsonPath("$.cni").value(DEFAULT_CNI.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.ticketNumber").value(DEFAULT_TICKET_NUMBER.toString()))
            .andExpect(jsonPath("$.seatNumber").value(DEFAULT_SEAT_NUMBER));
    }

    @Test
    @Transactional
    public void getNonExistingPassenger() throws Exception {
        // Get the passenger
        restPassengerMockMvc.perform(get("/api/passengers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePassenger() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);
        passengerSearchRepository.save(passenger);
        int databaseSizeBeforeUpdate = passengerRepository.findAll().size();

        // Update the passenger
        Passenger updatedPassenger = passengerRepository.findOne(passenger.getId());
        // Disconnect from session so that the updates on updatedPassenger are not directly saved in db
        em.detach(updatedPassenger);
        updatedPassenger
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .cni(UPDATED_CNI)
            .type(UPDATED_TYPE)
            .title(UPDATED_TITLE)
            .ticketNumber(UPDATED_TICKET_NUMBER)
            .seatNumber(UPDATED_SEAT_NUMBER);

        restPassengerMockMvc.perform(put("/api/passengers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPassenger)))
            .andExpect(status().isOk());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeUpdate);
        Passenger testPassenger = passengerList.get(passengerList.size() - 1);
        assertThat(testPassenger.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPassenger.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testPassenger.getCni()).isEqualTo(UPDATED_CNI);
        assertThat(testPassenger.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPassenger.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPassenger.getTicketNumber()).isEqualTo(UPDATED_TICKET_NUMBER);
        assertThat(testPassenger.getSeatNumber()).isEqualTo(UPDATED_SEAT_NUMBER);

        // Validate the Passenger in Elasticsearch
        Passenger passengerEs = passengerSearchRepository.findOne(testPassenger.getId());
        assertThat(passengerEs).isEqualToIgnoringGivenFields(testPassenger);
    }

    @Test
    @Transactional
    public void updateNonExistingPassenger() throws Exception {
        int databaseSizeBeforeUpdate = passengerRepository.findAll().size();

        // Create the Passenger

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPassengerMockMvc.perform(put("/api/passengers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isCreated());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePassenger() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);
        passengerSearchRepository.save(passenger);
        int databaseSizeBeforeDelete = passengerRepository.findAll().size();

        // Get the passenger
        restPassengerMockMvc.perform(delete("/api/passengers/{id}", passenger.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean passengerExistsInEs = passengerSearchRepository.exists(passenger.getId());
        assertThat(passengerExistsInEs).isFalse();

        // Validate the database is empty
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPassenger() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);
        passengerSearchRepository.save(passenger);

        // Search the passenger
        restPassengerMockMvc.perform(get("/api/_search/passengers?query=id:" + passenger.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(passenger.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME.toString())))
            .andExpect(jsonPath("$.[*].cni").value(hasItem(DEFAULT_CNI.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].ticketNumber").value(hasItem(DEFAULT_TICKET_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].seatNumber").value(hasItem(DEFAULT_SEAT_NUMBER)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Passenger.class);
        Passenger passenger1 = new Passenger();
        passenger1.setId(1L);
        Passenger passenger2 = new Passenger();
        passenger2.setId(passenger1.getId());
        assertThat(passenger1).isEqualTo(passenger2);
        passenger2.setId(2L);
        assertThat(passenger1).isNotEqualTo(passenger2);
        passenger1.setId(null);
        assertThat(passenger1).isNotEqualTo(passenger2);
    }
}
