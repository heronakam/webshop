package com.ippon.shop.web.rest;

import com.ippon.shop.ShopApp;

import com.ippon.shop.domain.Caracteristique;
import com.ippon.shop.repository.CaracteristiqueRepository;
import com.ippon.shop.service.CaracteristiqueService;
import com.ippon.shop.repository.search.CaracteristiqueSearchRepository;

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
 * Test class for the CaracteristiqueResource REST controller.
 *
 * @see CaracteristiqueResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShopApp.class)
public class CaracteristiqueResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Inject
    private CaracteristiqueRepository caracteristiqueRepository;

    @Inject
    private CaracteristiqueService caracteristiqueService;

    @Inject
    private CaracteristiqueSearchRepository caracteristiqueSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCaracteristiqueMockMvc;

    private Caracteristique caracteristique;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CaracteristiqueResource caracteristiqueResource = new CaracteristiqueResource();
        ReflectionTestUtils.setField(caracteristiqueResource, "caracteristiqueService", caracteristiqueService);
        this.restCaracteristiqueMockMvc = MockMvcBuilders.standaloneSetup(caracteristiqueResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Caracteristique createEntity(EntityManager em) {
        Caracteristique caracteristique = new Caracteristique()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION);
        return caracteristique;
    }

    @Before
    public void initTest() {
        caracteristiqueSearchRepository.deleteAll();
        caracteristique = createEntity(em);
    }

    @Test
    @Transactional
    public void createCaracteristique() throws Exception {
        int databaseSizeBeforeCreate = caracteristiqueRepository.findAll().size();

        // Create the Caracteristique

        restCaracteristiqueMockMvc.perform(post("/api/caracteristiques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(caracteristique)))
                .andExpect(status().isCreated());

        // Validate the Caracteristique in the database
        List<Caracteristique> caracteristiques = caracteristiqueRepository.findAll();
        assertThat(caracteristiques).hasSize(databaseSizeBeforeCreate + 1);
        Caracteristique testCaracteristique = caracteristiques.get(caracteristiques.size() - 1);
        assertThat(testCaracteristique.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCaracteristique.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Caracteristique in ElasticSearch
        Caracteristique caracteristiqueEs = caracteristiqueSearchRepository.findOne(testCaracteristique.getId());
        assertThat(caracteristiqueEs).isEqualToComparingFieldByField(testCaracteristique);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = caracteristiqueRepository.findAll().size();
        // set the field null
        caracteristique.setName(null);

        // Create the Caracteristique, which fails.

        restCaracteristiqueMockMvc.perform(post("/api/caracteristiques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(caracteristique)))
                .andExpect(status().isBadRequest());

        List<Caracteristique> caracteristiques = caracteristiqueRepository.findAll();
        assertThat(caracteristiques).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = caracteristiqueRepository.findAll().size();
        // set the field null
        caracteristique.setDescription(null);

        // Create the Caracteristique, which fails.

        restCaracteristiqueMockMvc.perform(post("/api/caracteristiques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(caracteristique)))
                .andExpect(status().isBadRequest());

        List<Caracteristique> caracteristiques = caracteristiqueRepository.findAll();
        assertThat(caracteristiques).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCaracteristiques() throws Exception {
        // Initialize the database
        caracteristiqueRepository.saveAndFlush(caracteristique);

        // Get all the caracteristiques
        restCaracteristiqueMockMvc.perform(get("/api/caracteristiques?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(caracteristique.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getCaracteristique() throws Exception {
        // Initialize the database
        caracteristiqueRepository.saveAndFlush(caracteristique);

        // Get the caracteristique
        restCaracteristiqueMockMvc.perform(get("/api/caracteristiques/{id}", caracteristique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(caracteristique.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCaracteristique() throws Exception {
        // Get the caracteristique
        restCaracteristiqueMockMvc.perform(get("/api/caracteristiques/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCaracteristique() throws Exception {
        // Initialize the database
        caracteristiqueService.save(caracteristique);

        int databaseSizeBeforeUpdate = caracteristiqueRepository.findAll().size();

        // Update the caracteristique
        Caracteristique updatedCaracteristique = caracteristiqueRepository.findOne(caracteristique.getId());
        updatedCaracteristique
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION);

        restCaracteristiqueMockMvc.perform(put("/api/caracteristiques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCaracteristique)))
                .andExpect(status().isOk());

        // Validate the Caracteristique in the database
        List<Caracteristique> caracteristiques = caracteristiqueRepository.findAll();
        assertThat(caracteristiques).hasSize(databaseSizeBeforeUpdate);
        Caracteristique testCaracteristique = caracteristiques.get(caracteristiques.size() - 1);
        assertThat(testCaracteristique.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCaracteristique.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Caracteristique in ElasticSearch
        Caracteristique caracteristiqueEs = caracteristiqueSearchRepository.findOne(testCaracteristique.getId());
        assertThat(caracteristiqueEs).isEqualToComparingFieldByField(testCaracteristique);
    }

    @Test
    @Transactional
    public void deleteCaracteristique() throws Exception {
        // Initialize the database
        caracteristiqueService.save(caracteristique);

        int databaseSizeBeforeDelete = caracteristiqueRepository.findAll().size();

        // Get the caracteristique
        restCaracteristiqueMockMvc.perform(delete("/api/caracteristiques/{id}", caracteristique.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean caracteristiqueExistsInEs = caracteristiqueSearchRepository.exists(caracteristique.getId());
        assertThat(caracteristiqueExistsInEs).isFalse();

        // Validate the database is empty
        List<Caracteristique> caracteristiques = caracteristiqueRepository.findAll();
        assertThat(caracteristiques).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCaracteristique() throws Exception {
        // Initialize the database
        caracteristiqueService.save(caracteristique);

        // Search the caracteristique
        restCaracteristiqueMockMvc.perform(get("/api/_search/caracteristiques?query=id:" + caracteristique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caracteristique.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
