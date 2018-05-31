package ma.maman.jeanne.web.rest;

import ma.maman.jeanne.Jeanne3App;

import ma.maman.jeanne.domain.Deposit;
import ma.maman.jeanne.domain.Agency;
import ma.maman.jeanne.repository.DepositRepository;
import ma.maman.jeanne.repository.search.DepositSearchRepository;
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

import ma.maman.jeanne.domain.enumeration.ModePayment;
/**
 * Test class for the DepositResource REST controller.
 *
 * @see DepositResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Jeanne3App.class)
public class DepositResourceIntTest {

    private static final Integer DEFAULT_AMOUNT = 1;
    private static final Integer UPDATED_AMOUNT = 2;

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ModePayment DEFAULT_MODE_PAYMENT = ModePayment.CASH;
    private static final ModePayment UPDATED_MODE_PAYMENT = ModePayment.INVOICE;

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private DepositSearchRepository depositSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDepositMockMvc;

    private Deposit deposit;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DepositResource depositResource = new DepositResource(depositRepository, depositSearchRepository);
        this.restDepositMockMvc = MockMvcBuilders.standaloneSetup(depositResource)
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
    public static Deposit createEntity(EntityManager em) {
        Deposit deposit = new Deposit()
            .amount(DEFAULT_AMOUNT)
            .date(DEFAULT_DATE)
            .modePayment(DEFAULT_MODE_PAYMENT);
        // Add required entity
        Agency agency = AgencyResourceIntTest.createEntity(em);
        em.persist(agency);
        em.flush();
        deposit.setAgency(agency);
        return deposit;
    }

    @Before
    public void initTest() {
        depositSearchRepository.deleteAll();
        deposit = createEntity(em);
    }

    @Test
    @Transactional
    public void createDeposit() throws Exception {
        int databaseSizeBeforeCreate = depositRepository.findAll().size();

        // Create the Deposit
        restDepositMockMvc.perform(post("/api/deposits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deposit)))
            .andExpect(status().isCreated());

        // Validate the Deposit in the database
        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeCreate + 1);
        Deposit testDeposit = depositList.get(depositList.size() - 1);
        assertThat(testDeposit.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testDeposit.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDeposit.getModePayment()).isEqualTo(DEFAULT_MODE_PAYMENT);

        // Validate the Deposit in Elasticsearch
        Deposit depositEs = depositSearchRepository.findOne(testDeposit.getId());
        assertThat(testDeposit.getDate()).isEqualTo(testDeposit.getDate());
        assertThat(depositEs).isEqualToIgnoringGivenFields(testDeposit, "date");
    }

    @Test
    @Transactional
    public void createDepositWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = depositRepository.findAll().size();

        // Create the Deposit with an existing ID
        deposit.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepositMockMvc.perform(post("/api/deposits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deposit)))
            .andExpect(status().isBadRequest());

        // Validate the Deposit in the database
        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = depositRepository.findAll().size();
        // set the field null
        deposit.setAmount(null);

        // Create the Deposit, which fails.

        restDepositMockMvc.perform(post("/api/deposits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deposit)))
            .andExpect(status().isBadRequest());

        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = depositRepository.findAll().size();
        // set the field null
        deposit.setDate(null);

        // Create the Deposit, which fails.

        restDepositMockMvc.perform(post("/api/deposits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deposit)))
            .andExpect(status().isBadRequest());

        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModePaymentIsRequired() throws Exception {
        int databaseSizeBeforeTest = depositRepository.findAll().size();
        // set the field null
        deposit.setModePayment(null);

        // Create the Deposit, which fails.

        restDepositMockMvc.perform(post("/api/deposits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deposit)))
            .andExpect(status().isBadRequest());

        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDeposits() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList
        restDepositMockMvc.perform(get("/api/deposits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deposit.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].modePayment").value(hasItem(DEFAULT_MODE_PAYMENT.toString())));
    }

    @Test
    @Transactional
    public void getDeposit() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get the deposit
        restDepositMockMvc.perform(get("/api/deposits/{id}", deposit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(deposit.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.modePayment").value(DEFAULT_MODE_PAYMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDeposit() throws Exception {
        // Get the deposit
        restDepositMockMvc.perform(get("/api/deposits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeposit() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);
        depositSearchRepository.save(deposit);
        int databaseSizeBeforeUpdate = depositRepository.findAll().size();

        // Update the deposit
        Deposit updatedDeposit = depositRepository.findOne(deposit.getId());
        // Disconnect from session so that the updates on updatedDeposit are not directly saved in db
        em.detach(updatedDeposit);
        updatedDeposit
            .amount(UPDATED_AMOUNT)
            .date(UPDATED_DATE)
            .modePayment(UPDATED_MODE_PAYMENT);

        restDepositMockMvc.perform(put("/api/deposits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDeposit)))
            .andExpect(status().isOk());

        // Validate the Deposit in the database
        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeUpdate);
        Deposit testDeposit = depositList.get(depositList.size() - 1);
        assertThat(testDeposit.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testDeposit.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDeposit.getModePayment()).isEqualTo(UPDATED_MODE_PAYMENT);

        // Validate the Deposit in Elasticsearch
        Deposit depositEs = depositSearchRepository.findOne(testDeposit.getId());
        assertThat(testDeposit.getDate()).isEqualTo(testDeposit.getDate());
        assertThat(depositEs).isEqualToIgnoringGivenFields(testDeposit, "date");
    }

    @Test
    @Transactional
    public void updateNonExistingDeposit() throws Exception {
        int databaseSizeBeforeUpdate = depositRepository.findAll().size();

        // Create the Deposit

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDepositMockMvc.perform(put("/api/deposits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deposit)))
            .andExpect(status().isCreated());

        // Validate the Deposit in the database
        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDeposit() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);
        depositSearchRepository.save(deposit);
        int databaseSizeBeforeDelete = depositRepository.findAll().size();

        // Get the deposit
        restDepositMockMvc.perform(delete("/api/deposits/{id}", deposit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean depositExistsInEs = depositSearchRepository.exists(deposit.getId());
        assertThat(depositExistsInEs).isFalse();

        // Validate the database is empty
        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDeposit() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);
        depositSearchRepository.save(deposit);

        // Search the deposit
        restDepositMockMvc.perform(get("/api/_search/deposits?query=id:" + deposit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deposit.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].modePayment").value(hasItem(DEFAULT_MODE_PAYMENT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Deposit.class);
        Deposit deposit1 = new Deposit();
        deposit1.setId(1L);
        Deposit deposit2 = new Deposit();
        deposit2.setId(deposit1.getId());
        assertThat(deposit1).isEqualTo(deposit2);
        deposit2.setId(2L);
        assertThat(deposit1).isNotEqualTo(deposit2);
        deposit1.setId(null);
        assertThat(deposit1).isNotEqualTo(deposit2);
    }
}
