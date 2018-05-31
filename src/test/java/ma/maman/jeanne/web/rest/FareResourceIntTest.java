package ma.maman.jeanne.web.rest;

import ma.maman.jeanne.Jeanne3App;

import ma.maman.jeanne.domain.Fare;
import ma.maman.jeanne.domain.Booking;
import ma.maman.jeanne.repository.FareRepository;
import ma.maman.jeanne.repository.search.FareSearchRepository;
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

/**
 * Test class for the FareResource REST controller.
 *
 * @see FareResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Jeanne3App.class)
public class FareResourceIntTest {

    private static final Integer DEFAULT_SEGMENT_NUMBER = 1;
    private static final Integer UPDATED_SEGMENT_NUMBER = 2;

    private static final Integer DEFAULT_PRICE = 1;
    private static final Integer UPDATED_PRICE = 2;

    @Autowired
    private FareRepository fareRepository;

    @Autowired
    private FareSearchRepository fareSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFareMockMvc;

    private Fare fare;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FareResource fareResource = new FareResource(fareRepository, fareSearchRepository);
        this.restFareMockMvc = MockMvcBuilders.standaloneSetup(fareResource)
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
    public static Fare createEntity(EntityManager em) {
        Fare fare = new Fare()
            .segmentNumber(DEFAULT_SEGMENT_NUMBER)
            .price(DEFAULT_PRICE);
        // Add required entity
        Booking booking = BookingResourceIntTest.createEntity(em);
        em.persist(booking);
        em.flush();
        fare.setBooking(booking);
        return fare;
    }

    @Before
    public void initTest() {
        fareSearchRepository.deleteAll();
        fare = createEntity(em);
    }

    @Test
    @Transactional
    public void createFare() throws Exception {
        int databaseSizeBeforeCreate = fareRepository.findAll().size();

        // Create the Fare
        restFareMockMvc.perform(post("/api/fares")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fare)))
            .andExpect(status().isCreated());

        // Validate the Fare in the database
        List<Fare> fareList = fareRepository.findAll();
        assertThat(fareList).hasSize(databaseSizeBeforeCreate + 1);
        Fare testFare = fareList.get(fareList.size() - 1);
        assertThat(testFare.getSegmentNumber()).isEqualTo(DEFAULT_SEGMENT_NUMBER);
        assertThat(testFare.getPrice()).isEqualTo(DEFAULT_PRICE);

        // Validate the Fare in Elasticsearch
        Fare fareEs = fareSearchRepository.findOne(testFare.getId());
        assertThat(fareEs).isEqualToIgnoringGivenFields(testFare);
    }

    @Test
    @Transactional
    public void createFareWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fareRepository.findAll().size();

        // Create the Fare with an existing ID
        fare.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFareMockMvc.perform(post("/api/fares")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fare)))
            .andExpect(status().isBadRequest());

        // Validate the Fare in the database
        List<Fare> fareList = fareRepository.findAll();
        assertThat(fareList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSegmentNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = fareRepository.findAll().size();
        // set the field null
        fare.setSegmentNumber(null);

        // Create the Fare, which fails.

        restFareMockMvc.perform(post("/api/fares")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fare)))
            .andExpect(status().isBadRequest());

        List<Fare> fareList = fareRepository.findAll();
        assertThat(fareList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = fareRepository.findAll().size();
        // set the field null
        fare.setPrice(null);

        // Create the Fare, which fails.

        restFareMockMvc.perform(post("/api/fares")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fare)))
            .andExpect(status().isBadRequest());

        List<Fare> fareList = fareRepository.findAll();
        assertThat(fareList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFares() throws Exception {
        // Initialize the database
        fareRepository.saveAndFlush(fare);

        // Get all the fareList
        restFareMockMvc.perform(get("/api/fares?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fare.getId().intValue())))
            .andExpect(jsonPath("$.[*].segmentNumber").value(hasItem(DEFAULT_SEGMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)));
    }

    @Test
    @Transactional
    public void getFare() throws Exception {
        // Initialize the database
        fareRepository.saveAndFlush(fare);

        // Get the fare
        restFareMockMvc.perform(get("/api/fares/{id}", fare.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fare.getId().intValue()))
            .andExpect(jsonPath("$.segmentNumber").value(DEFAULT_SEGMENT_NUMBER))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE));
    }

    @Test
    @Transactional
    public void getNonExistingFare() throws Exception {
        // Get the fare
        restFareMockMvc.perform(get("/api/fares/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFare() throws Exception {
        // Initialize the database
        fareRepository.saveAndFlush(fare);
        fareSearchRepository.save(fare);
        int databaseSizeBeforeUpdate = fareRepository.findAll().size();

        // Update the fare
        Fare updatedFare = fareRepository.findOne(fare.getId());
        // Disconnect from session so that the updates on updatedFare are not directly saved in db
        em.detach(updatedFare);
        updatedFare
            .segmentNumber(UPDATED_SEGMENT_NUMBER)
            .price(UPDATED_PRICE);

        restFareMockMvc.perform(put("/api/fares")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFare)))
            .andExpect(status().isOk());

        // Validate the Fare in the database
        List<Fare> fareList = fareRepository.findAll();
        assertThat(fareList).hasSize(databaseSizeBeforeUpdate);
        Fare testFare = fareList.get(fareList.size() - 1);
        assertThat(testFare.getSegmentNumber()).isEqualTo(UPDATED_SEGMENT_NUMBER);
        assertThat(testFare.getPrice()).isEqualTo(UPDATED_PRICE);

        // Validate the Fare in Elasticsearch
        Fare fareEs = fareSearchRepository.findOne(testFare.getId());
        assertThat(fareEs).isEqualToIgnoringGivenFields(testFare);
    }

    @Test
    @Transactional
    public void updateNonExistingFare() throws Exception {
        int databaseSizeBeforeUpdate = fareRepository.findAll().size();

        // Create the Fare

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFareMockMvc.perform(put("/api/fares")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fare)))
            .andExpect(status().isCreated());

        // Validate the Fare in the database
        List<Fare> fareList = fareRepository.findAll();
        assertThat(fareList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFare() throws Exception {
        // Initialize the database
        fareRepository.saveAndFlush(fare);
        fareSearchRepository.save(fare);
        int databaseSizeBeforeDelete = fareRepository.findAll().size();

        // Get the fare
        restFareMockMvc.perform(delete("/api/fares/{id}", fare.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean fareExistsInEs = fareSearchRepository.exists(fare.getId());
        assertThat(fareExistsInEs).isFalse();

        // Validate the database is empty
        List<Fare> fareList = fareRepository.findAll();
        assertThat(fareList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFare() throws Exception {
        // Initialize the database
        fareRepository.saveAndFlush(fare);
        fareSearchRepository.save(fare);

        // Search the fare
        restFareMockMvc.perform(get("/api/_search/fares?query=id:" + fare.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fare.getId().intValue())))
            .andExpect(jsonPath("$.[*].segmentNumber").value(hasItem(DEFAULT_SEGMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fare.class);
        Fare fare1 = new Fare();
        fare1.setId(1L);
        Fare fare2 = new Fare();
        fare2.setId(fare1.getId());
        assertThat(fare1).isEqualTo(fare2);
        fare2.setId(2L);
        assertThat(fare1).isNotEqualTo(fare2);
        fare1.setId(null);
        assertThat(fare1).isNotEqualTo(fare2);
    }
}
