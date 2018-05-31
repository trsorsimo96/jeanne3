package ma.maman.jeanne.web.rest;

import ma.maman.jeanne.Jeanne3App;

import ma.maman.jeanne.domain.Agency;
import ma.maman.jeanne.repository.AgencyRepository;
import ma.maman.jeanne.repository.search.AgencySearchRepository;
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
 * Test class for the AgencyResource REST controller.
 *
 * @see AgencyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Jeanne3App.class)
public class AgencyResourceIntTest {

    private static final String DEFAULT_PCC = "AAAAAAAAAA";
    private static final String UPDATED_PCC = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TEL = "AAAAAAAAAA";
    private static final String UPDATED_TEL = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_OM_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_OM_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_MOMO_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MOMO_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_SOLDE = 1;
    private static final Integer UPDATED_SOLDE = 2;

    @Autowired
    private AgencyRepository agencyRepository;

    @Autowired
    private AgencySearchRepository agencySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAgencyMockMvc;

    private Agency agency;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AgencyResource agencyResource = new AgencyResource(agencyRepository, agencySearchRepository);
        this.restAgencyMockMvc = MockMvcBuilders.standaloneSetup(agencyResource)
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
    public static Agency createEntity(EntityManager em) {
        Agency agency = new Agency()
            .pcc(DEFAULT_PCC)
            .name(DEFAULT_NAME)
            .tel(DEFAULT_TEL)
            .email(DEFAULT_EMAIL)
            .omNumber(DEFAULT_OM_NUMBER)
            .momoNumber(DEFAULT_MOMO_NUMBER)
            .solde(DEFAULT_SOLDE);
        return agency;
    }

    @Before
    public void initTest() {
        agencySearchRepository.deleteAll();
        agency = createEntity(em);
    }

