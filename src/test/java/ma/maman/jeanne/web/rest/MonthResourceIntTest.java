package ma.maman.jeanne.web.rest;

import ma.maman.jeanne.Jeanne3App;

import ma.maman.jeanne.domain.Month;
import ma.maman.jeanne.repository.MonthRepository;
import ma.maman.jeanne.repository.search.MonthSearchRepository;
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
 * Test class for the MonthResource REST controller.
 *
 * @see MonthResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Jeanne3App.class)
public class MonthResourceIntTest {

    private static final String DEFAULT_CODE = "AAA";
    private static final String UPDATED_CODE = "BBB";

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    @Autowired
    private MonthRepository monthRepository;

    @Autowired
    private MonthSearchRepository monthSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMonthMockMvc;

    private Month month;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MonthResource monthResource = new MonthResource(monthRepository, monthSearchRepository);
        this.restMonthMockMvc = MockMvcBuilders.standaloneSetup(monthResource)
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
    public static Month createEntity(EntityManager em) {
        Month month = new Month()
            .code(DEFAULT_CODE)
            .text(DEFAULT_TEXT)
            .number(DEFAULT_NUMBER);
        return month;
    }

    @Before
    public void initTest() {
        monthSearchRepository.deleteAll();
        month = createEntity(em);
    }

    @Test
    @Transactional
    public void createMonth() throws Exception {
        int databaseSizeBeforeCreate = monthRepository.findAll().size();

        // Create the Month
        restMonthMockMvc.perform(post("/api/months")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(month)))
            .andExpect(status().isCreated());

        // Validate the Month in the database
        List<Month> monthList = monthRepository.findAll();
        assertThat(monthList).hasSize(databaseSizeBeforeCreate + 1);
        Month testMonth = monthList.get(monthList.size() - 1);
        assertThat(testMonth.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testMonth.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testMonth.getNumber()).isEqualTo(DEFAULT_NUMBER);

        // Validate the Month in Elasticsearch
        Month monthEs = monthSearchRepository.findOne(testMonth.getId());
        assertThat(monthEs).isEqualToIgnoringGivenFields(testMonth);
    }

    @Test
    @Transactional
    public void createMonthWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = monthRepository.findAll().size();

        // Create the Month with an existing ID
        month.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMonthMockMvc.perform(post("/api/months")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(month)))
            .andExpect(status().isBadRequest());

        // Validate the Month in the database
        List<Month> monthList = monthRepository.findAll();
        assertThat(monthList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = monthRepository.findAll().size();
        // set the field null
        month.setCode(null);

        // Create the Month, which fails.

        restMonthMockMvc.perform(post("/api/months")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(month)))
            .andExpect(status().isBadRequest());

        List<Month> monthList = monthRepository.findAll();
        assertThat(monthList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = monthRepository.findAll().size();
        // set the field null
        month.setText(null);

        // Create the Month, which fails.

        restMonthMockMvc.perform(post("/api/months")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(month)))
            .andExpect(status().isBadRequest());

        List<Month> monthList = monthRepository.findAll();
        assertThat(monthList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMonths() throws Exception {
        // Initialize the database
        monthRepository.saveAndFlush(month);

        // Get all the monthList
        restMonthMockMvc.perform(get("/api/months?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(month.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)));
    }

    @Test
    @Transactional
    public void getMonth() throws Exception {
        // Initialize the database
        monthRepository.saveAndFlush(month);

        // Get the month
        restMonthMockMvc.perform(get("/api/months/{id}", month.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(month.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER));
    }

    @Test
    @Transactional
    public void getNonExistingMonth() throws Exception {
        // Get the month
        restMonthMockMvc.perform(get("/api/months/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMonth() throws Exception {
        // Initialize the database
        monthRepository.saveAndFlush(month);
        monthSearchRepository.save(month);
        int databaseSizeBeforeUpdate = monthRepository.findAll().size();

        // Update the month
        Month updatedMonth = monthRepository.findOne(month.getId());
        // Disconnect from session so that the updates on updatedMonth are not directly saved in db
        em.detach(updatedMonth);
        updatedMonth
            .code(UPDATED_CODE)
            .text(UPDATED_TEXT)
            .number(UPDATED_NUMBER);

        restMonthMockMvc.perform(put("/api/months")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMonth)))
            .andExpect(status().isOk());

        // Validate the Month in the database
        List<Month> monthList = monthRepository.findAll();
        assertThat(monthList).hasSize(databaseSizeBeforeUpdate);
        Month testMonth = monthList.get(monthList.size() - 1);
        assertThat(testMonth.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMonth.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testMonth.getNumber()).isEqualTo(UPDATED_NUMBER);

        // Validate the Month in Elasticsearch
        Month monthEs = monthSearchRepository.findOne(testMonth.getId());
        assertThat(monthEs).isEqualToIgnoringGivenFields(testMonth);
    }

    @Test
    @Transactional
    public void updateNonExistingMonth() throws Exception {
        int databaseSizeBeforeUpdate = monthRepository.findAll().size();

        // Create the Month

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMonthMockMvc.perform(put("/api/months")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(month)))
            .andExpect(status().isCreated());

        // Validate the Month in the database
        List<Month> monthList = monthRepository.findAll();
        assertThat(monthList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMonth() throws Exception {
        // Initialize the database
        monthRepository.saveAndFlush(month);
        monthSearchRepository.save(month);
        int databaseSizeBeforeDelete = monthRepository.findAll().size();

        // Get the month
        restMonthMockMvc.perform(delete("/api/months/{id}", month.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean monthExistsInEs = monthSearchRepository.exists(month.getId());
        assertThat(monthExistsInEs).isFalse();

        // Validate the database is empty
        List<Month> monthList = monthRepository.findAll();
        assertThat(monthList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMonth() throws Exception {
        // Initialize the database
        monthRepository.saveAndFlush(month);
        monthSearchRepository.save(month);

        // Search the month
        restMonthMockMvc.perform(get("/api/_search/months?query=id:" + month.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(month.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Month.class);
        Month month1 = new Month();
        month1.setId(1L);
        Month month2 = new Month();
        month2.setId(month1.getId());
        assertThat(month1).isEqualTo(month2);
        month2.setId(2L);
        assertThat(month1).isNotEqualTo(month2);
        month1.setId(null);
        assertThat(month1).isNotEqualTo(month2);
    }
}
