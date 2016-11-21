package com.ippon.shop.service.impl;

import com.ippon.shop.domain.Produit;
import com.ippon.shop.domain.User;
import com.ippon.shop.repository.UserRepository;
import com.ippon.shop.security.SecurityUtils;
import com.ippon.shop.service.CommentaireService;
import com.ippon.shop.domain.Commentaire;
import com.ippon.shop.repository.CommentaireRepository;
import com.ippon.shop.repository.search.CommentaireSearchRepository;
import com.ippon.shop.service.ProduitService;
import com.ippon.shop.web.rest.vm.ManagedCommentVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Commentaire.
 */
@Service
@Transactional
public class CommentaireServiceImpl implements CommentaireService{

    private final Logger log = LoggerFactory.getLogger(CommentaireServiceImpl.class);

    @Inject
    private CommentaireRepository commentaireRepository;

    @Inject
    private CommentaireSearchRepository commentaireSearchRepository;

    @Inject
    private  ProduitService produitService;

    @Inject
    private UserRepository userRepository;

    /**
     * Save a commentaire.
     *
     * @param commentaire the entity to save
     * @return the persisted entity
     */
    public Commentaire save(Commentaire commentaire) {
        log.debug("Request to save Commentaire : {}", commentaire);
        if(!commentaire.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin()))
        {
            throw new AccessDeniedException("Vous ne pouvez pas ajouter le commentaire");
        }
        Commentaire result = commentaireRepository.save(commentaire);
        commentaireSearchRepository.save(result);
        return result;
    }

    @Override
    public Commentaire createComment(ManagedCommentVM managedCommentVM) {
        Commentaire commentaire = new Commentaire();
        Produit produit=produitService.findOne(managedCommentVM.getId());
        Optional<User> userOptional=userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        commentaire.setNote(managedCommentVM.getNote());
        commentaire.setAvis(managedCommentVM.getAvis());
        commentaire.setProduit(produit);
        commentaire.setUser(userOptional.get());

        commentaireRepository.save(commentaire);

        return commentaire;
    }

    /**
     *  Get all the commentaires.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Commentaire> findAll(Pageable pageable) {
        log.debug("Request to get all Commentaires");
        Page<Commentaire> result = commentaireRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get all the commentaires for one product.
     *
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Commentaire> findAllByProduct(Long id) {
        log.debug("Request to get all Commentaires for product");
        List<Commentaire> commentaires = commentaireRepository.findByProductIsCurrentProduct(id);
        final Page<Commentaire> result = new PageImpl<>(commentaires);
        return result;
    }

    /**
     *  Get one commentaire by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Commentaire findOne(Long id) {
        log.debug("Request to get Commentaire : {}", id);
        Commentaire commentaire = commentaireRepository.findOne(id);
        return commentaire;
    }

    /**
     *  Delete the  commentaire by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Commentaire : {}", id);
        commentaireRepository.delete(id);
        commentaireSearchRepository.delete(id);
    }

    /**
     * Search for the commentaire corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Commentaire> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Commentaires for query {}", query);
        Page<Commentaire> result = commentaireSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
