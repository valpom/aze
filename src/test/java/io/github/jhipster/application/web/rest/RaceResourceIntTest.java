package io.github.jhipster.application.web.rest;

import io.github.jhipster.application.AzeApp;

import io.github.jhipster.application.domain.Race;
import io.github.jhipster.application.repository.RaceRepository;
import io.github.jhipster.application.web.rest.errors.ExceptionTranslator;

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


import static io.github.jhipster.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RaceResource REST controller.
 *
 * @see RaceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AzeApp.class)
public class RaceResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    @Autowired
    private RaceRepository raceRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRaceMockMvc;

    private Race race;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RaceResource raceResource = new RaceResource(raceRepository);
        this.restRaceMockMvc = MockMvcBuilders.standaloneSetup(raceResource)
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
    public static Race createEntity(EntityManager em) {
        Race race = new Race()
            .title(DEFAULT_TITLE);
        return race;
    }

    @Before
    public void initTest() {
        race = createEntity(em);
    }

    @Test
    @Transactional
    public void createRace() throws Exception {
        int databaseSizeBeforeCreate = raceRepository.findAll().size();

        // Create the Race
        restRaceMockMvc.perform(post("/api/races")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(race)))
            .andExpect(status().isCreated());

        // Validate the Race in the database
        List<Race> raceList = raceRepository.findAll();
        assertThat(raceList).hasSize(databaseSizeBeforeCreate + 1);
        Race testRace = raceList.get(raceList.size() - 1);
        assertThat(testRace.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void createRaceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = raceRepository.findAll().size();

        // Create the Race with an existing ID
        race.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRaceMockMvc.perform(post("/api/races")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(race)))
            .andExpect(status().isBadRequest());

        // Validate the Race in the database
        List<Race> raceList = raceRepository.findAll();
        assertThat(raceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = raceRepository.findAll().size();
        // set the field null
        race.setTitle(null);

        // Create the Race, which fails.

        restRaceMockMvc.perform(post("/api/races")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(race)))
            .andExpect(status().isBadRequest());

        List<Race> raceList = raceRepository.findAll();
        assertThat(raceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRaces() throws Exception {
        // Initialize the database
        raceRepository.saveAndFlush(race);

        // Get all the raceList
        restRaceMockMvc.perform(get("/api/races?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(race.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }
    
    @Test
    @Transactional
    public void getRace() throws Exception {
        // Initialize the database
        raceRepository.saveAndFlush(race);

        // Get the race
        restRaceMockMvc.perform(get("/api/races/{id}", race.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(race.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRace() throws Exception {
        // Get the race
        restRaceMockMvc.perform(get("/api/races/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRace() throws Exception {
        // Initialize the database
        raceRepository.saveAndFlush(race);

        int databaseSizeBeforeUpdate = raceRepository.findAll().size();

        // Update the race
        Race updatedRace = raceRepository.findById(race.getId()).get();
        // Disconnect from session so that the updates on updatedRace are not directly saved in db
        em.detach(updatedRace);
        updatedRace
            .title(UPDATED_TITLE);

        restRaceMockMvc.perform(put("/api/races")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRace)))
            .andExpect(status().isOk());

        // Validate the Race in the database
        List<Race> raceList = raceRepository.findAll();
        assertThat(raceList).hasSize(databaseSizeBeforeUpdate);
        Race testRace = raceList.get(raceList.size() - 1);
        assertThat(testRace.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingRace() throws Exception {
        int databaseSizeBeforeUpdate = raceRepository.findAll().size();

        // Create the Race

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRaceMockMvc.perform(put("/api/races")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(race)))
            .andExpect(status().isBadRequest());

        // Validate the Race in the database
        List<Race> raceList = raceRepository.findAll();
        assertThat(raceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRace() throws Exception {
        // Initialize the database
        raceRepository.saveAndFlush(race);

        int databaseSizeBeforeDelete = raceRepository.findAll().size();

        // Get the race
        restRaceMockMvc.perform(delete("/api/races/{id}", race.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Race> raceList = raceRepository.findAll();
        assertThat(raceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Race.class);
        Race race1 = new Race();
        race1.setId(1L);
        Race race2 = new Race();
        race2.setId(race1.getId());
        assertThat(race1).isEqualTo(race2);
        race2.setId(2L);
        assertThat(race1).isNotEqualTo(race2);
        race1.setId(null);
        assertThat(race1).isNotEqualTo(race2);
    }
}
