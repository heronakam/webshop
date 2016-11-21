package com.ippon.shop.service;

import com.ippon.shop.domain.Caracteristique;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Caracteristique.
 */
public interface CaracteristiqueService {

    /**
     * Save a caracteristique.
     *
     * @param caracteristique the entity to save
     * @return the persisted entity
     */
    Caracteristique save(Caracteristique caracteristique);

    /**
     *  Get all the caracteristiques.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Caracteristique> findAll(Pageable pageable);

    /**
     *  Get the "id" caracteristique.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Caracteristique findOne(Long id);

    /**
     *  Delete the "id" caracteristique.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the caracteristique corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Caracteristique> search(String query, Pageable pageable);
}
