package com.ippon.shop.service;

import com.ippon.shop.domain.Commentaire;
import com.ippon.shop.web.rest.vm.ManagedCommentVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Commentaire.
 */
public interface CommentaireService {

    /**
     * Save a commentaire.
     *
     * @param commentaire the entity to save
     * @return the persisted entity
     */
    Commentaire save(Commentaire commentaire);

    Commentaire createComment(ManagedCommentVM managedCommentVM);

    /**
     *  Get all the commentaires.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Commentaire> findAll(Pageable pageable);

    /**
     *  Get all the commentaires for product.
     *
     *
     *  @return the list of entities
     */
    Page<Commentaire> findAllByProduct(Long id);

    /**
     *  Get the "id" commentaire.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Commentaire findOne(Long id);

    /**
     *  Delete the "id" commentaire.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the commentaire corresponding to the query.
     *
     *  @param query the query of the search
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Commentaire> search(String query, Pageable pageable);
}
