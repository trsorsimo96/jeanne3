package ma.maman.jeanne.web.rest;

import ma.maman.jeanne.Jeanne3App;

import ma.maman.jeanne.domain.Routes;
import ma.maman.jeanne.domain.City;
import ma.maman.jeanne.domain.City;
import ma.maman.jeanne.repository.RoutesRepository;
import ma.maman.jeanne.repository.search.RoutesSearchRepository;
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
 * Test class for the RoutesResource REST controller.
 *
 * @see RoutesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Jeanne3App.class)
public class RoutesResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_DISTANCE = 1;
    private static final Integer UPDATED_DISTANCE = 2;

    @Autowired
    private RoutesRepository routesRepository;

    @Autowired
    private RoutesSearchRepository routesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRoutesMockMvc;

    private Routes routes;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RoutesResource routesResource = new RoutesResource(routesRepository, routesSearchRepository);
        this.restRoutesMockMvc = MockMvcBuilders.standaloneSetup(routesResource)
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
    public static Routes createEntity(EntityManager em) {
        Routes routes = new Routes()
            .code(DEFAULT_CODE)
            .distance(DEFAULT_DISTANCE);
        // Add required entity
        City origin = CityResourceIntTest.createEntity(em);
        em.persist(origin);
        em.flush();
        routes.setOrigin(origin);
        // Add required entity
        City destination = CityResourceIntTest.createEntity(em);
        em.persist(destination);
        em.flush();
        routes.setDestination(destination);
        return routes;
    }

    @Before
    public void initTest() {
        routesSearchRepository.deleteAll();
        routes = createEntity(em);
    }

    @Test
    @Transactional
    public void createRoutes() throws Exception {
        int databaseSizeBeforeCreate = routesRepository.findAll().size();

        // Create the Routes
        restRoutesMockMvc.perform(post("/api/routes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(routes)))
            .andExpect(status().isCreated());

        // Validate the Routes in the database
        List<Routes> routesList = routesRepository.findAll();
        assertThat(routesList).hasSize(databaseSizeBeforeCreate + 1);
        Routes testRoutes = routesList.get(routesList.size() - 1);
        assertThat(testRoutes.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testRoutes.getDistance()).isEqualTo(DEFAULT_DISTANCE);

        // Validate the Routes in Elasticsearch
        Routes routesEs = routesSearchRepository.findOne(testRoutes.getId());
        assertThat(routesEs).isEqualToIgnoringGivenFields(testRoutes);
    }

    @Test
    @Transactional
    public void createRoutesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = routesRepository.findAll().size();

        // Create the Routes with an existing ID
        routes.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoutesMockMvc.perform(post("/api/routes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(routes)))
            .andExpect(status().isBadRequest());

        // Validate the Routes in the database
        List<Routes> routesList = routesRepository.findAll();
        assertThat(routesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = routesRepository.findAll().size();
        // set the field null
        routes.setCode(null);

        // Create the Routes, which fails.

        restRoutesMockMvc.perform(post("/api/routes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(routes)))
            .andExpect(status().isBadRequest());

        List<Routes> routesList = routesRepository.findAll();
        assertThat(routesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRoutes() throws Exception {
        // Initialize the database
        routesRepository.saveAndFlush(routes);

        // Get all the routesList
        restRoutesMockMvc.perform(get("/api/routes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(routes.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].distance").value(hasItem(DEFAULT_DISTANCE)));
    }

    @Test
    @Transactional
    public void getRoutes() throws Exception {
        // Initialize the database
        routesRepository.saveAndFlush(routes);

        // Get the routes
        restRoutesMockMvc.perform(get("/api/routes/{id}", routes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(routes.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.distance").value(DEFAULT_DISTANCE));
    }

    @Test
    @Transactional
    public void getNonExistingRoutes() throws Exception {
        // Get the routes
        restRoutesMockMvc.perform(get("/api/routes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRoutes() throws Exception {
        // Initialize the database
        routesRepository.saveAndFlush(routes);
        routesSearchRepository.save(routes);
        int databaseSizeBeforeUpdate = routesRepository.findAll().size();

        // Update the routes
        Routes updatedRoutes = routesRepository.findOne(routes.getId());
        // Disconnect from session so that the updates on updatedRoutes are not directly saved in db
        em.detach(updatedRoutes);
        updatedRoutes
            .code(UPDATED_CODE)
            .distance(UPDATED_DISTANCE);

        restRoutesMockMvc.perform(put("/api/routes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRoutes)))
            .andExpect(status().isOk());

        // Validate the Routes in the database
        List<Routes> routesList = routesRepository.findAll();
        assertThat(routesList).hasSize(databaseSizeBeforeUpdate);
        Routes testRoutes = routesList.get(routesList.size() - 1);
        assertThat(testRoutes.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testRoutes.getDistance()).isEqualTo(UPDATED_DISTANCE);

        // Validate the Routes in Elasticsearch
        Routes routesEs = routesSearchRepository.findOne(testRoutes.getId());
        assertThat(routesEs).isEqualToIgnoringGivenFields(testRoutes);
    }

    @Test
    @Transactional
    public void updateNonExistingRoutes() throws Exception {
        int databaseSizeBeforeUpdate = routesRepository.findAll().size();

        // Create the Routes

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRoutesMockMvc.perform(put("/api/routes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(routes)))
            .andExpect(status().isCreated());

        // Validate the Routes in the database
        List<Routes> routesList = routesRepository.findAll();
        assertThat(routesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRoutes() throws Exception {
        // Initialize the database
        routesRepository.saveAndFlush(routes);
        routesSearchRepository.save(routes);
        int databaseSizeBeforeDelete = routesRepository.findAll().size();

        // Get the routes
        restRoutesMockMvc.perform(delete("/api/routes/{id}", routes.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean routesExistsInEs = routesSearchRepository.exists(routes.getId());
        assertThat(routesExistsInEs).isFalse();

        // Validate the database is empty
        List<Routes> routesList = routesRepository.findAll();
        assertThat(routesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRoutes() throws Exception {
        // Initialize the database
        routesRepository.saveAndFlush(routes);
        routesSearchRepository.save(routes);

        // Search the routes
        restRoutesMockMvc.perform(get("/api/_search/routes?query=id:" + routes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(routes.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].distance").value(hasItem(DEFAULT_DISTANCE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Routes.class);
        Routes routes1 = new Routes();
        routes1.setId(1L);
        Routes routes2 = new Routes();
        routes2.setId(routes1.getId());
        assertThat(routes1).isEqualTo(routes2);
        routes2.setId(2L);
        assertThat(routes1).isNotEqualTo(routes2);
        routes1.setId(null);
        assertThat(routes1).isNotEqualTo(routes2);
    }
}
