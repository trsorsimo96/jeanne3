package ma.maman.jeanne.web.rest;

import ma.maman.jeanne.Jeanne3App;

import ma.maman.jeanne.domain.Wagon;
import ma.maman.jeanne.domain.ModelTrain;
import ma.maman.jeanne.repository.WagonRepository;
import ma.maman.jeanne.repository.search.WagonSearchRepository;
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
 * Test class for the WagonResource REST controller.
 *
 * @see WagonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Jeanne3App.class)
public class WagonResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_PLACE = 1;
    private static final Integer UPDATED_PLACE = 2;

    @Autowired
    private WagonRepository wagonRepository;

    @Autowired
    private WagonSearchRepository wagonSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWagonMockMvc;

    private Wagon wagon;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WagonResource wagonResource = new WagonResource(wagonRepository, wagonSearchRepository);
        this.restWagonMockMvc = MockMvcBuilders.standaloneSetup(wagonResource)
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
    public static Wagon createEntity(EntityManager em) {
        Wagon wagon = new Wagon()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .place(DEFAULT_PLACE);
        // Add required entity
        ModelTrain model = ModelTrainResourceIntTest.createEntity(em);
        em.persist(model);
        em.flush();
        wagon.setModel(model);
        return wagon;
    }

    @Before
    public void initTest() {
        wagonSearchRepository.deleteAll();
        wagon = createEntity(em);
    }

    @Test
    @Transactional
    public void createWagon() throws Exception {
        int databaseSizeBeforeCreate = wagonRepository.findAll().size();

        // Create the Wagon
        restWagonMockMvc.perform(post("/api/wagons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wagon)))
            .andExpect(status().isCreated());

        // Validate the Wagon in the database
        List<Wagon> wagonList = wagonRepository.findAll();
        assertThat(wagonList).hasSize(databaseSizeBeforeCreate + 1);
        Wagon testWagon = wagonList.get(wagonList.size() - 1);
        assertThat(testWagon.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testWagon.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWagon.getPlace()).isEqualTo(DEFAULT_PLACE);

        // Validate the Wagon in Elasticsearch
        Wagon wagonEs = wagonSearchRepository.findOne(testWagon.getId());
        assertThat(wagonEs).isEqualToIgnoringGivenFields(testWagon);
    }

    @Test
    @Transactional
    public void createWagonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wagonRepository.findAll().size();

        // Create the Wagon with an existing ID
        wagon.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWagonMockMvc.perform(post("/api/wagons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wagon)))
            .andExpect(status().isBadRequest());

        // Validate the Wagon in the database
        List<Wagon> wagonList = wagonRepository.findAll();
        assertThat(wagonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = wagonRepository.findAll().size();
        // set the field null
        wagon.setCode(null);

        // Create the Wagon, which fails.

        restWagonMockMvc.perform(post("/api/wagons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wagon)))
            .andExpect(status().isBadRequest());

        List<Wagon> wagonList = wagonRepository.findAll();
        assertThat(wagonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = wagonRepository.findAll().size();
        // set the field null
        wagon.setName(null);

        // Create the Wagon, which fails.

        restWagonMockMvc.perform(post("/api/wagons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wagon)))
            .andExpect(status().isBadRequest());

        List<Wagon> wagonList = wagonRepository.findAll();
        assertThat(wagonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWagons() throws Exception {
        // Initialize the database
        wagonRepository.saveAndFlush(wagon);

        // Get all the wagonList
        restWagonMockMvc.perform(get("/api/wagons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wagon.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].place").value(hasItem(DEFAULT_PLACE)));
    }

    @Test
    @Transactional
    public void getWagon() throws Exception {
        // Initialize the database
        wagonRepository.saveAndFlush(wagon);

        // Get the wagon
        restWagonMockMvc.perform(get("/api/wagons/{id}", wagon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wagon.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.place").value(DEFAULT_PLACE));
    }

    @Test
    @Transactional
    public void getNonExistingWagon() throws Exception {
        // Get the wagon
        restWagonMockMvc.perform(get("/api/wagons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWagon() throws Exception {
        // Initialize the database
        wagonRepository.saveAndFlush(wagon);
        wagonSearchRepository.save(wagon);
        int databaseSizeBeforeUpdate = wagonRepository.findAll().size();

        // Update the wagon
        Wagon updatedWagon = wagonRepository.findOne(wagon.getId());
        // Disconnect from session so that the updates on updatedWagon are not directly saved in db
        em.detach(updatedWagon);
        updatedWagon
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .place(UPDATED_PLACE);

        restWagonMockMvc.perform(put("/api/wagons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWagon)))
            .andExpect(status().isOk());

        // Validate the Wagon in the database
        List<Wagon> wagonList = wagonRepository.findAll();
        assertThat(wagonList).hasSize(databaseSizeBeforeUpdate);
        Wagon testWagon = wagonList.get(wagonList.size() - 1);
        assertThat(testWagon.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testWagon.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWagon.getPlace()).isEqualTo(UPDATED_PLACE);

        // Validate the Wagon in Elasticsearch
        Wagon wagonEs = wagonSearchRepository.findOne(testWagon.getId());
        assertThat(wagonEs).isEqualToIgnoringGivenFields(testWagon);
    }

    @Test
    @Transactional
    public void updateNonExistingWagon() throws Exception {
        int databaseSizeBeforeUpdate = wagonRepository.findAll().size();

        // Create the Wagon

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWagonMockMvc.perform(put("/api/wagons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wagon)))
            .andExpect(status().isCreated());

        // Validate the Wagon in the database
        List<Wagon> wagonList = wagonRepository.findAll();
        assertThat(wagonList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWagon() throws Exception {
        // Initialize the database
        wagonRepository.saveAndFlush(wagon);
        wagonSearchRepository.save(wagon);
        int databaseSizeBeforeDelete = wagonRepository.findAll().size();

        // Get the wagon
        restWagonMockMvc.perform(delete("/api/wagons/{id}", wagon.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean wagonExistsInEs = wagonSearchRepository.exists(wagon.getId());
        assertThat(wagonExistsInEs).isFalse();

        // Validate the database is empty
        List<Wagon> wagonList = wagonRepository.findAll();
        assertThat(wagonList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWagon() throws Exception {
        // Initialize the database
        wagonRepository.saveAndFlush(wagon);
        wagonSearchRepository.save(wagon);

        // Search the wagon
        restWagonMockMvc.perform(get("/api/_search/wagons?query=id:" + wagon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wagon.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].place").value(hasItem(DEFAULT_PLACE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Wagon.class);
        Wagon wagon1 = new Wagon();
        wagon1.setId(1L);
        Wagon wagon2 = new Wagon();
        wagon2.setId(wagon1.getId());
        assertThat(wagon1).isEqualTo(wagon2);
        wagon2.setId(2L);
        assertThat(wagon1).isNotEqualTo(wagon2);
        wagon1.setId(null);
        assertThat(wagon1).isNotEqualTo(wagon2);
    }
}
