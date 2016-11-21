package com.ippon.shop.service;

import com.ippon.shop.domain.Produit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Produit.
 */
public interface ProduitService {

    /**
     * Save a produit.
     *
     * @param produit the entity to save
     * @return the persisted entity
     */
    Produit save(Produit produit);

    /**
     *  Get all the produits.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Produit> findAll(Pageable pageable);

    /**
     *  Get the "id" produit.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Produit findOne(Long id);

    /**
     *  Delete the "id" produit.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the produit corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Produit> search(String query, Pageable pageable);
}
