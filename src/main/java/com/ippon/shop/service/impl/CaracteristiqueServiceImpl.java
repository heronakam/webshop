package com.ippon.shop.service.impl;

import com.ippon.shop.service.CaracteristiqueService;
import com.ippon.shop.domain.Caracteristique;
import com.ippon.shop.repository.CaracteristiqueRepository;
import com.ippon.shop.repository.search.CaracteristiqueSearchRepository;
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
 * Service Implementation for managing Caracteristique.
 */
@Service
@Transactional
public class CaracteristiqueServiceImpl implements CaracteristiqueService{

    private final Logger log = LoggerFactory.getLogger(CaracteristiqueServiceImpl.class);
    
    @Inject
    private CaracteristiqueRepository caracteristiqueRepository;

    @Inject
    private CaracteristiqueSearchRepository caracteristiqueSearchRepository;

    /**
     * Save a caracteristique.
     *
     * @param caracteristique the entity to save
     * @return the persisted entity
     */
    public Caracteristique save(Caracteristique caracteristique) {
        log.debug("Request to save Caracteristique : {}", caracteristique);
        Caracteristique result = caracteristiqueRepository.save(caracteristique);
        caracteristiqueSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the caracteristiques.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Caracteristique> findAll(Pageable pageable) {
        log.debug("Request to get all Caracteristiques");
        Page<Caracteristique> result = caracteristiqueRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one caracteristique by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Caracteristique findOne(Long id) {
        log.debug("Request to get Caracteristique : {}", id);
        Caracteristique caracteristique = caracteristiqueRepository.findOne(id);
        return caracteristique;
    }

    /**
     *  Delete the  caracteristique by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Caracteristique : {}", id);
        caracteristiqueRepository.delete(id);
        caracteristiqueSearchRepository.delete(id);
    }

    /**
     * Search for the caracteristique corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Caracteristique> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Caracteristiques for query {}", query);
        Page<Caracteristique> result = caracteristiqueSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
