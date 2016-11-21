package com.ippon.shop.service;

import com.ippon.shop.domain.Panier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Panier.
 */
public interface PanierService {

    /**
     * Save a panier.
     *
     * @param panier the entity to save
     * @return the persisted entity
     */
    Panier save(Panier panier);

    /**
     *  Get all the paniers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Panier> findAll(Pageable pageable);

    /**
     *  Get the "id" panier.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Panier findOne(Long id);

    /**
     *  Delete the "id" panier.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the panier corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Panier> search(String query, Pageable pageable);
}
