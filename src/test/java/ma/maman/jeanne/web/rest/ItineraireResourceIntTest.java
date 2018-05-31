package ma.maman.jeanne.web.rest;

import ma.maman.jeanne.Jeanne3App;

import ma.maman.jeanne.domain.Itineraire;
import ma.maman.jeanne.domain.Routes;
import ma.maman.jeanne.domain.Classe;
import ma.maman.jeanne.domain.Company;
import ma.maman.jeanne.domain.Day;
import ma.maman.jeanne.repository.ItineraireRepository;
import ma.maman.jeanne.repository.search.ItineraireSearchRepository;
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

/**
 * Test class for the ItineraireResource REST controller.
 *
 * @see ItineraireResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Jeanne3App.class)
public class ItineraireResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE_DEPART = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_DEPART = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_ARRIVE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_ARRIVE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ItineraireRepository itineraireRepository;

    @Autowired
    private ItineraireSearchRepository itineraireSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restItineraireMockMvc;

    private Itineraire itineraire;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ItineraireResource itineraireResource = new ItineraireResource(itineraireRepository, itineraireSearchRepository);
        this.restItineraireMockMvc = MockMvcBuilders.standaloneSetup(itineraireResource)
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
    public static Itineraire createEntity(EntityManager em) {
        Itineraire itineraire = new Itineraire()
            .dateDepart(DEFAULT_DATE_DEPART)
            .dateArrive(DEFAULT_DATE_ARRIVE);
        // Add required entity
        Routes route = RoutesResourceIntTest.createEntity(em);
        em.persist(route);
        em.flush();
        itineraire.setRoute(route);
        // Add required entity
        Classe classe = ClasseResourceIntTest.createEntity(em);
        em.persist(classe);
        em.flush();
        itineraire.setClasse(classe);
        // Add required entity
        Company company = CompanyResourceIntTest.createEntity(em);
        em.persist(company);
        em.flush();
        itineraire.setCompany(company);
        // Add required entity
        Day day = DayResourceIntTest.createEntity(em);
        em.persist(day);
        em.flush();
        itineraire.setDay(day);
        return itineraire;
    }

    @Before
    public void initTest() {
        itineraireSearchRepository.deleteAll();
        itineraire = createEntity(em);
    }

    @Test
    @Transactional
    public void createItineraire() throws Exception {
        int databaseSizeBeforeCreate = itineraireRepository.findAll().size();

        // Create the Itineraire
        restItineraireMockMvc.perform(post("/api/itineraires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itineraire)))
            .andExpect(status().isCreated());

        // Validate the Itineraire in the database
        List<Itineraire> itineraireList = itineraireRepository.findAll();
        assertThat(itineraireList).hasSize(databaseSizeBeforeCreate + 1);
        Itineraire testItineraire = itineraireList.get(itineraireList.size() - 1);
        assertThat(testItineraire.getDateDepart()).isEqualTo(DEFAULT_DATE_DEPART);
        assertThat(testItineraire.getDateArrive()).isEqualTo(DEFAULT_DATE_ARRIVE);

        // Validate the Itineraire in Elasticsearch
        Itineraire itineraireEs = itineraireSearchRepository.findOne(testItineraire.getId());
        assertThat(testItineraire.getDateDepart()).isEqualTo(testItineraire.getDateDepart());
        assertThat(testItineraire.getDateArrive()).isEqualTo(testItineraire.getDateArrive());
        assertThat(itineraireEs).isEqualToIgnoringGivenFields(testItineraire, "dateDepart", "dateArrive");
    }

    @Test
    @Transactional
    public void createItineraireWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = itineraireRepository.findAll().size();

        // Create the Itineraire with an existing ID
        itineraire.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restItineraireMockMvc.perform(post("/api/itineraires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itineraire)))
            .andExpect(status().isBadRequest());

        // Validate the Itineraire in the database
        List<Itineraire> itineraireList = itineraireRepository.findAll();
        assertThat(itineraireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateDepartIsRequired() throws Exception {
        int databaseSizeBeforeTest = itineraireRepository.findAll().size();
        // set the field null
        itineraire.setDateDepart(null);

        // Create the Itineraire, which fails.

        restItineraireMockMvc.perform(post("/api/itineraires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itineraire)))
            .andExpect(status().isBadRequest());

        List<Itineraire> itineraireList = itineraireRepository.findAll();
        assertThat(itineraireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateArriveIsRequired() throws Exception {
        int databaseSizeBeforeTest = itineraireRepository.findAll().size();
        // set the field null
        itineraire.setDateArrive(null);

        // Create the Itineraire, which fails.

        restItineraireMockMvc.perform(post("/api/itineraires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itineraire)))
            .andExpect(status().isBadRequest());

        List<Itineraire> itineraireList = itineraireRepository.findAll();
        assertThat(itineraireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItineraires() throws Exception {
        // Initialize the database
        itineraireRepository.saveAndFlush(itineraire);

        // Get all the itineraireList
        restItineraireMockMvc.perform(get("/api/itineraires?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itineraire.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDepart").value(hasItem(sameInstant(DEFAULT_DATE_DEPART))))
            .andExpect(jsonPath("$.[*].dateArrive").value(hasItem(sameInstant(DEFAULT_DATE_ARRIVE))));
    }

    @Test
    @Transactional
    public void getItineraire() throws Exception {
        // Initialize the database
        itineraireRepository.saveAndFlush(itineraire);

        // Get the itineraire
        restItineraireMockMvc.perform(get("/api/itineraires/{id}", itineraire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(itineraire.getId().intValue()))
            .andExpect(jsonPath("$.dateDepart").value(sameInstant(DEFAULT_DATE_DEPART)))
            .andExpect(jsonPath("$.dateArrive").value(sameInstant(DEFAULT_DATE_ARRIVE)));
    }

    @Test
    @Transactional
    public void getNonExistingItineraire() throws Exception {
        // Get the itineraire
        restItineraireMockMvc.perform(get("/api/itineraires/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItineraire() throws Exception {
        // Initialize the database
        itineraireRepository.saveAndFlush(itineraire);
        itineraireSearchRepository.save(itineraire);
        int databaseSizeBeforeUpdate = itineraireRepository.findAll().size();

        // Update the itineraire
        Itineraire updatedItineraire = itineraireRepository.findOne(itineraire.getId());
        // Disconnect from session so that the updates on updatedItineraire are not directly saved in db
        em.detach(updatedItineraire);
        updatedItineraire
            .dateDepart(UPDATED_DATE_DEPART)
            .dateArrive(UPDATED_DATE_ARRIVE);

        restItineraireMockMvc.perform(put("/api/itineraires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedItineraire)))
            .andExpect(status().isOk());

        // Validate the Itineraire in the database
        List<Itineraire> itineraireList = itineraireRepository.findAll();
        assertThat(itineraireList).hasSize(databaseSizeBeforeUpdate);
        Itineraire testItineraire = itineraireList.get(itineraireList.size() - 1);
        assertThat(testItineraire.getDateDepart()).isEqualTo(UPDATED_DATE_DEPART);
        assertThat(testItineraire.getDateArrive()).isEqualTo(UPDATED_DATE_ARRIVE);

        // Validate the Itineraire in Elasticsearch
        Itineraire itineraireEs = itineraireSearchRepository.findOne(testItineraire.getId());
        assertThat(testItineraire.getDateDepart()).isEqualTo(testItineraire.getDateDepart());
        assertThat(testItineraire.getDateArrive()).isEqualTo(testItineraire.getDateArrive());
        assertThat(itineraireEs).isEqualToIgnoringGivenFields(testItineraire, "dateDepart", "dateArrive");
    }

    @Test
    @Transactional
    public void updateNonExistingItineraire() throws Exception {
        int databaseSizeBeforeUpdate = itineraireRepository.findAll().size();

        // Create the Itineraire

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restItineraireMockMvc.perform(put("/api/itineraires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itineraire)))
            .andExpect(status().isCreated());

        // Validate the Itineraire in the database
        List<Itineraire> itineraireList = itineraireRepository.findAll();
        assertThat(itineraireList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteItineraire() throws Exception {
        // Initialize the database
        itineraireRepository.saveAndFlush(itineraire);
        itineraireSearchRepository.save(itineraire);
        int databaseSizeBeforeDelete = itineraireRepository.findAll().size();

        // Get the itineraire
        restItineraireMockMvc.perform(delete("/api/itineraires/{id}", itineraire.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean itineraireExistsInEs = itineraireSearchRepository.exists(itineraire.getId());
        assertThat(itineraireExistsInEs).isFalse();

        // Validate the database is empty
        List<Itineraire> itineraireList = itineraireRepository.findAll();
        assertThat(itineraireList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchItineraire() throws Exception {
        // Initialize the database
        itineraireRepository.saveAndFlush(itineraire);
        itineraireSearchRepository.save(itineraire);

        // Search the itineraire
        restItineraireMockMvc.perform(get("/api/_search/itineraires?query=id:" + itineraire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itineraire.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDepart").value(hasItem(sameInstant(DEFAULT_DATE_DEPART))))
            .andExpect(jsonPath("$.[*].dateArrive").value(hasItem(sameInstant(DEFAULT_DATE_ARRIVE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Itineraire.class);
        Itineraire itineraire1 = new Itineraire();
        itineraire1.setId(1L);
        Itineraire itineraire2 = new Itineraire();
        itineraire2.setId(itineraire1.getId());
        assertThat(itineraire1).isEqualTo(itineraire2);
        itineraire2.setId(2L);
        assertThat(itineraire1).isNotEqualTo(itineraire2);
        itineraire1.setId(null);
        assertThat(itineraire1).isNotEqualTo(itineraire2);
    }
}
