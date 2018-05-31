package ma.maman.jeanne.web.rest;

import ma.maman.jeanne.Jeanne3App;

import ma.maman.jeanne.domain.FeesAgency;
import ma.maman.jeanne.repository.FeesAgencyRepository;
import ma.maman.jeanne.repository.search.FeesAgencySearchRepository;
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
 * Test class for the FeesAgencyResource REST controller.
 *
 * @see FeesAgencyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Jeanne3App.class)
public class FeesAgencyResourceIntTest {

    private static final Integer DEFAULT_FEES = 1;
    private static final Integer UPDATED_FEES = 2;

    @Autowired
    private FeesAgencyRepository feesAgencyRepository;

    @Autowired
    private FeesAgencySearchRepository feesAgencySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFeesAgencyMockMvc;

    private FeesAgency feesAgency;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FeesAgencyResource feesAgencyResource = new FeesAgencyResource(feesAgencyRepository, feesAgencySearchRepository);
        this.restFeesAgencyMockMvc = MockMvcBuilders.standaloneSetup(feesAgencyResource)
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
    public static FeesAgency createEntity(EntityManager em) {
        FeesAgency feesAgency = new FeesAgency()
            .fees(DEFAULT_FEES);
        return feesAgency;
    }

    @Before
    public void initTest() {
        feesAgencySearchRepository.deleteAll();
        feesAgency = createEntity(em);
    }

    @Test
    @Transactional
    public void createFeesAgency() throws Exception {
        int databaseSizeBeforeCreate = feesAgencyRepository.findAll().size();

        // Create the FeesAgency
        restFeesAgencyMockMvc.perform(post("/api/fees-agencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(feesAgency)))
            .andExpect(status().isCreated());

        // Validate the FeesAgency in the database
        List<FeesAgency> feesAgencyList = feesAgencyRepository.findAll();
        assertThat(feesAgencyList).hasSize(databaseSizeBeforeCreate + 1);
        FeesAgency testFeesAgency = feesAgencyList.get(feesAgencyList.size() - 1);
        assertThat(testFeesAgency.getFees()).isEqualTo(DEFAULT_FEES);

        // Validate the FeesAgency in Elasticsearch
        FeesAgency feesAgencyEs = feesAgencySearchRepository.findOne(testFeesAgency.getId());
        assertThat(feesAgencyEs).isEqualToIgnoringGivenFields(testFeesAgency);
    }

    @Test
    @Transactional
    public void createFeesAgencyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = feesAgencyRepository.findAll().size();

        // Create the FeesAgency with an existing ID
        feesAgency.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeesAgencyMockMvc.perform(post("/api/fees-agencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(feesAgency)))
            .andExpect(status().isBadRequest());

        // Validate the FeesAgency in the database
        List<FeesAgency> feesAgencyList = feesAgencyRepository.findAll();
        assertThat(feesAgencyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFeesAgencies() throws Exception {
        // Initialize the database
        feesAgencyRepository.saveAndFlush(feesAgency);

        // Get all the feesAgencyList
        restFeesAgencyMockMvc.perform(get("/api/fees-agencies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feesAgency.getId().intValue())))
            .andExpect(jsonPath("$.[*].fees").value(hasItem(DEFAULT_FEES)));
    }

    @Test
    @Transactional
    public void getFeesAgency() throws Exception {
        // Initialize the database
        feesAgencyRepository.saveAndFlush(feesAgency);

        // Get the feesAgency
        restFeesAgencyMockMvc.perform(get("/api/fees-agencies/{id}", feesAgency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(feesAgency.getId().intValue()))
            .andExpect(jsonPath("$.fees").value(DEFAULT_FEES));
    }

    @Test
    @Transactional
    public void getNonExistingFeesAgency() throws Exception {
        // Get the feesAgency
        restFeesAgencyMockMvc.perform(get("/api/fees-agencies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFeesAgency() throws Exception {
        // Initialize the database
        feesAgencyRepository.saveAndFlush(feesAgency);
        feesAgencySearchRepository.save(feesAgency);
        int databaseSizeBeforeUpdate = feesAgencyRepository.findAll().size();

        // Update the feesAgency
        FeesAgency updatedFeesAgency = feesAgencyRepository.findOne(feesAgency.getId());
        // Disconnect from session so that the updates on updatedFeesAgency are not directly saved in db
        em.detach(updatedFeesAgency);
        updatedFeesAgency
            .fees(UPDATED_FEES);

        restFeesAgencyMockMvc.perform(put("/api/fees-agencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFeesAgency)))
            .andExpect(status().isOk());

        // Validate the FeesAgency in the database
        List<FeesAgency> feesAgencyList = feesAgencyRepository.findAll();
        assertThat(feesAgencyList).hasSize(databaseSizeBeforeUpdate);
        FeesAgency testFeesAgency = feesAgencyList.get(feesAgencyList.size() - 1);
        assertThat(testFeesAgency.getFees()).isEqualTo(UPDATED_FEES);

        // Validate the FeesAgency in Elasticsearch
        FeesAgency feesAgencyEs = feesAgencySearchRepository.findOne(testFeesAgency.getId());
        assertThat(feesAgencyEs).isEqualToIgnoringGivenFields(testFeesAgency);
    }

    @Test
    @Transactional
    public void updateNonExistingFeesAgency() throws Exception {
        int databaseSizeBeforeUpdate = feesAgencyRepository.findAll().size();

        // Create the FeesAgency

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFeesAgencyMockMvc.perform(put("/api/fees-agencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(feesAgency)))
            .andExpect(status().isCreated());

        // Validate the FeesAgency in the database
        List<FeesAgency> feesAgencyList = feesAgencyRepository.findAll();
        assertThat(feesAgencyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFeesAgency() throws Exception {
        // Initialize the database
        feesAgencyRepository.saveAndFlush(feesAgency);
        feesAgencySearchRepository.save(feesAgency);
        int databaseSizeBeforeDelete = feesAgencyRepository.findAll().size();

        // Get the feesAgency
        restFeesAgencyMockMvc.perform(delete("/api/fees-agencies/{id}", feesAgency.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean feesAgencyExistsInEs = feesAgencySearchRepository.exists(feesAgency.getId());
        assertThat(feesAgencyExistsInEs).isFalse();

        // Validate the database is empty
        List<FeesAgency> feesAgencyList = feesAgencyRepository.findAll();
        assertThat(feesAgencyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFeesAgency() throws Exception {
        // Initialize the database
        feesAgencyRepository.saveAndFlush(feesAgency);
        feesAgencySearchRepository.save(feesAgency);

        // Search the feesAgency
        restFeesAgencyMockMvc.perform(get("/api/_search/fees-agencies?query=id:" + feesAgency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feesAgency.getId().intValue())))
            .andExpect(jsonPath("$.[*].fees").value(hasItem(DEFAULT_FEES)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeesAgency.class);
        FeesAgency feesAgency1 = new FeesAgency();
        feesAgency1.setId(1L);
        FeesAgency feesAgency2 = new FeesAgency();
        feesAgency2.setId(feesAgency1.getId());
        assertThat(feesAgency1).isEqualTo(feesAgency2);
        feesAgency2.setId(2L);
        assertThat(feesAgency1).isNotEqualTo(feesAgency2);
        feesAgency1.setId(null);
        assertThat(feesAgency1).isNotEqualTo(feesAgency2);
    }
}
