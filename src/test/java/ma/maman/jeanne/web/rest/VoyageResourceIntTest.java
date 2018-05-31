package ma.maman.jeanne.web.rest;

import ma.maman.jeanne.Jeanne3App;

import ma.maman.jeanne.domain.Voyage;
import ma.maman.jeanne.domain.Car;
import ma.maman.jeanne.domain.Train;
import ma.maman.jeanne.repository.VoyageRepository;
import ma.maman.jeanne.repository.search.VoyageSearchRepository;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static ma.maman.jeanne.web.rest.TestUtil.sameInstant;
import static ma.maman.jeanne.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ma.maman.jeanne.domain.enumeration.TypeVoyage;
import ma.maman.jeanne.domain.enumeration.StateVoyage;
/**
 * Test class for the VoyageResource REST controller.
 *
 * @see VoyageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Jeanne3App.class)
public class VoyageResourceIntTest {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATEDEPART = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATEDEPART = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATEARRIVE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATEARRIVE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final TypeVoyage DEFAULT_TYPE = TypeVoyage.SCHEDULED;
    private static final TypeVoyage UPDATED_TYPE = TypeVoyage.INSTANTLY;

    private static final StateVoyage DEFAULT_STATE = StateVoyage.OPEN;
    private static final StateVoyage UPDATED_STATE = StateVoyage.FULL;

    @Autowired
    private VoyageRepository voyageRepository;

    @Autowired
    private VoyageSearchRepository voyageSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVoyageMockMvc;

    private Voyage voyage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VoyageResource voyageResource = new VoyageResource(voyageRepository, voyageSearchRepository);
        this.restVoyageMockMvc = MockMvcBuilders.standaloneSetup(voyageResource)
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
    public static Voyage createEntity(EntityManager em) {
        Voyage voyage = new Voyage()
            .numero(DEFAULT_NUMERO)
            .datedepart(DEFAULT_DATEDEPART)
            .datearrive(DEFAULT_DATEARRIVE)
            .type(DEFAULT_TYPE)
            .state(DEFAULT_STATE);
        // Add required entity
        Car car = CarResourceIntTest.createEntity(em);
        em.persist(car);
        em.flush();
        voyage.setCar(car);
        // Add required entity
        Train train = TrainResourceIntTest.createEntity(em);
        em.persist(train);
        em.flush();
        voyage.setTrain(train);
        return voyage;
    }

    @Before
    public void initTest() {
        voyageSearchRepository.deleteAll();
        voyage = createEntity(em);
    }

    @Test
    @Transactional
    public void createVoyage() throws Exception {
        int databaseSizeBeforeCreate = voyageRepository.findAll().size();

        // Create the Voyage
        restVoyageMockMvc.perform(post("/api/voyages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voyage)))
            .andExpect(status().isCreated());

        // Validate the Voyage in the database
        List<Voyage> voyageList = voyageRepository.findAll();
        assertThat(voyageList).hasSize(databaseSizeBeforeCreate + 1);
        Voyage testVoyage = voyageList.get(voyageList.size() - 1);
        assertThat(testVoyage.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testVoyage.getDatedepart()).isEqualTo(DEFAULT_DATEDEPART);
        assertThat(testVoyage.getDatearrive()).isEqualTo(DEFAULT_DATEARRIVE);
        assertThat(testVoyage.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testVoyage.getState()).isEqualTo(DEFAULT_STATE);

        // Validate the Voyage in Elasticsearch
        Voyage voyageEs = voyageSearchRepository.findOne(testVoyage.getId());
        assertThat(testVoyage.getDatedepart()).isEqualTo(testVoyage.getDatedepart());
        assertThat(testVoyage.getDatearrive()).isEqualTo(testVoyage.getDatearrive());
        assertThat(voyageEs).isEqualToIgnoringGivenFields(testVoyage, "datedepart", "datearrive");
    }

    @Test
    @Transactional
    public void createVoyageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = voyageRepository.findAll().size();

        // Create the Voyage with an existing ID
        voyage.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVoyageMockMvc.perform(post("/api/voyages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voyage)))
            .andExpect(status().isBadRequest());

        // Validate the Voyage in the database
        List<Voyage> voyageList = voyageRepository.findAll();
        assertThat(voyageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = voyageRepository.findAll().size();
        // set the field null
        voyage.setNumero(null);

        // Create the Voyage, which fails.

        restVoyageMockMvc.perform(post("/api/voyages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voyage)))
            .andExpect(status().isBadRequest());

        List<Voyage> voyageList = voyageRepository.findAll();
        assertThat(voyageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = voyageRepository.findAll().size();
        // set the field null
        voyage.setType(null);

        // Create the Voyage, which fails.

        restVoyageMockMvc.perform(post("/api/voyages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voyage)))
            .andExpect(status().isBadRequest());

        List<Voyage> voyageList = voyageRepository.findAll();
        assertThat(voyageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVoyages() throws Exception {
        // Initialize the database
        voyageRepository.saveAndFlush(voyage);

        // Get all the voyageList
        restVoyageMockMvc.perform(get("/api/voyages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(voyage.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO.toString())))
            .andExpect(jsonPath("$.[*].datedepart").value(hasItem(sameInstant(DEFAULT_DATEDEPART))))
            .andExpect(jsonPath("$.[*].datearrive").value(hasItem(sameInstant(DEFAULT_DATEARRIVE))))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }

    @Test
    @Transactional
    public void getVoyage() throws Exception {
        // Initialize the database
        voyageRepository.saveAndFlush(voyage);

        // Get the voyage
        restVoyageMockMvc.perform(get("/api/voyages/{id}", voyage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(voyage.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO.toString()))
            .andExpect(jsonPath("$.datedepart").value(sameInstant(DEFAULT_DATEDEPART)))
            .andExpect(jsonPath("$.datearrive").value(sameInstant(DEFAULT_DATEARRIVE)))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVoyage() throws Exception {
        // Get the voyage
        restVoyageMockMvc.perform(get("/api/voyages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVoyage() throws Exception {
        // Initialize the database
        voyageRepository.saveAndFlush(voyage);
        voyageSearchRepository.save(voyage);
        int databaseSizeBeforeUpdate = voyageRepository.findAll().size();

        // Update the voyage
        Voyage updatedVoyage = voyageRepository.findOne(voyage.getId());
        // Disconnect from session so that the updates on updatedVoyage are not directly saved in db
        em.detach(updatedVoyage);
        updatedVoyage
            .numero(UPDATED_NUMERO)
            .datedepart(UPDATED_DATEDEPART)
            .datearrive(UPDATED_DATEARRIVE)
            .type(UPDATED_TYPE)
            .state(UPDATED_STATE);

        restVoyageMockMvc.perform(put("/api/voyages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVoyage)))
            .andExpect(status().isOk());

        // Validate the Voyage in the database
        List<Voyage> voyageList = voyageRepository.findAll();
        assertThat(voyageList).hasSize(databaseSizeBeforeUpdate);
        Voyage testVoyage = voyageList.get(voyageList.size() - 1);
        assertThat(testVoyage.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testVoyage.getDatedepart()).isEqualTo(UPDATED_DATEDEPART);
        assertThat(testVoyage.getDatearrive()).isEqualTo(UPDATED_DATEARRIVE);
        assertThat(testVoyage.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testVoyage.getState()).isEqualTo(UPDATED_STATE);

        // Validate the Voyage in Elasticsearch
        Voyage voyageEs = voyageSearchRepository.findOne(testVoyage.getId());
        assertThat(testVoyage.getDatedepart()).isEqualTo(testVoyage.getDatedepart());
        assertThat(testVoyage.getDatearrive()).isEqualTo(testVoyage.getDatearrive());
        assertThat(voyageEs).isEqualToIgnoringGivenFields(testVoyage, "datedepart", "datearrive");
    }

    @Test
    @Transactional
    public void updateNonExistingVoyage() throws Exception {
        int databaseSizeBeforeUpdate = voyageRepository.findAll().size();

        // Create the Voyage

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVoyageMockMvc.perform(put("/api/voyages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voyage)))
            .andExpect(status().isCreated());

        // Validate the Voyage in the database
        List<Voyage> voyageList = voyageRepository.findAll();
        assertThat(voyageList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVoyage() throws Exception {
        // Initialize the database
        voyageRepository.saveAndFlush(voyage);
        voyageSearchRepository.save(voyage);
        int databaseSizeBeforeDelete = voyageRepository.findAll().size();

        // Get the voyage
        restVoyageMockMvc.perform(delete("/api/voyages/{id}", voyage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean voyageExistsInEs = voyageSearchRepository.exists(voyage.getId());
        assertThat(voyageExistsInEs).isFalse();

        // Validate the database is empty
        List<Voyage> voyageList = voyageRepository.findAll();
        assertThat(voyageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchVoyage() throws Exception {
        // Initialize the database
        voyageRepository.saveAndFlush(voyage);
        voyageSearchRepository.save(voyage);

        // Search the voyage
        restVoyageMockMvc.perform(get("/api/_search/voyages?query=id:" + voyage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(voyage.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO.toString())))
            .andExpect(jsonPath("$.[*].datedepart").value(hasItem(sameInstant(DEFAULT_DATEDEPART))))
            .andExpect(jsonPath("$.[*].datearrive").value(hasItem(sameInstant(DEFAULT_DATEARRIVE))))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Voyage.class);
        Voyage voyage1 = new Voyage();
        voyage1.setId(1L);
        Voyage voyage2 = new Voyage();
        voyage2.setId(voyage1.getId());
        assertThat(voyage1).isEqualTo(voyage2);
        voyage2.setId(2L);
        assertThat(voyage1).isNotEqualTo(voyage2);
        voyage1.setId(null);
        assertThat(voyage1).isNotEqualTo(voyage2);
    }
}
