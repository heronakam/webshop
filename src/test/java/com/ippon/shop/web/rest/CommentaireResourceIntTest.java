package com.ippon.shop.web.rest;

import com.ippon.shop.ShopApp;

import com.ippon.shop.domain.Commentaire;
import com.ippon.shop.domain.User;
import com.ippon.shop.domain.Produit;
import com.ippon.shop.repository.CommentaireRepository;
import com.ippon.shop.service.CommentaireService;
import com.ippon.shop.repository.search.CommentaireSearchRepository;

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
 * Test class for the CommentaireResource REST controller.
 *
 * @see CommentaireResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShopApp.class)
public class CommentaireResourceIntTest {

    private static final Integer DEFAULT_NOTE = 5;
    private static final Integer UPDATED_NOTE = 4;

    private static final String DEFAULT_AVIS = "AAAAAAAAAA";
    private static final String UPDATED_AVIS = "BBBBBBBBBB";

    @Inject
    private CommentaireRepository commentaireRepository;

    @Inject
    private CommentaireService commentaireService;

    @Inject
    private CommentaireSearchRepository commentaireSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCommentaireMockMvc;

    private Commentaire commentaire;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CommentaireResource commentaireResource = new CommentaireResource();
        ReflectionTestUtils.setField(commentaireResource, "commentaireService", commentaireService);
        this.restCommentaireMockMvc = MockMvcBuilders.standaloneSetup(commentaireResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commentaire createEntity(EntityManager em) {
        Commentaire commentaire = new Commentaire()
                .note(DEFAULT_NOTE)
                .avis(DEFAULT_AVIS);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        commentaire.setUser(user);
        // Add required entity
        Produit produit = ProduitResourceIntTest.createEntity(em);
        em.persist(produit);
        em.flush();
        commentaire.setProduit(produit);
        return commentaire;
    }

    @Before
    public void initTest() {
        commentaireSearchRepository.deleteAll();
        commentaire = createEntity(em);
    }

    @Test
    @Transactional
    public void createCommentaire() throws Exception {
        int databaseSizeBeforeCreate = commentaireRepository.findAll().size();

        // Create the Commentaire

        restCommentaireMockMvc.perform(post("/api/commentaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(commentaire)))
                .andExpect(status().isCreated());

        // Validate the Commentaire in the database
        List<Commentaire> commentaires = commentaireRepository.findAll();
        assertThat(commentaires).hasSize(databaseSizeBeforeCreate + 1);
        Commentaire testCommentaire = commentaires.get(commentaires.size() - 1);
        assertThat(testCommentaire.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testCommentaire.getAvis()).isEqualTo(DEFAULT_AVIS);

        // Validate the Commentaire in ElasticSearch
        Commentaire commentaireEs = commentaireSearchRepository.findOne(testCommentaire.getId());
        assertThat(commentaireEs).isEqualToComparingFieldByField(testCommentaire);
    }

    @Test
    @Transactional
    public void checkNoteIsRequired() throws Exception {
        int databaseSizeBeforeTest = commentaireRepository.findAll().size();
        // set the field null
        commentaire.setNote(null);

        // Create the Commentaire, which fails.

        restCommentaireMockMvc.perform(post("/api/commentaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(commentaire)))
                .andExpect(status().isBadRequest());

        List<Commentaire> commentaires = commentaireRepository.findAll();
        assertThat(commentaires).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCommentaires() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaires
        restCommentaireMockMvc.perform(get("/api/commentaires?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(commentaire.getId().intValue())))
                .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
                .andExpect(jsonPath("$.[*].avis").value(hasItem(DEFAULT_AVIS.toString())));
    }

    @Test
    @Transactional
    public void getCommentaire() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get the commentaire
        restCommentaireMockMvc.perform(get("/api/commentaires/{id}", commentaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(commentaire.getId().intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.avis").value(DEFAULT_AVIS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCommentaire() throws Exception {
        // Get the commentaire
        restCommentaireMockMvc.perform(get("/api/commentaires/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommentaire() throws Exception {
        // Initialize the database
        commentaireService.save(commentaire);

        int databaseSizeBeforeUpdate = commentaireRepository.findAll().size();

        // Update the commentaire
        Commentaire updatedCommentaire = commentaireRepository.findOne(commentaire.getId());
        updatedCommentaire
                .note(UPDATED_NOTE)
                .avis(UPDATED_AVIS);

        restCommentaireMockMvc.perform(put("/api/commentaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCommentaire)))
                .andExpect(status().isOk());

        // Validate the Commentaire in the database
        List<Commentaire> commentaires = commentaireRepository.findAll();
        assertThat(commentaires).hasSize(databaseSizeBeforeUpdate);
        Commentaire testCommentaire = commentaires.get(commentaires.size() - 1);
        assertThat(testCommentaire.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testCommentaire.getAvis()).isEqualTo(UPDATED_AVIS);

        // Validate the Commentaire in ElasticSearch
        Commentaire commentaireEs = commentaireSearchRepository.findOne(testCommentaire.getId());
        assertThat(commentaireEs).isEqualToComparingFieldByField(testCommentaire);
    }

    @Test
    @Transactional
    public void deleteCommentaire() throws Exception {
        // Initialize the database
        commentaireService.save(commentaire);

        int databaseSizeBeforeDelete = commentaireRepository.findAll().size();

        // Get the commentaire
        restCommentaireMockMvc.perform(delete("/api/commentaires/{id}", commentaire.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean commentaireExistsInEs = commentaireSearchRepository.exists(commentaire.getId());
        assertThat(commentaireExistsInEs).isFalse();

        // Validate the database is empty
        List<Commentaire> commentaires = commentaireRepository.findAll();
        assertThat(commentaires).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCommentaire() throws Exception {
        // Initialize the database
        commentaireService.save(commentaire);

        // Search the commentaire
        restCommentaireMockMvc.perform(get("/api/_search/commentaires?query=id:" + commentaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commentaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].avis").value(hasItem(DEFAULT_AVIS.toString())));
    }
}
