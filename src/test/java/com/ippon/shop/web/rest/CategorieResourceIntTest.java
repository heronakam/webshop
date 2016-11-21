package com.ippon.shop.web.rest;

import com.ippon.shop.ShopApp;

import com.ippon.shop.domain.Categorie;
import com.ippon.shop.repository.CategorieRepository;
import com.ippon.shop.service.CategorieService;
import com.ippon.shop.repository.search.CategorieSearchRepository;

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
 * Test class for the CategorieResource REST controller.
 *
 * @see CategorieResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShopApp.class)
public class CategorieResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ICON_URL = "AAAAAAAAAA";
    private static final String UPDATED_ICON_URL = "BBBBBBBBBB";

    @Inject
    private CategorieRepository categorieRepository;

    @Inject
    private CategorieService categorieService;

    @Inject
    private CategorieSearchRepository categorieSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCategorieMockMvc;

    private Categorie categorie;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CategorieResource categorieResource = new CategorieResource();
        ReflectionTestUtils.setField(categorieResource, "categorieService", categorieService);
        this.restCategorieMockMvc = MockMvcBuilders.standaloneSetup(categorieResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Categorie createEntity(EntityManager em) {
        Categorie categorie = new Categorie()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .iconUrl(DEFAULT_ICON_URL);
        return categorie;
    }

    @Before
    public void initTest() {
        categorieSearchRepository.deleteAll();
        categorie = createEntity(em);
    }

    @Test
    @Transactional
    public void createCategorie() throws Exception {
        int databaseSizeBeforeCreate = categorieRepository.findAll().size();

        // Create the Categorie

        restCategorieMockMvc.perform(post("/api/categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(categorie)))
                .andExpect(status().isCreated());

        // Validate the Categorie in the database
        List<Categorie> categories = categorieRepository.findAll();
        assertThat(categories).hasSize(databaseSizeBeforeCreate + 1);
        Categorie testCategorie = categories.get(categories.size() - 1);
        assertThat(testCategorie.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCategorie.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCategorie.getIconUrl()).isEqualTo(DEFAULT_ICON_URL);

        // Validate the Categorie in ElasticSearch
        Categorie categorieEs = categorieSearchRepository.findOne(testCategorie.getId());
        assertThat(categorieEs).isEqualToComparingFieldByField(testCategorie);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = categorieRepository.findAll().size();
        // set the field null
        categorie.setName(null);

        // Create the Categorie, which fails.

        restCategorieMockMvc.perform(post("/api/categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(categorie)))
                .andExpect(status().isBadRequest());

        List<Categorie> categories = categorieRepository.findAll();
        assertThat(categories).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = categorieRepository.findAll().size();
        // set the field null
        categorie.setDescription(null);

        // Create the Categorie, which fails.

        restCategorieMockMvc.perform(post("/api/categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(categorie)))
                .andExpect(status().isBadRequest());

        List<Categorie> categories = categorieRepository.findAll();
        assertThat(categories).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCategories() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        // Get all the categories
        restCategorieMockMvc.perform(get("/api/categories?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(categorie.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].iconUrl").value(hasItem(DEFAULT_ICON_URL.toString())));
    }

    @Test
    @Transactional
    public void getCategorie() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        // Get the categorie
        restCategorieMockMvc.perform(get("/api/categories/{id}", categorie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(categorie.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.iconUrl").value(DEFAULT_ICON_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCategorie() throws Exception {
        // Get the categorie
        restCategorieMockMvc.perform(get("/api/categories/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCategorie() throws Exception {
        // Initialize the database
        categorieService.save(categorie);

        int databaseSizeBeforeUpdate = categorieRepository.findAll().size();

        // Update the categorie
        Categorie updatedCategorie = categorieRepository.findOne(categorie.getId());
        updatedCategorie
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .iconUrl(UPDATED_ICON_URL);

        restCategorieMockMvc.perform(put("/api/categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCategorie)))
                .andExpect(status().isOk());

        // Validate the Categorie in the database
        List<Categorie> categories = categorieRepository.findAll();
        assertThat(categories).hasSize(databaseSizeBeforeUpdate);
        Categorie testCategorie = categories.get(categories.size() - 1);
        assertThat(testCategorie.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCategorie.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCategorie.getIconUrl()).isEqualTo(UPDATED_ICON_URL);

        // Validate the Categorie in ElasticSearch
        Categorie categorieEs = categorieSearchRepository.findOne(testCategorie.getId());
        assertThat(categorieEs).isEqualToComparingFieldByField(testCategorie);
    }

    @Test
    @Transactional
    public void deleteCategorie() throws Exception {
        // Initialize the database
        categorieService.save(categorie);

        int databaseSizeBeforeDelete = categorieRepository.findAll().size();

        // Get the categorie
        restCategorieMockMvc.perform(delete("/api/categories/{id}", categorie.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean categorieExistsInEs = categorieSearchRepository.exists(categorie.getId());
        assertThat(categorieExistsInEs).isFalse();

        // Validate the database is empty
        List<Categorie> categories = categorieRepository.findAll();
        assertThat(categories).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCategorie() throws Exception {
        // Initialize the database
        categorieService.save(categorie);

        // Search the categorie
        restCategorieMockMvc.perform(get("/api/_search/categories?query=id:" + categorie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categorie.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].iconUrl").value(hasItem(DEFAULT_ICON_URL.toString())));
    }
}
