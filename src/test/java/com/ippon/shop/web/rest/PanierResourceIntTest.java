package com.ippon.shop.web.rest;

import com.ippon.shop.ShopApp;

import com.ippon.shop.domain.Panier;
import com.ippon.shop.domain.Produit;
import com.ippon.shop.repository.PanierRepository;
import com.ippon.shop.service.PanierService;
import com.ippon.shop.repository.search.PanierSearchRepository;

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
 * Test class for the PanierResource REST controller.
 *
 * @see PanierResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShopApp.class)
public class PanierResourceIntTest {

    @Inject
    private PanierRepository panierRepository;

    @Inject
    private PanierService panierService;

    @Inject
    private PanierSearchRepository panierSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPanierMockMvc;

    private Panier panier;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PanierResource panierResource = new PanierResource();
        ReflectionTestUtils.setField(panierResource, "panierService", panierService);
        this.restPanierMockMvc = MockMvcBuilders.standaloneSetup(panierResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Panier createEntity(EntityManager em) {
        Panier panier = new Panier();
        // Add required entity
        Produit produit = ProduitResourceIntTest.createEntity(em);
        em.persist(produit);
        em.flush();
        panier.getProduits().add(produit);
        return panier;
    }

    @Before
    public void initTest() {
        panierSearchRepository.deleteAll();
        panier = createEntity(em);
    }

    @Test
    @Transactional
    public void createPanier() throws Exception {
        int databaseSizeBeforeCreate = panierRepository.findAll().size();

        // Create the Panier

        restPanierMockMvc.perform(post("/api/paniers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(panier)))
                .andExpect(status().isCreated());

        // Validate the Panier in the database
        List<Panier> paniers = panierRepository.findAll();
        assertThat(paniers).hasSize(databaseSizeBeforeCreate + 1);
        Panier testPanier = paniers.get(paniers.size() - 1);

        // Validate the Panier in ElasticSearch
        Panier panierEs = panierSearchRepository.findOne(testPanier.getId());
        assertThat(panierEs).isEqualToComparingFieldByField(testPanier);
    }

    @Test
    @Transactional
    public void getAllPaniers() throws Exception {
        // Initialize the database
        panierRepository.saveAndFlush(panier);

        // Get all the paniers
        restPanierMockMvc.perform(get("/api/paniers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(panier.getId().intValue())));
    }

    @Test
    @Transactional
    public void getPanier() throws Exception {
        // Initialize the database
        panierRepository.saveAndFlush(panier);

        // Get the panier
        restPanierMockMvc.perform(get("/api/paniers/{id}", panier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(panier.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPanier() throws Exception {
        // Get the panier
        restPanierMockMvc.perform(get("/api/paniers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePanier() throws Exception {
        // Initialize the database
        panierService.save(panier);

        int databaseSizeBeforeUpdate = panierRepository.findAll().size();

        // Update the panier
        Panier updatedPanier = panierRepository.findOne(panier.getId());

        restPanierMockMvc.perform(put("/api/paniers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPanier)))
                .andExpect(status().isOk());

        // Validate the Panier in the database
        List<Panier> paniers = panierRepository.findAll();
        assertThat(paniers).hasSize(databaseSizeBeforeUpdate);
        Panier testPanier = paniers.get(paniers.size() - 1);

        // Validate the Panier in ElasticSearch
        Panier panierEs = panierSearchRepository.findOne(testPanier.getId());
        assertThat(panierEs).isEqualToComparingFieldByField(testPanier);
    }

    @Test
    @Transactional
    public void deletePanier() throws Exception {
        // Initialize the database
        panierService.save(panier);

        int databaseSizeBeforeDelete = panierRepository.findAll().size();

        // Get the panier
        restPanierMockMvc.perform(delete("/api/paniers/{id}", panier.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean panierExistsInEs = panierSearchRepository.exists(panier.getId());
        assertThat(panierExistsInEs).isFalse();

        // Validate the database is empty
        List<Panier> paniers = panierRepository.findAll();
        assertThat(paniers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPanier() throws Exception {
        // Initialize the database
        panierService.save(panier);

        // Search the panier
        restPanierMockMvc.perform(get("/api/_search/paniers?query=id:" + panier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(panier.getId().intValue())));
    }
}
