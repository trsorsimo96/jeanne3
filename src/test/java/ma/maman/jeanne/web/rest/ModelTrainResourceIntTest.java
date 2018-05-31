package ma.maman.jeanne.web.rest;

import ma.maman.jeanne.Jeanne3App;

import ma.maman.jeanne.domain.ModelTrain;
import ma.maman.jeanne.repository.ModelTrainRepository;
import ma.maman.jeanne.repository.search.ModelTrainSearchRepository;
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
 * Test class for the ModelTrainResource REST controller.
 *
 * @see ModelTrainResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Jeanne3App.class)
public class ModelTrainResourceIntTest {

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
    private ModelTrainRepository modelTrainRepository;

    @Autowired
    private ModelTrainSearchRepository modelTrainSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restModelTrainMockMvc;

    private ModelTrain modelTrain;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ModelTrainResource modelTrainResource = new ModelTrainResource(modelTrainRepository, modelTrainSearchRepository);
        this.restModelTrainMockMvc = MockMvcBuilders.standaloneSetup(modelTrainResource)
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
    public static ModelTrain createEntity(EntityManager em) {
        ModelTrain modelTrain = new ModelTrain()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .nbNormalRange(DEFAULT_NB_NORMAL_RANGE)
            .nbSeatLeft(DEFAULT_NB_SEAT_LEFT)
            .nbSeatRight(DEFAULT_NB_SEAT_RIGHT)
            .nbSeatBefore(DEFAULT_NB_SEAT_BEFORE)
            .nbSeatBelow(DEFAULT_NB_SEAT_BELOW)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return modelTrain;
    }

    @Before
    public void initTest() {
        modelTrainSearchRepository.deleteAll();
        modelTrain = createEntity(em);
    }

