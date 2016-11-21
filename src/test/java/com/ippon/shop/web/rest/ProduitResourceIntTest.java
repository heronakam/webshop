package com.ippon.shop.web.rest;

import com.ippon.shop.ShopApp;

import com.ippon.shop.domain.Produit;
import com.ippon.shop.domain.Categorie;
import com.ippon.shop.domain.Caracteristique;
import com.ippon.shop.domain.Marque;
import com.ippon.shop.repository.ProduitRepository;
import com.ippon.shop.service.ProduitService;
import com.ippon.shop.repository.search.ProduitSearchRepository;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProduitResource REST controller.
 *
 * @see ProduitResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShopApp.class)
public class ProduitResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRIX_UNITAIRE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRIX_UNITAIRE = new BigDecimal(2);

    private static final Float DEFAULT_SOLDE = 1F;
    private static final Float UPDATED_SOLDE = 2F;

    private static final Integer DEFAULT_QTE_EN_STOCK = 1;
    private static final Integer UPDATED_QTE_EN_STOCK = 2;

    @Inject
    private ProduitRepository produitRepository;

    @Inject
    private ProduitService produitService;

    @Inject
    private ProduitSearchRepository produitSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restProduitMockMvc;

    private Produit produit;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProduitResource produitResource = new ProduitResource();
        ReflectionTestUtils.setField(produitResource, "produitService", produitService);
        this.restProduitMockMvc = MockMvcBuilders.standaloneSetup(produitResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Produit createEntity(EntityManager em) {
        Produit produit = new Produit()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .imageUrl(DEFAULT_IMAGE_URL)
                .prixUnitaire(DEFAULT_PRIX_UNITAIRE)
                .solde(DEFAULT_SOLDE)
                .qteEnStock(DEFAULT_QTE_EN_STOCK);
        // Add required entity
        Categorie categorie = CategorieResourceIntTest.createEntity(em);
        em.persist(categorie);
        em.flush();
        produit.setCategorie(categorie);
        // Add required entity
        Caracteristique caracteristique = CaracteristiqueResourceIntTest.createEntity(em);
        em.persist(caracteristique);
        em.flush();
        produit.getCaracteristiques().add(caracteristique);
        // Add required entity
        Marque marque = MarqueResourceIntTest.createEntity(em);
        em.persist(marque);
        em.flush();
        produit.setMarque(marque);
        return produit;
    }

    @Before
    public void initTest() {
        produitSearchRepository.deleteAll();
        produit = createEntity(em);
    }

    @Test
    @Transactional
    public void createProduit() throws Exception {
        int databaseSizeBeforeCreate = produitRepository.findAll().size();

        // Create the Produit

        restProduitMockMvc.perform(post("/api/produits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(produit)))
                .andExpect(status().isCreated());

        // Validate the Produit in the database
        List<Produit> produits = produitRepository.findAll();
        assertThat(produits).hasSize(databaseSizeBeforeCreate + 1);
        Produit testProduit = produits.get(produits.size() - 1);
        assertThat(testProduit.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduit.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProduit.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testProduit.getPrixUnitaire()).isEqualTo(DEFAULT_PRIX_UNITAIRE);
        assertThat(testProduit.getSolde()).isEqualTo(DEFAULT_SOLDE);
        assertThat(testProduit.getQteEnStock()).isEqualTo(DEFAULT_QTE_EN_STOCK);

        // Validate the Produit in ElasticSearch
        Produit produitEs = produitSearchRepository.findOne(testProduit.getId());
        assertThat(produitEs).isEqualToComparingFieldByField(testProduit);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = produitRepository.findAll().size();
        // set the field null
        produit.setName(null);

        // Create the Produit, which fails.

        restProduitMockMvc.perform(post("/api/produits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(produit)))
                .andExpect(status().isBadRequest());

        List<Produit> produits = produitRepository.findAll();
        assertThat(produits).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = produitRepository.findAll().size();
        // set the field null
        produit.setDescription(null);

        // Create the Produit, which fails.

        restProduitMockMvc.perform(post("/api/produits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(produit)))
                .andExpect(status().isBadRequest());

        List<Produit> produits = produitRepository.findAll();
        assertThat(produits).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkImageUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = produitRepository.findAll().size();
        // set the field null
        produit.setImageUrl(null);

        // Create the Produit, which fails.

        restProduitMockMvc.perform(post("/api/produits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(produit)))
                .andExpect(status().isBadRequest());

        List<Produit> produits = produitRepository.findAll();
        assertThat(produits).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrixUnitaireIsRequired() throws Exception {
        int databaseSizeBeforeTest = produitRepository.findAll().size();
        // set the field null
        produit.setPrixUnitaire(null);

        // Create the Produit, which fails.

        restProduitMockMvc.perform(post("/api/produits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(produit)))
                .andExpect(status().isBadRequest());

        List<Produit> produits = produitRepository.findAll();
        assertThat(produits).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQteEnStockIsRequired() throws Exception {
        int databaseSizeBeforeTest = produitRepository.findAll().size();
        // set the field null
        produit.setQteEnStock(null);

        // Create the Produit, which fails.

        restProduitMockMvc.perform(post("/api/produits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(produit)))
                .andExpect(status().isBadRequest());

        List<Produit> produits = produitRepository.findAll();
        assertThat(produits).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProduits() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produits
        restProduitMockMvc.perform(get("/api/produits?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(produit.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL.toString())))
                .andExpect(jsonPath("$.[*].prixUnitaire").value(hasItem(DEFAULT_PRIX_UNITAIRE.intValue())))
                .andExpect(jsonPath("$.[*].solde").value(hasItem(DEFAULT_SOLDE.doubleValue())))
                .andExpect(jsonPath("$.[*].qteEnStock").value(hasItem(DEFAULT_QTE_EN_STOCK)));
    }

    @Test
    @Transactional
    public void getProduit() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get the produit
        restProduitMockMvc.perform(get("/api/produits/{id}", produit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(produit.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL.toString()))
            .andExpect(jsonPath("$.prixUnitaire").value(DEFAULT_PRIX_UNITAIRE.intValue()))
            .andExpect(jsonPath("$.solde").value(DEFAULT_SOLDE.doubleValue()))
            .andExpect(jsonPath("$.qteEnStock").value(DEFAULT_QTE_EN_STOCK));
    }

    @Test
    @Transactional
    public void getNonExistingProduit() throws Exception {
        // Get the produit
        restProduitMockMvc.perform(get("/api/produits/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProduit() throws Exception {
        // Initialize the database
        produitService.save(produit);

        int databaseSizeBeforeUpdate = produitRepository.findAll().size();

        // Update the produit
        Produit updatedProduit = produitRepository.findOne(produit.getId());
        updatedProduit
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .imageUrl(UPDATED_IMAGE_URL)
                .prixUnitaire(UPDATED_PRIX_UNITAIRE)
                .solde(UPDATED_SOLDE)
                .qteEnStock(UPDATED_QTE_EN_STOCK);

        restProduitMockMvc.perform(put("/api/produits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProduit)))
                .andExpect(status().isOk());

        // Validate the Produit in the database
        List<Produit> produits = produitRepository.findAll();
        assertThat(produits).hasSize(databaseSizeBeforeUpdate);
        Produit testProduit = produits.get(produits.size() - 1);
        assertThat(testProduit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduit.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testProduit.getPrixUnitaire()).isEqualTo(UPDATED_PRIX_UNITAIRE);
        assertThat(testProduit.getSolde()).isEqualTo(UPDATED_SOLDE);
        assertThat(testProduit.getQteEnStock()).isEqualTo(UPDATED_QTE_EN_STOCK);

        // Validate the Produit in ElasticSearch
        Produit produitEs = produitSearchRepository.findOne(testProduit.getId());
        assertThat(produitEs).isEqualToComparingFieldByField(testProduit);
    }

    @Test
    @Transactional
    public void deleteProduit() throws Exception {
        // Initialize the database
        produitService.save(produit);

        int databaseSizeBeforeDelete = produitRepository.findAll().size();

        // Get the produit
        restProduitMockMvc.perform(delete("/api/produits/{id}", produit.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean produitExistsInEs = produitSearchRepository.exists(produit.getId());
        assertThat(produitExistsInEs).isFalse();

        // Validate the database is empty
        List<Produit> produits = produitRepository.findAll();
        assertThat(produits).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProduit() throws Exception {
        // Initialize the database
        produitService.save(produit);

        // Search the produit
        restProduitMockMvc.perform(get("/api/_search/produits?query=id:" + produit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(produit.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].prixUnitaire").value(hasItem(DEFAULT_PRIX_UNITAIRE.intValue())))
            .andExpect(jsonPath("$.[*].solde").value(hasItem(DEFAULT_SOLDE.doubleValue())))
            .andExpect(jsonPath("$.[*].qteEnStock").value(hasItem(DEFAULT_QTE_EN_STOCK)));
    }
}
