package ma.maman.jeanne.web.rest;

import ma.maman.jeanne.Jeanne3App;

import ma.maman.jeanne.domain.ModelCar;
import ma.maman.jeanne.repository.ModelCarRepository;
import ma.maman.jeanne.repository.search.ModelCarSearchRepository;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static ma.maman.jeanne.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ModelCarResource REST controller.
 *
 * @see ModelCarResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Jeanne3App.class)
public class ModelCarResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_NB_NORMAL_RANGE = 1;
    private static final Integer UPDATED_NB_NORMAL_RANGE = 2;

    private static final Integer DEFAULT_NB_SEAT_LEFT = 1;
    private static final Integer UPDATED_NB_SEAT_LEFT = 2;

    private static final Integer DEFAULT_NB_SEAT_RIGHT = 1;
    private static final Integer UPDATED_NB_SEAT_RIGHT = 2;

    private static final Integer DEFAULT_NB_SEAT_BEFORE = 1;
    private static final Integer UPDATED_NB_SEAT_BEFORE = 2;

    private static final Integer DEFAULT_NB_SEAT_BELOW = 1;
    private static final Integer UPDATED_NB_SEAT_BELOW = 2;

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    @Autowired
    private ModelCarRepository modelCarRepository;

    @Autowired
    private ModelCarSearchRepository modelCarSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restModelCarMockMvc;

    private ModelCar modelCar;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ModelCarResource modelCarResource = new ModelCarResource(modelCarRepository, modelCarSearchRepository);
        this.restModelCarMockMvc = MockMvcBuilders.standaloneSetup(modelCarResource)
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
    public static ModelCar createEntity(EntityManager em) {
        ModelCar modelCar = new ModelCar()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .nbNormalRange(DEFAULT_NB_NORMAL_RANGE)
            .nbSeatLeft(DEFAULT_NB_SEAT_LEFT)
            .nbSeatRight(DEFAULT_NB_SEAT_RIGHT)
            .nbSeatBefore(DEFAULT_NB_SEAT_BEFORE)
            .nbSeatBelow(DEFAULT_NB_SEAT_BELOW)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return modelCar;
    }

    @Before
    public void initTest() {
        modelCarSearchRepository.deleteAll();
        modelCar = createEntity(em);
    }

    @Test
    @Transactional
    public void createModelCar() throws Exception {
        int databaseSizeBeforeCreate = modelCarRepository.findAll().size();

        // Create the ModelCar
        restModelCarMockMvc.perform(post("/api/model-cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modelCar)))
            .andExpect(status().isCreated());

        // Validate the ModelCar in the database
        List<ModelCar> modelCarList = modelCarRepository.findAll();
        assertThat(modelCarList).hasSize(databaseSizeBeforeCreate + 1);
        ModelCar testModelCar = modelCarList.get(modelCarList.size() - 1);
        assertThat(testModelCar.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testModelCar.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testModelCar.getNbNormalRange()).isEqualTo(DEFAULT_NB_NORMAL_RANGE);
        assertThat(testModelCar.getNbSeatLeft()).isEqualTo(DEFAULT_NB_SEAT_LEFT);
        assertThat(testModelCar.getNbSeatRight()).isEqualTo(DEFAULT_NB_SEAT_RIGHT);
        assertThat(testModelCar.getNbSeatBefore()).isEqualTo(DEFAULT_NB_SEAT_BEFORE);
        assertThat(testModelCar.getNbSeatBelow()).isEqualTo(DEFAULT_NB_SEAT_BELOW);
        assertThat(testModelCar.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testModelCar.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);

        // Validate the ModelCar in Elasticsearch
        ModelCar modelCarEs = modelCarSearchRepository.findOne(testModelCar.getId());
        assertThat(modelCarEs).isEqualToIgnoringGivenFields(testModelCar);
    }

    @Test
    @Transactional
    public void createModelCarWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = modelCarRepository.findAll().size();

        // Create the ModelCar with an existing ID
        modelCar.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restModelCarMockMvc.perform(post("/api/model-cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modelCar)))
            .andExpect(status().isBadRequest());

        // Validate the ModelCar in the database
        List<ModelCar> modelCarList = modelCarRepository.findAll();
        assertThat(modelCarList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelCarRepository.findAll().size();
        // set the field null
        modelCar.setCode(null);

        // Create the ModelCar, which fails.

        restModelCarMockMvc.perform(post("/api/model-cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modelCar)))
            .andExpect(status().isBadRequest());

        List<ModelCar> modelCarList = modelCarRepository.findAll();
        assertThat(modelCarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelCarRepository.findAll().size();
        // set the field null
        modelCar.setName(null);

        // Create the ModelCar, which fails.

        restModelCarMockMvc.perform(post("/api/model-cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modelCar)))
            .andExpect(status().isBadRequest());

        List<ModelCar> modelCarList = modelCarRepository.findAll();
        assertThat(modelCarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNbNormalRangeIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelCarRepository.findAll().size();
        // set the field null
        modelCar.setNbNormalRange(null);

        // Create the ModelCar, which fails.

        restModelCarMockMvc.perform(post("/api/model-cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modelCar)))
            .andExpect(status().isBadRequest());

        List<ModelCar> modelCarList = modelCarRepository.findAll();
        assertThat(modelCarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNbSeatLeftIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelCarRepository.findAll().size();
        // set the field null
        modelCar.setNbSeatLeft(null);

        // Create the ModelCar, which fails.

        restModelCarMockMvc.perform(post("/api/model-cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modelCar)))
            .andExpect(status().isBadRequest());

        List<ModelCar> modelCarList = modelCarRepository.findAll();
        assertThat(modelCarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNbSeatRightIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelCarRepository.findAll().size();
        // set the field null
        modelCar.setNbSeatRight(null);

        // Create the ModelCar, which fails.

        restModelCarMockMvc.perform(post("/api/model-cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modelCar)))
            .andExpect(status().isBadRequest());

        List<ModelCar> modelCarList = modelCarRepository.findAll();
        assertThat(modelCarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllModelCars() throws Exception {
        // Initialize the database
        modelCarRepository.saveAndFlush(modelCar);

        // Get all the modelCarList
        restModelCarMockMvc.perform(get("/api/model-cars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(modelCar.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].nbNormalRange").value(hasItem(DEFAULT_NB_NORMAL_RANGE)))
            .andExpect(jsonPath("$.[*].nbSeatLeft").value(hasItem(DEFAULT_NB_SEAT_LEFT)))
            .andExpect(jsonPath("$.[*].nbSeatRight").value(hasItem(DEFAULT_NB_SEAT_RIGHT)))
            .andExpect(jsonPath("$.[*].nbSeatBefore").value(hasItem(DEFAULT_NB_SEAT_BEFORE)))
            .andExpect(jsonPath("$.[*].nbSeatBelow").value(hasItem(DEFAULT_NB_SEAT_BELOW)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    public void getModelCar() throws Exception {
        // Initialize the database
        modelCarRepository.saveAndFlush(modelCar);

        // Get the modelCar
        restModelCarMockMvc.perform(get("/api/model-cars/{id}", modelCar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(modelCar.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.nbNormalRange").value(DEFAULT_NB_NORMAL_RANGE))
            .andExpect(jsonPath("$.nbSeatLeft").value(DEFAULT_NB_SEAT_LEFT))
            .andExpect(jsonPath("$.nbSeatRight").value(DEFAULT_NB_SEAT_RIGHT))
            .andExpect(jsonPath("$.nbSeatBefore").value(DEFAULT_NB_SEAT_BEFORE))
            .andExpect(jsonPath("$.nbSeatBelow").value(DEFAULT_NB_SEAT_BELOW))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    public void getNonExistingModelCar() throws Exception {
        // Get the modelCar
        restModelCarMockMvc.perform(get("/api/model-cars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateModelCar() throws Exception {
        // Initialize the database
        modelCarRepository.saveAndFlush(modelCar);
        modelCarSearchRepository.save(modelCar);
        int databaseSizeBeforeUpdate = modelCarRepository.findAll().size();

        // Update the modelCar
        ModelCar updatedModelCar = modelCarRepository.findOne(modelCar.getId());
        // Disconnect from session so that the updates on updatedModelCar are not directly saved in db
        em.detach(updatedModelCar);
        updatedModelCar
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .nbNormalRange(UPDATED_NB_NORMAL_RANGE)
            .nbSeatLeft(UPDATED_NB_SEAT_LEFT)
            .nbSeatRight(UPDATED_NB_SEAT_RIGHT)
            .nbSeatBefore(UPDATED_NB_SEAT_BEFORE)
            .nbSeatBelow(UPDATED_NB_SEAT_BELOW)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restModelCarMockMvc.perform(put("/api/model-cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedModelCar)))
            .andExpect(status().isOk());

        // Validate the ModelCar in the database
        List<ModelCar> modelCarList = modelCarRepository.findAll();
        assertThat(modelCarList).hasSize(databaseSizeBeforeUpdate);
        ModelCar testModelCar = modelCarList.get(modelCarList.size() - 1);
        assertThat(testModelCar.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testModelCar.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testModelCar.getNbNormalRange()).isEqualTo(UPDATED_NB_NORMAL_RANGE);
        assertThat(testModelCar.getNbSeatLeft()).isEqualTo(UPDATED_NB_SEAT_LEFT);
        assertThat(testModelCar.getNbSeatRight()).isEqualTo(UPDATED_NB_SEAT_RIGHT);
        assertThat(testModelCar.getNbSeatBefore()).isEqualTo(UPDATED_NB_SEAT_BEFORE);
        assertThat(testModelCar.getNbSeatBelow()).isEqualTo(UPDATED_NB_SEAT_BELOW);
        assertThat(testModelCar.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testModelCar.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);

        // Validate the ModelCar in Elasticsearch
        ModelCar modelCarEs = modelCarSearchRepository.findOne(testModelCar.getId());
        assertThat(modelCarEs).isEqualToIgnoringGivenFields(testModelCar);
    }

    @Test
    @Transactional
    public void updateNonExistingModelCar() throws Exception {
        int databaseSizeBeforeUpdate = modelCarRepository.findAll().size();

        // Create the ModelCar

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restModelCarMockMvc.perform(put("/api/model-cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modelCar)))
            .andExpect(status().isCreated());

        // Validate the ModelCar in the database
        List<ModelCar> modelCarList = modelCarRepository.findAll();
        assertThat(modelCarList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteModelCar() throws Exception {
        // Initialize the database
        modelCarRepository.saveAndFlush(modelCar);
        modelCarSearchRepository.save(modelCar);
        int databaseSizeBeforeDelete = modelCarRepository.findAll().size();

        // Get the modelCar
        restModelCarMockMvc.perform(delete("/api/model-cars/{id}", modelCar.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean modelCarExistsInEs = modelCarSearchRepository.exists(modelCar.getId());
        assertThat(modelCarExistsInEs).isFalse();

        // Validate the database is empty
        List<ModelCar> modelCarList = modelCarRepository.findAll();
        assertThat(modelCarList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchModelCar() throws Exception {
        // Initialize the database
        modelCarRepository.saveAndFlush(modelCar);
        modelCarSearchRepository.save(modelCar);

        // Search the modelCar
        restModelCarMockMvc.perform(get("/api/_search/model-cars?query=id:" + modelCar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(modelCar.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].nbNormalRange").value(hasItem(DEFAULT_NB_NORMAL_RANGE)))
            .andExpect(jsonPath("$.[*].nbSeatLeft").value(hasItem(DEFAULT_NB_SEAT_LEFT)))
            .andExpect(jsonPath("$.[*].nbSeatRight").value(hasItem(DEFAULT_NB_SEAT_RIGHT)))
            .andExpect(jsonPath("$.[*].nbSeatBefore").value(hasItem(DEFAULT_NB_SEAT_BEFORE)))
            .andExpect(jsonPath("$.[*].nbSeatBelow").value(hasItem(DEFAULT_NB_SEAT_BELOW)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModelCar.class);
        ModelCar modelCar1 = new ModelCar();
        modelCar1.setId(1L);
        ModelCar modelCar2 = new ModelCar();
        modelCar2.setId(modelCar1.getId());
        assertThat(modelCar1).isEqualTo(modelCar2);
        modelCar2.setId(2L);
        assertThat(modelCar1).isNotEqualTo(modelCar2);
        modelCar1.setId(null);
        assertThat(modelCar1).isNotEqualTo(modelCar2);
    }
}
