package ma.maman.jeanne.web.rest;

import ma.maman.jeanne.Jeanne3App;

import ma.maman.jeanne.domain.Day;
import ma.maman.jeanne.repository.DayRepository;
import ma.maman.jeanne.repository.search.DaySearchRepository;
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
 * Test class for the DayResource REST controller.
 *
 * @see DayResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Jeanne3App.class)
public class DayResourceIntTest {

    private static final String DEFAULT_CODE = "AAA";
    private static final String UPDATED_CODE = "BBB";

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    @Autowired
    private DayRepository dayRepository;

    @Autowired
    private DaySearchRepository daySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDayMockMvc;

    private Day day;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DayResource dayResource = new DayResource(dayRepository, daySearchRepository);
        this.restDayMockMvc = MockMvcBuilders.standaloneSetup(dayResource)
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
    public static Day createEntity(EntityManager em) {
        Day day = new Day()
            .code(DEFAULT_CODE)
            .text(DEFAULT_TEXT)
            .number(DEFAULT_NUMBER);
        return day;
    }

    @Before
    public void initTest() {
        daySearchRepository.deleteAll();
        day = createEntity(em);
    }

    @Test
    @Transactional
    public void createDay() throws Exception {
        int databaseSizeBeforeCreate = dayRepository.findAll().size();

        // Create the Day
        restDayMockMvc.perform(post("/api/days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(day)))
            .andExpect(status().isCreated());

        // Validate the Day in the database
        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeCreate + 1);
        Day testDay = dayList.get(dayList.size() - 1);
        assertThat(testDay.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDay.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testDay.getNumber()).isEqualTo(DEFAULT_NUMBER);

        // Validate the Day in Elasticsearch
        Day dayEs = daySearchRepository.findOne(testDay.getId());
        assertThat(dayEs).isEqualToIgnoringGivenFields(testDay);
    }

    @Test
    @Transactional
    public void createDayWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dayRepository.findAll().size();

        // Create the Day with an existing ID
        day.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDayMockMvc.perform(post("/api/days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(day)))
            .andExpect(status().isBadRequest());

        // Validate the Day in the database
        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = dayRepository.findAll().size();
        // set the field null
        day.setCode(null);

        // Create the Day, which fails.

        restDayMockMvc.perform(post("/api/days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(day)))
            .andExpect(status().isBadRequest());

        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = dayRepository.findAll().size();
        // set the field null
        day.setText(null);

        // Create the Day, which fails.

        restDayMockMvc.perform(post("/api/days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(day)))
            .andExpect(status().isBadRequest());

        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = dayRepository.findAll().size();
        // set the field null
        day.setNumber(null);

        // Create the Day, which fails.

        restDayMockMvc.perform(post("/api/days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(day)))
            .andExpect(status().isBadRequest());

        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDays() throws Exception {
        // Initialize the database
        dayRepository.saveAndFlush(day);

        // Get all the dayList
        restDayMockMvc.perform(get("/api/days?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(day.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)));
    }

    @Test
    @Transactional
    public void getDay() throws Exception {
        // Initialize the database
        dayRepository.saveAndFlush(day);

        // Get the day
        restDayMockMvc.perform(get("/api/days/{id}", day.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(day.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER));
    }

    @Test
    @Transactional
    public void getNonExistingDay() throws Exception {
        // Get the day
        restDayMockMvc.perform(get("/api/days/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDay() throws Exception {
        // Initialize the database
        dayRepository.saveAndFlush(day);
        daySearchRepository.save(day);
        int databaseSizeBeforeUpdate = dayRepository.findAll().size();

        // Update the day
        Day updatedDay = dayRepository.findOne(day.getId());
        // Disconnect from session so that the updates on updatedDay are not directly saved in db
        em.detach(updatedDay);
        updatedDay
            .code(UPDATED_CODE)
            .text(UPDATED_TEXT)
            .number(UPDATED_NUMBER);

        restDayMockMvc.perform(put("/api/days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDay)))
            .andExpect(status().isOk());

        // Validate the Day in the database
        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeUpdate);
        Day testDay = dayList.get(dayList.size() - 1);
        assertThat(testDay.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDay.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testDay.getNumber()).isEqualTo(UPDATED_NUMBER);

        // Validate the Day in Elasticsearch
        Day dayEs = daySearchRepository.findOne(testDay.getId());
        assertThat(dayEs).isEqualToIgnoringGivenFields(testDay);
    }

    @Test
    @Transactional
    public void updateNonExistingDay() throws Exception {
        int databaseSizeBeforeUpdate = dayRepository.findAll().size();

        // Create the Day

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDayMockMvc.perform(put("/api/days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(day)))
            .andExpect(status().isCreated());

        // Validate the Day in the database
        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDay() throws Exception {
        // Initialize the database
        dayRepository.saveAndFlush(day);
        daySearchRepository.save(day);
        int databaseSizeBeforeDelete = dayRepository.findAll().size();

        // Get the day
        restDayMockMvc.perform(delete("/api/days/{id}", day.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean dayExistsInEs = daySearchRepository.exists(day.getId());
        assertThat(dayExistsInEs).isFalse();

        // Validate the database is empty
        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDay() throws Exception {
        // Initialize the database
        dayRepository.saveAndFlush(day);
        daySearchRepository.save(day);

        // Search the day
        restDayMockMvc.perform(get("/api/_search/days?query=id:" + day.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(day.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Day.class);
        Day day1 = new Day();
        day1.setId(1L);
        Day day2 = new Day();
        day2.setId(day1.getId());
        assertThat(day1).isEqualTo(day2);
        day2.setId(2L);
        assertThat(day1).isNotEqualTo(day2);
        day1.setId(null);
        assertThat(day1).isNotEqualTo(day2);
    }
}