    @Test
    @Transactional
    public void createAgency() throws Exception {
        int databaseSizeBeforeCreate = agencyRepository.findAll().size();

        // Create the Agency
        restAgencyMockMvc.perform(post("/api/agencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agency)))
            .andExpect(status().isCreated());

        // Validate the Agency in the database
        List<Agency> agencyList = agencyRepository.findAll();
        assertThat(agencyList).hasSize(databaseSizeBeforeCreate + 1);
        Agency testAgency = agencyList.get(agencyList.size() - 1);
        assertThat(testAgency.getPcc()).isEqualTo(DEFAULT_PCC);
        assertThat(testAgency.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAgency.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testAgency.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAgency.getOmNumber()).isEqualTo(DEFAULT_OM_NUMBER);
        assertThat(testAgency.getMomoNumber()).isEqualTo(DEFAULT_MOMO_NUMBER);
        assertThat(testAgency.getSolde()).isEqualTo(DEFAULT_SOLDE);

        // Validate the Agency in Elasticsearch
        Agency agencyEs = agencySearchRepository.findOne(testAgency.getId());
        assertThat(agencyEs).isEqualToIgnoringGivenFields(testAgency);
    }

    @Test
    @Transactional
    public void createAgencyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = agencyRepository.findAll().size();

        // Create the Agency with an existing ID
        agency.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgencyMockMvc.perform(post("/api/agencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agency)))
            .andExpect(status().isBadRequest());

        // Validate the Agency in the database
        List<Agency> agencyList = agencyRepository.findAll();
        assertThat(agencyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPccIsRequired() throws Exception {
        int databaseSizeBeforeTest = agencyRepository.findAll().size();
        // set the field null
        agency.setPcc(null);

        // Create the Agency, which fails.

        restAgencyMockMvc.perform(post("/api/agencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agency)))
            .andExpect(status().isBadRequest());

        List<Agency> agencyList = agencyRepository.findAll();
        assertThat(agencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = agencyRepository.findAll().size();
        // set the field null
        agency.setName(null);

        // Create the Agency, which fails.

        restAgencyMockMvc.perform(post("/api/agencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agency)))
            .andExpect(status().isBadRequest());

        List<Agency> agencyList = agencyRepository.findAll();
        assertThat(agencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = agencyRepository.findAll().size();
        // set the field null
        agency.setEmail(null);

        // Create the Agency, which fails.

        restAgencyMockMvc.perform(post("/api/agencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agency)))
            .andExpect(status().isBadRequest());

        List<Agency> agencyList = agencyRepository.findAll();
        assertThat(agencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAgencies() throws Exception {
        // Initialize the database
        agencyRepository.saveAndFlush(agency);

        // Get all the agencyList
        restAgencyMockMvc.perform(get("/api/agencies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agency.getId().intValue())))
            .andExpect(jsonPath("$.[*].pcc").value(hasItem(DEFAULT_PCC.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].omNumber").value(hasItem(DEFAULT_OM_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].momoNumber").value(hasItem(DEFAULT_MOMO_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].solde").value(hasItem(DEFAULT_SOLDE)));
    }

    @Test
    @Transactional
    public void getAgency() throws Exception {
        // Initialize the database
        agencyRepository.saveAndFlush(agency);

        // Get the agency
        restAgencyMockMvc.perform(get("/api/agencies/{id}", agency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(agency.getId().intValue()))
            .andExpect(jsonPath("$.pcc").value(DEFAULT_PCC.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.omNumber").value(DEFAULT_OM_NUMBER.toString()))
            .andExpect(jsonPath("$.momoNumber").value(DEFAULT_MOMO_NUMBER.toString()))
            .andExpect(jsonPath("$.solde").value(DEFAULT_SOLDE));
    }

    @Test
    @Transactional
    public void getNonExistingAgency() throws Exception {
        // Get the agency
        restAgencyMockMvc.perform(get("/api/agencies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAgency() throws Exception {
        // Initialize the database
        agencyRepository.saveAndFlush(agency);
        agencySearchRepository.save(agency);
        int databaseSizeBeforeUpdate = agencyRepository.findAll().size();

        // Update the agency
        Agency updatedAgency = agencyRepository.findOne(agency.getId());
        // Disconnect from session so that the updates on updatedAgency are not directly saved in db
        em.detach(updatedAgency);
        updatedAgency
            .pcc(UPDATED_PCC)
            .name(UPDATED_NAME)
            .tel(UPDATED_TEL)
            .email(UPDATED_EMAIL)
            .omNumber(UPDATED_OM_NUMBER)
            .momoNumber(UPDATED_MOMO_NUMBER)
            .solde(UPDATED_SOLDE);

        restAgencyMockMvc.perform(put("/api/agencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAgency)))
            .andExpect(status().isOk());

        // Validate the Agency in the database
        List<Agency> agencyList = agencyRepository.findAll();
        assertThat(agencyList).hasSize(databaseSizeBeforeUpdate);
        Agency testAgency = agencyList.get(agencyList.size() - 1);
        assertThat(testAgency.getPcc()).isEqualTo(UPDATED_PCC);
        assertThat(testAgency.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAgency.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testAgency.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAgency.getOmNumber()).isEqualTo(UPDATED_OM_NUMBER);
        assertThat(testAgency.getMomoNumber()).isEqualTo(UPDATED_MOMO_NUMBER);
        assertThat(testAgency.getSolde()).isEqualTo(UPDATED_SOLDE);

        // Validate the Agency in Elasticsearch
        Agency agencyEs = agencySearchRepository.findOne(testAgency.getId());
        assertThat(agencyEs).isEqualToIgnoringGivenFields(testAgency);
    }

    @Test
    @Transactional
    public void updateNonExistingAgency() throws Exception {
        int databaseSizeBeforeUpdate = agencyRepository.findAll().size();

        // Create the Agency

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAgencyMockMvc.perform(put("/api/agencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agency)))
            .andExpect(status().isCreated());

        // Validate the Agency in the database
        List<Agency> agencyList = agencyRepository.findAll();
        assertThat(agencyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAgency() throws Exception {
        // Initialize the database
        agencyRepository.saveAndFlush(agency);
        agencySearchRepository.save(agency);
        int databaseSizeBeforeDelete = agencyRepository.findAll().size();

        // Get the agency
        restAgencyMockMvc.perform(delete("/api/agencies/{id}", agency.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean agencyExistsInEs = agencySearchRepository.exists(agency.getId());
        assertThat(agencyExistsInEs).isFalse();

        // Validate the database is empty
        List<Agency> agencyList = agencyRepository.findAll();
        assertThat(agencyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAgency() throws Exception {
        // Initialize the database
        agencyRepository.saveAndFlush(agency);
        agencySearchRepository.save(agency);

        // Search the agency
        restAgencyMockMvc.perform(get("/api/_search/agencies?query=id:" + agency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agency.getId().intValue())))
            .andExpect(jsonPath("$.[*].pcc").value(hasItem(DEFAULT_PCC.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].omNumber").value(hasItem(DEFAULT_OM_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].momoNumber").value(hasItem(DEFAULT_MOMO_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].solde").value(hasItem(DEFAULT_SOLDE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Agency.class);
        Agency agency1 = new Agency();
        agency1.setId(1L);
        Agency agency2 = new Agency();
        agency2.setId(agency1.getId());
        assertThat(agency1).isEqualTo(agency2);
        agency2.setId(2L);
        assertThat(agency1).isNotEqualTo(agency2);
        agency1.setId(null);
        assertThat(agency1).isNotEqualTo(agency2);
    }
}