    @Test
    @Transactional
    public void createModelTrain() throws Exception {
        int databaseSizeBeforeCreate = modelTrainRepository.findAll().size();

        // Create the ModelTrain
        restModelTrainMockMvc.perform(post("/api/model-trains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modelTrain)))
            .andExpect(status().isCreated());

        // Validate the ModelTrain in the database
        List<ModelTrain> modelTrainList = modelTrainRepository.findAll();
        assertThat(modelTrainList).hasSize(databaseSizeBeforeCreate + 1);
        ModelTrain testModelTrain = modelTrainList.get(modelTrainList.size() - 1);
        assertThat(testModelTrain.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testModelTrain.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testModelTrain.getNbNormalRange()).isEqualTo(DEFAULT_NB_NORMAL_RANGE);
        assertThat(testModelTrain.getNbSeatLeft()).isEqualTo(DEFAULT_NB_SEAT_LEFT);
        assertThat(testModelTrain.getNbSeatRight()).isEqualTo(DEFAULT_NB_SEAT_RIGHT);
        assertThat(testModelTrain.getNbSeatBefore()).isEqualTo(DEFAULT_NB_SEAT_BEFORE);
        assertThat(testModelTrain.getNbSeatBelow()).isEqualTo(DEFAULT_NB_SEAT_BELOW);
        assertThat(testModelTrain.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testModelTrain.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);

        // Validate the ModelTrain in Elasticsearch
        ModelTrain modelTrainEs = modelTrainSearchRepository.findOne(testModelTrain.getId());
        assertThat(modelTrainEs).isEqualToIgnoringGivenFields(testModelTrain);
    }

    @Test
    @Transactional
    public void createModelTrainWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = modelTrainRepository.findAll().size();

        // Create the ModelTrain with an existing ID
        modelTrain.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restModelTrainMockMvc.perform(post("/api/model-trains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modelTrain)))
            .andExpect(status().isBadRequest());

        // Validate the ModelTrain in the database
        List<ModelTrain> modelTrainList = modelTrainRepository.findAll();
        assertThat(modelTrainList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelTrainRepository.findAll().size();
        // set the field null
        modelTrain.setCode(null);

        // Create the ModelTrain, which fails.

        restModelTrainMockMvc.perform(post("/api/model-trains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modelTrain)))
            .andExpect(status().isBadRequest());

        List<ModelTrain> modelTrainList = modelTrainRepository.findAll();
        assertThat(modelTrainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelTrainRepository.findAll().size();
        // set the field null
        modelTrain.setName(null);

        // Create the ModelTrain, which fails.

        restModelTrainMockMvc.perform(post("/api/model-trains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modelTrain)))
            .andExpect(status().isBadRequest());

        List<ModelTrain> modelTrainList = modelTrainRepository.findAll();
        assertThat(modelTrainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNbNormalRangeIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelTrainRepository.findAll().size();
        // set the field null
        modelTrain.setNbNormalRange(null);

        // Create the ModelTrain, which fails.

        restModelTrainMockMvc.perform(post("/api/model-trains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modelTrain)))
            .andExpect(status().isBadRequest());

        List<ModelTrain> modelTrainList = modelTrainRepository.findAll();
        assertThat(modelTrainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNbSeatLeftIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelTrainRepository.findAll().size();
        // set the field null
        modelTrain.setNbSeatLeft(null);

        // Create the ModelTrain, which fails.

        restModelTrainMockMvc.perform(post("/api/model-trains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modelTrain)))
            .andExpect(status().isBadRequest());

        List<ModelTrain> modelTrainList = modelTrainRepository.findAll();
        assertThat(modelTrainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNbSeatRightIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelTrainRepository.findAll().size();
        // set the field null
        modelTrain.setNbSeatRight(null);

        // Create the ModelTrain, which fails.

        restModelTrainMockMvc.perform(post("/api/model-trains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modelTrain)))
            .andExpect(status().isBadRequest());

        List<ModelTrain> modelTrainList = modelTrainRepository.findAll();
        assertThat(modelTrainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllModelTrains() throws Exception {
        // Initialize the database
        modelTrainRepository.saveAndFlush(modelTrain);

        // Get all the modelTrainList
        restModelTrainMockMvc.perform(get("/api/model-trains?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(modelTrain.getId().intValue())))
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
    public void getModelTrain() throws Exception {
        // Initialize the database
        modelTrainRepository.saveAndFlush(modelTrain);

        // Get the modelTrain
        restModelTrainMockMvc.perform(get("/api/model-trains/{id}", modelTrain.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(modelTrain.getId().intValue()))
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
    public void getNonExistingModelTrain() throws Exception {
        // Get the modelTrain
        restModelTrainMockMvc.perform(get("/api/model-trains/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateModelTrain() throws Exception {
        // Initialize the database
        modelTrainRepository.saveAndFlush(modelTrain);
        modelTrainSearchRepository.save(modelTrain);
        int databaseSizeBeforeUpdate = modelTrainRepository.findAll().size();

        // Update the modelTrain
        ModelTrain updatedModelTrain = modelTrainRepository.findOne(modelTrain.getId());
        // Disconnect from session so that the updates on updatedModelTrain are not directly saved in db
        em.detach(updatedModelTrain);
        updatedModelTrain
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .nbNormalRange(UPDATED_NB_NORMAL_RANGE)
            .nbSeatLeft(UPDATED_NB_SEAT_LEFT)
            .nbSeatRight(UPDATED_NB_SEAT_RIGHT)
            .nbSeatBefore(UPDATED_NB_SEAT_BEFORE)
            .nbSeatBelow(UPDATED_NB_SEAT_BELOW)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restModelTrainMockMvc.perform(put("/api/model-trains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedModelTrain)))
            .andExpect(status().isOk());

        // Validate the ModelTrain in the database
        List<ModelTrain> modelTrainList = modelTrainRepository.findAll();
        assertThat(modelTrainList).hasSize(databaseSizeBeforeUpdate);
        ModelTrain testModelTrain = modelTrainList.get(modelTrainList.size() - 1);
        assertThat(testModelTrain.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testModelTrain.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testModelTrain.getNbNormalRange()).isEqualTo(UPDATED_NB_NORMAL_RANGE);
        assertThat(testModelTrain.getNbSeatLeft()).isEqualTo(UPDATED_NB_SEAT_LEFT);
        assertThat(testModelTrain.getNbSeatRight()).isEqualTo(UPDATED_NB_SEAT_RIGHT);
        assertThat(testModelTrain.getNbSeatBefore()).isEqualTo(UPDATED_NB_SEAT_BEFORE);
        assertThat(testModelTrain.getNbSeatBelow()).isEqualTo(UPDATED_NB_SEAT_BELOW);
        assertThat(testModelTrain.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testModelTrain.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);

        // Validate the ModelTrain in Elasticsearch
        ModelTrain modelTrainEs = modelTrainSearchRepository.findOne(testModelTrain.getId());
        assertThat(modelTrainEs).isEqualToIgnoringGivenFields(testModelTrain);
    }

    @Test
    @Transactional
    public void updateNonExistingModelTrain() throws Exception {
        int databaseSizeBeforeUpdate = modelTrainRepository.findAll().size();

        // Create the ModelTrain

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restModelTrainMockMvc.perform(put("/api/model-trains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modelTrain)))
            .andExpect(status().isCreated());

        // Validate the ModelTrain in the database
        List<ModelTrain> modelTrainList = modelTrainRepository.findAll();
        assertThat(modelTrainList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteModelTrain() throws Exception {
        // Initialize the database
        modelTrainRepository.saveAndFlush(modelTrain);
        modelTrainSearchRepository.save(modelTrain);
        int databaseSizeBeforeDelete = modelTrainRepository.findAll().size();

        // Get the modelTrain
        restModelTrainMockMvc.perform(delete("/api/model-trains/{id}", modelTrain.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean modelTrainExistsInEs = modelTrainSearchRepository.exists(modelTrain.getId());
        assertThat(modelTrainExistsInEs).isFalse();

        // Validate the database is empty
        List<ModelTrain> modelTrainList = modelTrainRepository.findAll();
        assertThat(modelTrainList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchModelTrain() throws Exception {
        // Initialize the database
        modelTrainRepository.saveAndFlush(modelTrain);
        modelTrainSearchRepository.save(modelTrain);

        // Search the modelTrain
        restModelTrainMockMvc.perform(get("/api/_search/model-trains?query=id:" + modelTrain.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(modelTrain.getId().intValue())))
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
        TestUtil.equalsVerifier(ModelTrain.class);
        ModelTrain modelTrain1 = new ModelTrain();
        modelTrain1.setId(1L);
        ModelTrain modelTrain2 = new ModelTrain();
        modelTrain2.setId(modelTrain1.getId());
        assertThat(modelTrain1).isEqualTo(modelTrain2);
        modelTrain2.setId(2L);
        assertThat(modelTrain1).isNotEqualTo(modelTrain2);
        modelTrain1.setId(null);
        assertThat(modelTrain1).isNotEqualTo(modelTrain2);
    }
}
