package ma.maman.jeanne.web.rest;

import ma.maman.jeanne.Jeanne3App;

import ma.maman.jeanne.domain.Train;
import ma.maman.jeanne.domain.Company;
import ma.maman.jeanne.repository.TrainRepository;
import ma.maman.jeanne.repository.search.TrainSearchRepository;
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
 * Test class for the TrainResource REST controller.
 *
 * @see TrainResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Jeanne3App.class)
public class TrainResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_PLACE = 1;
    private static final Integer UPDATED_PLACE = 2;

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private TrainSearchRepository trainSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTrainMockMvc;

    private Train train;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TrainResource trainResource = new TrainResource(trainRepository, trainSearchRepository);
        this.restTrainMockMvc = MockMvcBuilders.standaloneSetup(trainResource)
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
    public static Train createEntity(EntityManager em) {
        Train train = new Train()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .place(DEFAULT_PLACE);
        // Add required entity
        Company company = CompanyResourceIntTest.createEntity(em);
        em.persist(company);
        em.flush();
        train.setCompany(company);
        return train;
    }

    @Before
    public void initTest() {
        trainSearchRepository.deleteAll();
        train = createEntity(em);
    }

    @Test
    @Transactional
    public void createTrain() throws Exception {
        int databaseSizeBeforeCreate = trainRepository.findAll().size();

        // Create the Train
        restTrainMockMvc.perform(post("/api/trains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(train)))
            .andExpect(status().isCreated());

        // Validate the Train in the database
        List<Train> trainList = trainRepository.findAll();
        assertThat(trainList).hasSize(databaseSizeBeforeCreate + 1);
        Train testTrain = trainList.get(trainList.size() - 1);
        assertThat(testTrain.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTrain.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTrain.getPlace()).isEqualTo(DEFAULT_PLACE);

        // Validate the Train in Elasticsearch
        Train trainEs = trainSearchRepository.findOne(testTrain.getId());
        assertThat(trainEs).isEqualToIgnoringGivenFields(testTrain);
    }

    @Test
    @Transactional
    public void createTrainWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = trainRepository.findAll().size();

        // Create the Train with an existing ID
        train.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrainMockMvc.perform(post("/api/trains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(train)))
            .andExpect(status().isBadRequest());

        // Validate the Train in the database
        List<Train> trainList = trainRepository.findAll();
        assertThat(trainList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = trainRepository.findAll().size();
        // set the field null
        train.setCode(null);

        // Create the Train, which fails.

        restTrainMockMvc.perform(post("/api/trains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(train)))
            .andExpect(status().isBadRequest());

        List<Train> trainList = trainRepository.findAll();
        assertThat(trainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = trainRepository.findAll().size();
        // set the field null
        train.setName(null);

        // Create the Train, which fails.

        restTrainMockMvc.perform(post("/api/trains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(train)))
            .andExpect(status().isBadRequest());

        List<Train> trainList = trainRepository.findAll();
        assertThat(trainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTrains() throws Exception {
        // Initialize the database
        trainRepository.saveAndFlush(train);

        // Get all the trainList
        restTrainMockMvc.perform(get("/api/trains?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(train.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].place").value(hasItem(DEFAULT_PLACE)));
    }

    @Test
    @Transactional
    public void getTrain() throws Exception {
        // Initialize the database
        trainRepository.saveAndFlush(train);

        // Get the train
        restTrainMockMvc.perform(get("/api/trains/{id}", train.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(train.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.place").value(DEFAULT_PLACE));
    }

    @Test
    @Transactional
    public void getNonExistingTrain() throws Exception {
        // Get the train
        restTrainMockMvc.perform(get("/api/trains/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrain() throws Exception {
        // Initialize the database
        trainRepository.saveAndFlush(train);
        trainSearchRepository.save(train);
        int databaseSizeBeforeUpdate = trainRepository.findAll().size();

        // Update the train
        Train updatedTrain = trainRepository.findOne(train.getId());
        // Disconnect from session so that the updates on updatedTrain are not directly saved in db
        em.detach(updatedTrain);
        updatedTrain
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .place(UPDATED_PLACE);

        restTrainMockMvc.perform(put("/api/trains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTrain)))
            .andExpect(status().isOk());

        // Validate the Train in the database
        List<Train> trainList = trainRepository.findAll();
        assertThat(trainList).hasSize(databaseSizeBeforeUpdate);
        Train testTrain = trainList.get(trainList.size() - 1);
        assertThat(testTrain.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTrain.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTrain.getPlace()).isEqualTo(UPDATED_PLACE);

        // Validate the Train in Elasticsearch
        Train trainEs = trainSearchRepository.findOne(testTrain.getId());
        assertThat(trainEs).isEqualToIgnoringGivenFields(testTrain);
    }

    @Test
    @Transactional
    public void updateNonExistingTrain() throws Exception {
        int databaseSizeBeforeUpdate = trainRepository.findAll().size();

        // Create the Train

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTrainMockMvc.perform(put("/api/trains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(train)))
            .andExpect(status().isCreated());

        // Validate the Train in the database
        List<Train> trainList = trainRepository.findAll();
        assertThat(trainList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTrain() throws Exception {
        // Initialize the database
        trainRepository.saveAndFlush(train);
        trainSearchRepository.save(train);
        int databaseSizeBeforeDelete = trainRepository.findAll().size();

        // Get the train
        restTrainMockMvc.perform(delete("/api/trains/{id}", train.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean trainExistsInEs = trainSearchRepository.exists(train.getId());
        assertThat(trainExistsInEs).isFalse();

        // Validate the database is empty
        List<Train> trainList = trainRepository.findAll();
        assertThat(trainList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTrain() throws Exception {
        // Initialize the database
        trainRepository.saveAndFlush(train);
        trainSearchRepository.save(train);

        // Search the train
        restTrainMockMvc.perform(get("/api/_search/trains?query=id:" + train.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(train.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].place").value(hasItem(DEFAULT_PLACE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Train.class);
        Train train1 = new Train();
        train1.setId(1L);
        Train train2 = new Train();
        train2.setId(train1.getId());
        assertThat(train1).isEqualTo(train2);
        train2.setId(2L);
        assertThat(train1).isNotEqualTo(train2);
        train1.setId(null);
        assertThat(train1).isNotEqualTo(train2);
    }
}
