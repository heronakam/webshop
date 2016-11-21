package com.ippon.shop.service;

import com.ippon.shop.domain.Couleur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Couleur.
 */
public interface CouleurService {

    /**
     * Save a couleur.
     *
     * @param couleur the entity to save
     * @return the persisted entity
     */
    Couleur save(Couleur couleur);

    /**
     *  Get all the couleurs.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Couleur> findAll(Pageable pageable);

    /**
     *  Get the "id" couleur.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Couleur findOne(Long id);

    /**
     *  Delete the "id" couleur.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the couleur corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Couleur> search(String query, Pageable pageable);
}
