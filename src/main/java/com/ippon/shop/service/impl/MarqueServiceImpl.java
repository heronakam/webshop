package com.ippon.shop.service.impl;

import com.ippon.shop.service.MarqueService;
import com.ippon.shop.domain.Marque;
import com.ippon.shop.repository.MarqueRepository;
import com.ippon.shop.repository.search.MarqueSearchRepository;
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
 * Service Implementation for managing Marque.
 */
@Service
//@Transactional
public class MarqueServiceImpl implements MarqueService{

    private final Logger log = LoggerFactory.getLogger(MarqueServiceImpl.class);

    @Inject
    private MarqueRepository marqueRepository;

    @Inject
    private MarqueSearchRepository marqueSearchRepository;

    /**
     * Save a marque.
     *
     * @param marque the entity to save
     * @return the persisted entity
     */
    public Marque save(Marque marque) {
        log.debug("Request to save Marque : {}", marque);
        Marque result = marqueRepository.save(marque);
        marqueSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the marques.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    //@Transactional(readOnly = true)
    public Page<Marque> findAll(Pageable pageable) {
        log.debug("Request to get all Marques");
        Page<Marque> result = marqueRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one marque by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Marque findOne(Long id) {
        log.debug("Request to get Marque : {}", id);
        Marque marque = marqueRepository.findOne(id);
        return marque;
    }

    /**
     *  Delete the  marque by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Marque : {}", id);
        marqueRepository.delete(id);
        marqueSearchRepository.delete(id);
    }

    /**
     * Search for the marque corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Marque> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Marques for query {}", query);
        Page<Marque> result = marqueSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
