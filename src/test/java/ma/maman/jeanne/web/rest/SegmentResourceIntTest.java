package ma.maman.jeanne.web.rest;

import ma.maman.jeanne.Jeanne3App;

import ma.maman.jeanne.domain.Segment;
import ma.maman.jeanne.domain.Voyage;
import ma.maman.jeanne.domain.Booking;
import ma.maman.jeanne.repository.SegmentRepository;
import ma.maman.jeanne.repository.search.SegmentSearchRepository;
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

import ma.maman.jeanne.domain.enumeration.StatusSegment;
/**
 * Test class for the SegmentResource REST controller.
 *
 * @see SegmentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Jeanne3App.class)
public class SegmentResourceIntTest {

    private static final StatusSegment DEFAULT_STATUS = StatusSegment.U;
    private static final StatusSegment UPDATED_STATUS = StatusSegment.O;

    @Autowired
    private SegmentRepository segmentRepository;

    @Autowired
    private SegmentSearchRepository segmentSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSegmentMockMvc;

    private Segment segment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SegmentResource segmentResource = new SegmentResource(segmentRepository, segmentSearchRepository);
        this.restSegmentMockMvc = MockMvcBuilders.standaloneSetup(segmentResource)
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
    public static Segment createEntity(EntityManager em) {
        Segment segment = new Segment()
            .status(DEFAULT_STATUS);
        // Add required entity
        Voyage voyage = VoyageResourceIntTest.createEntity(em);
        em.persist(voyage);
        em.flush();
        segment.setVoyage(voyage);
        // Add required entity
        Booking booking = BookingResourceIntTest.createEntity(em);
        em.persist(booking);
        em.flush();
        segment.setBooking(booking);
        return segment;
    }

    @Before
    public void initTest() {
        segmentSearchRepository.deleteAll();
        segment = createEntity(em);
    }

    @Test
    @Transactional
    public void createSegment() throws Exception {
        int databaseSizeBeforeCreate = segmentRepository.findAll().size();

        // Create the Segment
        restSegmentMockMvc.perform(post("/api/segments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(segment)))
            .andExpect(status().isCreated());

        // Validate the Segment in the database
        List<Segment> segmentList = segmentRepository.findAll();
        assertThat(segmentList).hasSize(databaseSizeBeforeCreate + 1);
        Segment testSegment = segmentList.get(segmentList.size() - 1);
        assertThat(testSegment.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Segment in Elasticsearch
        Segment segmentEs = segmentSearchRepository.findOne(testSegment.getId());
        assertThat(segmentEs).isEqualToIgnoringGivenFields(testSegment);
    }

    @Test
    @Transactional
    public void createSegmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = segmentRepository.findAll().size();

        // Create the Segment with an existing ID
        segment.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSegmentMockMvc.perform(post("/api/segments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(segment)))
            .andExpect(status().isBadRequest());

        // Validate the Segment in the database
        List<Segment> segmentList = segmentRepository.findAll();
        assertThat(segmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSegments() throws Exception {
        // Initialize the database
        segmentRepository.saveAndFlush(segment);

        // Get all the segmentList
        restSegmentMockMvc.perform(get("/api/segments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(segment.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getSegment() throws Exception {
        // Initialize the database
        segmentRepository.saveAndFlush(segment);

        // Get the segment
        restSegmentMockMvc.perform(get("/api/segments/{id}", segment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(segment.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSegment() throws Exception {
        // Get the segment
        restSegmentMockMvc.perform(get("/api/segments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSegment() throws Exception {
        // Initialize the database
        segmentRepository.saveAndFlush(segment);
        segmentSearchRepository.save(segment);
        int databaseSizeBeforeUpdate = segmentRepository.findAll().size();

        // Update the segment
        Segment updatedSegment = segmentRepository.findOne(segment.getId());
        // Disconnect from session so that the updates on updatedSegment are not directly saved in db
        em.detach(updatedSegment);
        updatedSegment
            .status(UPDATED_STATUS);

        restSegmentMockMvc.perform(put("/api/segments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSegment)))
            .andExpect(status().isOk());

        // Validate the Segment in the database
        List<Segment> segmentList = segmentRepository.findAll();
        assertThat(segmentList).hasSize(databaseSizeBeforeUpdate);
        Segment testSegment = segmentList.get(segmentList.size() - 1);
        assertThat(testSegment.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Segment in Elasticsearch
        Segment segmentEs = segmentSearchRepository.findOne(testSegment.getId());
        assertThat(segmentEs).isEqualToIgnoringGivenFields(testSegment);
    }

    @Test
    @Transactional
    public void updateNonExistingSegment() throws Exception {
        int databaseSizeBeforeUpdate = segmentRepository.findAll().size();

        // Create the Segment

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSegmentMockMvc.perform(put("/api/segments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(segment)))
            .andExpect(status().isCreated());

        // Validate the Segment in the database
        List<Segment> segmentList = segmentRepository.findAll();
        assertThat(segmentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSegment() throws Exception {
        // Initialize the database
        segmentRepository.saveAndFlush(segment);
        segmentSearchRepository.save(segment);
        int databaseSizeBeforeDelete = segmentRepository.findAll().size();

        // Get the segment
        restSegmentMockMvc.perform(delete("/api/segments/{id}", segment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean segmentExistsInEs = segmentSearchRepository.exists(segment.getId());
        assertThat(segmentExistsInEs).isFalse();

        // Validate the database is empty
        List<Segment> segmentList = segmentRepository.findAll();
        assertThat(segmentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSegment() throws Exception {
        // Initialize the database
        segmentRepository.saveAndFlush(segment);
        segmentSearchRepository.save(segment);

        // Search the segment
        restSegmentMockMvc.perform(get("/api/_search/segments?query=id:" + segment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(segment.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Segment.class);
        Segment segment1 = new Segment();
        segment1.setId(1L);
        Segment segment2 = new Segment();
        segment2.setId(segment1.getId());
        assertThat(segment1).isEqualTo(segment2);
        segment2.setId(2L);
        assertThat(segment1).isNotEqualTo(segment2);
        segment1.setId(null);
        assertThat(segment1).isNotEqualTo(segment2);
    }
}
