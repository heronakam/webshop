package com.ippon.shop.service.impl;

import com.ippon.shop.service.CouleurService;
import com.ippon.shop.domain.Couleur;
import com.ippon.shop.repository.CouleurRepository;
import com.ippon.shop.repository.search.CouleurSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Couleur.
 */
@Service
@Transactional
public class CouleurServiceImpl implements CouleurService{

    private final Logger log = LoggerFactory.getLogger(CouleurServiceImpl.class);
    
    @Inject
    private CouleurRepository couleurRepository;

    @Inject
    private CouleurSearchRepository couleurSearchRepository;

    /**
     * Save a couleur.
     *
     * @param couleur the entity to save
     * @return the persisted entity
     */
    public Couleur save(Couleur couleur) {
        log.debug("Request to save Couleur : {}", couleur);
        Couleur result = couleurRepository.save(couleur);
        couleurSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the couleurs.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Couleur> findAll(Pageable pageable) {
        log.debug("Request to get all Couleurs");
        Page<Couleur> result = couleurRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one couleur by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Couleur findOne(Long id) {
        log.debug("Request to get Couleur : {}", id);
        Couleur couleur = couleurRepository.findOne(id);
        return couleur;
    }

    /**
     *  Delete the  couleur by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Couleur : {}", id);
        couleurRepository.delete(id);
        couleurSearchRepository.delete(id);
    }

    /**
     * Search for the couleur corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Couleur> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Couleurs for query {}", query);
        Page<Couleur> result = couleurSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
