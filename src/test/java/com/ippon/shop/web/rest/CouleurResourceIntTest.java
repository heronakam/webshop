package com.ippon.shop.web.rest;

import com.ippon.shop.ShopApp;

import com.ippon.shop.domain.Couleur;
import com.ippon.shop.repository.CouleurRepository;
import com.ippon.shop.service.CouleurService;
import com.ippon.shop.repository.search.CouleurSearchRepository;

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
 * Test class for the CouleurResource REST controller.
 *
 * @see CouleurResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShopApp.class)
public class CouleurResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_HEXADECIMAL = "AAAAAAAAAA";
    private static final String UPDATED_CODE_HEXADECIMAL = "BBBBBBBBBB";

    @Inject
    private CouleurRepository couleurRepository;

    @Inject
    private CouleurService couleurService;

    @Inject
    private CouleurSearchRepository couleurSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCouleurMockMvc;

    private Couleur couleur;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CouleurResource couleurResource = new CouleurResource();
        ReflectionTestUtils.setField(couleurResource, "couleurService", couleurService);
        this.restCouleurMockMvc = MockMvcBuilders.standaloneSetup(couleurResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Couleur createEntity(EntityManager em) {
        Couleur couleur = new Couleur()
                .name(DEFAULT_NAME)
                .codeHexadecimal(DEFAULT_CODE_HEXADECIMAL);
        return couleur;
    }

    @Before
    public void initTest() {
        couleurSearchRepository.deleteAll();
        couleur = createEntity(em);
    }

    @Test
    @Transactional
    public void createCouleur() throws Exception {
        int databaseSizeBeforeCreate = couleurRepository.findAll().size();

        // Create the Couleur

        restCouleurMockMvc.perform(post("/api/couleurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(couleur)))
                .andExpect(status().isCreated());

        // Validate the Couleur in the database
        List<Couleur> couleurs = couleurRepository.findAll();
        assertThat(couleurs).hasSize(databaseSizeBeforeCreate + 1);
        Couleur testCouleur = couleurs.get(couleurs.size() - 1);
        assertThat(testCouleur.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCouleur.getCodeHexadecimal()).isEqualTo(DEFAULT_CODE_HEXADECIMAL);

        // Validate the Couleur in ElasticSearch
        Couleur couleurEs = couleurSearchRepository.findOne(testCouleur.getId());
        assertThat(couleurEs).isEqualToComparingFieldByField(testCouleur);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = couleurRepository.findAll().size();
        // set the field null
        couleur.setName(null);

        // Create the Couleur, which fails.

        restCouleurMockMvc.perform(post("/api/couleurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(couleur)))
                .andExpect(status().isBadRequest());

        List<Couleur> couleurs = couleurRepository.findAll();
        assertThat(couleurs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCouleurs() throws Exception {
        // Initialize the database
        couleurRepository.saveAndFlush(couleur);

        // Get all the couleurs
        restCouleurMockMvc.perform(get("/api/couleurs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(couleur.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].codeHexadecimal").value(hasItem(DEFAULT_CODE_HEXADECIMAL.toString())));
    }

    @Test
    @Transactional
    public void getCouleur() throws Exception {
        // Initialize the database
        couleurRepository.saveAndFlush(couleur);

        // Get the couleur
        restCouleurMockMvc.perform(get("/api/couleurs/{id}", couleur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(couleur.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.codeHexadecimal").value(DEFAULT_CODE_HEXADECIMAL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCouleur() throws Exception {
        // Get the couleur
        restCouleurMockMvc.perform(get("/api/couleurs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCouleur() throws Exception {
        // Initialize the database
        couleurService.save(couleur);

        int databaseSizeBeforeUpdate = couleurRepository.findAll().size();

        // Update the couleur
        Couleur updatedCouleur = couleurRepository.findOne(couleur.getId());
        updatedCouleur
                .name(UPDATED_NAME)
                .codeHexadecimal(UPDATED_CODE_HEXADECIMAL);

        restCouleurMockMvc.perform(put("/api/couleurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCouleur)))
                .andExpect(status().isOk());

        // Validate the Couleur in the database
        List<Couleur> couleurs = couleurRepository.findAll();
        assertThat(couleurs).hasSize(databaseSizeBeforeUpdate);
        Couleur testCouleur = couleurs.get(couleurs.size() - 1);
        assertThat(testCouleur.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCouleur.getCodeHexadecimal()).isEqualTo(UPDATED_CODE_HEXADECIMAL);

        // Validate the Couleur in ElasticSearch
        Couleur couleurEs = couleurSearchRepository.findOne(testCouleur.getId());
        assertThat(couleurEs).isEqualToComparingFieldByField(testCouleur);
    }

    @Test
    @Transactional
    public void deleteCouleur() throws Exception {
        // Initialize the database
        couleurService.save(couleur);

        int databaseSizeBeforeDelete = couleurRepository.findAll().size();

        // Get the couleur
        restCouleurMockMvc.perform(delete("/api/couleurs/{id}", couleur.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean couleurExistsInEs = couleurSearchRepository.exists(couleur.getId());
        assertThat(couleurExistsInEs).isFalse();

        // Validate the database is empty
        List<Couleur> couleurs = couleurRepository.findAll();
        assertThat(couleurs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCouleur() throws Exception {
        // Initialize the database
        couleurService.save(couleur);

        // Search the couleur
        restCouleurMockMvc.perform(get("/api/_search/couleurs?query=id:" + couleur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(couleur.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].codeHexadecimal").value(hasItem(DEFAULT_CODE_HEXADECIMAL.toString())));
    }
}
