package com.ippon.shop.web.rest;

import com.ippon.shop.ShopApp;

import com.ippon.shop.domain.Marque;
import com.ippon.shop.repository.MarqueRepository;
import com.ippon.shop.service.MarqueService;
import com.ippon.shop.repository.search.MarqueSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MarqueResource REST controller.
 *
 * @see MarqueResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShopApp.class)
public class MarqueResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PRESENTATION = "AAAAAAAAAA";
    private static final String UPDATED_PRESENTATION = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO_URL = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_URL = "BBBBBBBBBB";

    @Inject
    private MarqueRepository marqueRepository;

    @Inject
    private MarqueService marqueService;

    @Inject
    private MarqueSearchRepository marqueSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restMarqueMockMvc;

    private Marque marque;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MarqueResource marqueResource = new MarqueResource();
        ReflectionTestUtils.setField(marqueResource, "marqueService", marqueService);
        this.restMarqueMockMvc = MockMvcBuilders.standaloneSetup(marqueResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Marque createEntity(EntityManager em) {
        Marque marque = new Marque()
                .name(DEFAULT_NAME)
                .presentation(DEFAULT_PRESENTATION)
                .logoUrl(DEFAULT_LOGO_URL);
        return marque;
    }

    @Before
    public void initTest() {
        marqueSearchRepository.deleteAll();
        marque = createEntity(em);
    }

    @Test
    @Transactional
    public void createMarque() throws Exception {
        int databaseSizeBeforeCreate = marqueRepository.findAll().size();

        // Create the Marque

        restMarqueMockMvc.perform(post("/api/marques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(marque)))
                .andExpect(status().isCreated());

        // Validate the Marque in the database
        List<Marque> marques = marqueRepository.findAll();
        assertThat(marques).hasSize(databaseSizeBeforeCreate + 1);
        Marque testMarque = marques.get(marques.size() - 1);
        assertThat(testMarque.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMarque.getPresentation()).isEqualTo(DEFAULT_PRESENTATION);
        assertThat(testMarque.getLogoUrl()).isEqualTo(DEFAULT_LOGO_URL);

        // Validate the Marque in ElasticSearch
        Marque marqueEs = marqueSearchRepository.findOne(testMarque.getId());
        assertThat(marqueEs).isEqualToComparingFieldByField(testMarque);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = marqueRepository.findAll().size();
        // set the field null
        marque.setName(null);

        // Create the Marque, which fails.

        restMarqueMockMvc.perform(post("/api/marques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(marque)))
                .andExpect(status().isBadRequest());

        List<Marque> marques = marqueRepository.findAll();
        assertThat(marques).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPresentationIsRequired() throws Exception {
        int databaseSizeBeforeTest = marqueRepository.findAll().size();
        // set the field null
        marque.setPresentation(null);

        // Create the Marque, which fails.

        restMarqueMockMvc.perform(post("/api/marques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(marque)))
                .andExpect(status().isBadRequest());

        List<Marque> marques = marqueRepository.findAll();
        assertThat(marques).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMarques() throws Exception {
        // Initialize the database
        marqueRepository.saveAndFlush(marque);

        // Get all the marques
        restMarqueMockMvc.perform(get("/api/marques?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(marque.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].presentation").value(hasItem(DEFAULT_PRESENTATION.toString())))
                .andExpect(jsonPath("$.[*].logoUrl").value(hasItem(DEFAULT_LOGO_URL.toString())));
    }

    @Test
    @Transactional
    public void getMarque() throws Exception {
        // Initialize the database
        marqueRepository.saveAndFlush(marque);

        // Get the marque
        restMarqueMockMvc.perform(get("/api/marques/{id}", marque.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(marque.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.presentation").value(DEFAULT_PRESENTATION.toString()))
            .andExpect(jsonPath("$.logoUrl").value(DEFAULT_LOGO_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMarque() throws Exception {
        // Get the marque
        restMarqueMockMvc.perform(get("/api/marques/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMarque() throws Exception {
        // Initialize the database
        marqueService.save(marque);

        int databaseSizeBeforeUpdate = marqueRepository.findAll().size();

        // Update the marque
        Marque updatedMarque = marqueRepository.findOne(marque.getId());
        updatedMarque
                .name(UPDATED_NAME)
                .presentation(UPDATED_PRESENTATION)
                .logoUrl(UPDATED_LOGO_URL);

        restMarqueMockMvc.perform(put("/api/marques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMarque)))
                .andExpect(status().isOk());

        // Validate the Marque in the database
        List<Marque> marques = marqueRepository.findAll();
        assertThat(marques).hasSize(databaseSizeBeforeUpdate);
        Marque testMarque = marques.get(marques.size() - 1);
        assertThat(testMarque.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMarque.getPresentation()).isEqualTo(UPDATED_PRESENTATION);
        assertThat(testMarque.getLogoUrl()).isEqualTo(UPDATED_LOGO_URL);

        // Validate the Marque in ElasticSearch
        Marque marqueEs = marqueSearchRepository.findOne(testMarque.getId());
        assertThat(marqueEs).isEqualToComparingFieldByField(testMarque);
    }

    @Test
    @Transactional
    public void deleteMarque() throws Exception {
        // Initialize the database
        marqueService.save(marque);

        int databaseSizeBeforeDelete = marqueRepository.findAll().size();

        // Get the marque
        restMarqueMockMvc.perform(delete("/api/marques/{id}", marque.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean marqueExistsInEs = marqueSearchRepository.exists(marque.getId());
        assertThat(marqueExistsInEs).isFalse();

        // Validate the database is empty
        List<Marque> marques = marqueRepository.findAll();
        assertThat(marques).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMarque() throws Exception {
        // Initialize the database
        marqueService.save(marque);

        // Search the marque
        restMarqueMockMvc.perform(get("/api/_search/marques?query=id:" + marque.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marque.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].presentation").value(hasItem(DEFAULT_PRESENTATION.toString())))
            .andExpect(jsonPath("$.[*].logoUrl").value(hasItem(DEFAULT_LOGO_URL.toString())));
    }
}
