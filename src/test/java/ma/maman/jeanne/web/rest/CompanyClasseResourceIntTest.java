package ma.maman.jeanne.web.rest;

import ma.maman.jeanne.Jeanne3App;

import ma.maman.jeanne.domain.CompanyClasse;
import ma.maman.jeanne.repository.CompanyClasseRepository;
import ma.maman.jeanne.repository.search.CompanyClasseSearchRepository;
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
 * Test class for the CompanyClasseResource REST controller.
 *
 * @see CompanyClasseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Jeanne3App.class)
public class CompanyClasseResourceIntTest {

    private static final Integer DEFAULT_PRICE = 1;
    private static final Integer UPDATED_PRICE = 2;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private CompanyClasseRepository companyClasseRepository;

    @Autowired
    private CompanyClasseSearchRepository companyClasseSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCompanyClasseMockMvc;

    private CompanyClasse companyClasse;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CompanyClasseResource companyClasseResource = new CompanyClasseResource(companyClasseRepository, companyClasseSearchRepository);
        this.restCompanyClasseMockMvc = MockMvcBuilders.standaloneSetup(companyClasseResource)
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
    public static CompanyClasse createEntity(EntityManager em) {
        CompanyClasse companyClasse = new CompanyClasse()
            .price(DEFAULT_PRICE)
            .name(DEFAULT_NAME);
        return companyClasse;
    }

    @Before
    public void initTest() {
        companyClasseSearchRepository.deleteAll();
        companyClasse = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompanyClasse() throws Exception {
        int databaseSizeBeforeCreate = companyClasseRepository.findAll().size();

        // Create the CompanyClasse
        restCompanyClasseMockMvc.perform(post("/api/company-classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyClasse)))
            .andExpect(status().isCreated());

        // Validate the CompanyClasse in the database
        List<CompanyClasse> companyClasseList = companyClasseRepository.findAll();
        assertThat(companyClasseList).hasSize(databaseSizeBeforeCreate + 1);
        CompanyClasse testCompanyClasse = companyClasseList.get(companyClasseList.size() - 1);
        assertThat(testCompanyClasse.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testCompanyClasse.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the CompanyClasse in Elasticsearch
        CompanyClasse companyClasseEs = companyClasseSearchRepository.findOne(testCompanyClasse.getId());
        assertThat(companyClasseEs).isEqualToIgnoringGivenFields(testCompanyClasse);
    }

    @Test
    @Transactional
    public void createCompanyClasseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = companyClasseRepository.findAll().size();

        // Create the CompanyClasse with an existing ID
        companyClasse.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyClasseMockMvc.perform(post("/api/company-classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyClasse)))
            .andExpect(status().isBadRequest());

        // Validate the CompanyClasse in the database
        List<CompanyClasse> companyClasseList = companyClasseRepository.findAll();
        assertThat(companyClasseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCompanyClasses() throws Exception {
        // Initialize the database
        companyClasseRepository.saveAndFlush(companyClasse);

        // Get all the companyClasseList
        restCompanyClasseMockMvc.perform(get("/api/company-classes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyClasse.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getCompanyClasse() throws Exception {
        // Initialize the database
        companyClasseRepository.saveAndFlush(companyClasse);

        // Get the companyClasse
        restCompanyClasseMockMvc.perform(get("/api/company-classes/{id}", companyClasse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(companyClasse.getId().intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCompanyClasse() throws Exception {
        // Get the companyClasse
        restCompanyClasseMockMvc.perform(get("/api/company-classes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompanyClasse() throws Exception {
        // Initialize the database
        companyClasseRepository.saveAndFlush(companyClasse);
        companyClasseSearchRepository.save(companyClasse);
        int databaseSizeBeforeUpdate = companyClasseRepository.findAll().size();

        // Update the companyClasse
        CompanyClasse updatedCompanyClasse = companyClasseRepository.findOne(companyClasse.getId());
        // Disconnect from session so that the updates on updatedCompanyClasse are not directly saved in db
        em.detach(updatedCompanyClasse);
        updatedCompanyClasse
            .price(UPDATED_PRICE)
            .name(UPDATED_NAME);

        restCompanyClasseMockMvc.perform(put("/api/company-classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCompanyClasse)))
            .andExpect(status().isOk());

        // Validate the CompanyClasse in the database
        List<CompanyClasse> companyClasseList = companyClasseRepository.findAll();
        assertThat(companyClasseList).hasSize(databaseSizeBeforeUpdate);
        CompanyClasse testCompanyClasse = companyClasseList.get(companyClasseList.size() - 1);
        assertThat(testCompanyClasse.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCompanyClasse.getName()).isEqualTo(UPDATED_NAME);

        // Validate the CompanyClasse in Elasticsearch
        CompanyClasse companyClasseEs = companyClasseSearchRepository.findOne(testCompanyClasse.getId());
        assertThat(companyClasseEs).isEqualToIgnoringGivenFields(testCompanyClasse);
    }

    @Test
    @Transactional
    public void updateNonExistingCompanyClasse() throws Exception {
        int databaseSizeBeforeUpdate = companyClasseRepository.findAll().size();

        // Create the CompanyClasse

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCompanyClasseMockMvc.perform(put("/api/company-classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyClasse)))
            .andExpect(status().isCreated());

        // Validate the CompanyClasse in the database
        List<CompanyClasse> companyClasseList = companyClasseRepository.findAll();
        assertThat(companyClasseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCompanyClasse() throws Exception {
        // Initialize the database
        companyClasseRepository.saveAndFlush(companyClasse);
        companyClasseSearchRepository.save(companyClasse);
        int databaseSizeBeforeDelete = companyClasseRepository.findAll().size();

        // Get the companyClasse
        restCompanyClasseMockMvc.perform(delete("/api/company-classes/{id}", companyClasse.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean companyClasseExistsInEs = companyClasseSearchRepository.exists(companyClasse.getId());
        assertThat(companyClasseExistsInEs).isFalse();

        // Validate the database is empty
        List<CompanyClasse> companyClasseList = companyClasseRepository.findAll();
        assertThat(companyClasseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCompanyClasse() throws Exception {
        // Initialize the database
        companyClasseRepository.saveAndFlush(companyClasse);
        companyClasseSearchRepository.save(companyClasse);

        // Search the companyClasse
        restCompanyClasseMockMvc.perform(get("/api/_search/company-classes?query=id:" + companyClasse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyClasse.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyClasse.class);
        CompanyClasse companyClasse1 = new CompanyClasse();
        companyClasse1.setId(1L);
        CompanyClasse companyClasse2 = new CompanyClasse();
        companyClasse2.setId(companyClasse1.getId());
        assertThat(companyClasse1).isEqualTo(companyClasse2);
        companyClasse2.setId(2L);
        assertThat(companyClasse1).isNotEqualTo(companyClasse2);
        companyClasse1.setId(null);
        assertThat(companyClasse1).isNotEqualTo(companyClasse2);
    }
}
