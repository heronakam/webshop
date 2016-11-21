package com.ippon.shop.service.impl;

import com.ippon.shop.service.PanierService;
import com.ippon.shop.domain.Panier;
import com.ippon.shop.repository.PanierRepository;
import com.ippon.shop.repository.search.PanierSearchRepository;
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
 * Service Implementation for managing Panier.
 */
@Service
@Transactional
public class PanierServiceImpl implements PanierService{

    private final Logger log = LoggerFactory.getLogger(PanierServiceImpl.class);
    
    @Inject
    private PanierRepository panierRepository;

    @Inject
    private PanierSearchRepository panierSearchRepository;

    /**
     * Save a panier.
     *
     * @param panier the entity to save
     * @return the persisted entity
     */
    public Panier save(Panier panier) {
        log.debug("Request to save Panier : {}", panier);
        Panier result = panierRepository.save(panier);
        panierSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the paniers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Panier> findAll(Pageable pageable) {
        log.debug("Request to get all Paniers");
        Page<Panier> result = panierRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one panier by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Panier findOne(Long id) {
        log.debug("Request to get Panier : {}", id);
        Panier panier = panierRepository.findOneWithEagerRelationships(id);
        return panier;
    }

    /**
     *  Delete the  panier by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Panier : {}", id);
        panierRepository.delete(id);
        panierSearchRepository.delete(id);
    }

    /**
     * Search for the panier corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Panier> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Paniers for query {}", query);
        Page<Panier> result = panierSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
