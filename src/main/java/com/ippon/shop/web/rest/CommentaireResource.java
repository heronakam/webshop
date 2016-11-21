package com.ippon.shop.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ippon.shop.domain.Commentaire;
import com.ippon.shop.security.SecurityUtils;
import com.ippon.shop.service.CommentaireService;
import com.ippon.shop.service.UserService;
import com.ippon.shop.web.rest.util.HeaderUtil;
import com.ippon.shop.web.rest.util.PaginationUtil;
import com.ippon.shop.web.rest.vm.ManagedCommentVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Commentaire.
 */
@RestController
@RequestMapping("/api")
public class CommentaireResource {

    private final Logger log = LoggerFactory.getLogger(CommentaireResource.class);

    @Inject
    private CommentaireService commentaireService;

    @Inject
    private UserService userService;

    /**
     * POST  /commentaires : Create a new commentaire.
     *
     * @param commentaire the commentaire to create
     * @return the ResponseEntity with status 201 (Created) and with body the new commentaire, or with status 400 (Bad Request) if the commentaire has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/commentaires")
    @Timed
    public ResponseEntity<Commentaire> createCommentaire(@Valid @RequestBody Commentaire commentaire) throws URISyntaxException {
        log.debug("REST request to save Commentaire : {}", commentaire);
        if (commentaire.getId() != null || !SecurityUtils.isAuthenticated()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("commentaire", "idexists", "A new commentaire cannot already have an ID")).body(null);
        }
        Commentaire result = commentaireService.save(commentaire);
        return ResponseEntity.created(new URI("/api/commentaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("commentaire", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /commentaires : Updates an existing commentaire.
     *
     * @param commentaire the commentaire to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated commentaire,
     * or with status 400 (Bad Request) if the commentaire is not valid,
     * or with status 500 (Internal Server Error) if the commentaire couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
   @PutMapping("/commentaires")
    @Timed

    public ResponseEntity<Commentaire> updateCommentaire(@Valid @RequestBody Commentaire commentaire) throws URISyntaxException {
        log.debug("REST request to update Commentaire : {}", commentaire);
        if (commentaire.getId() == null) {
            return createCommentaire(commentaire);
        }
        Commentaire result = commentaireService.save(commentaire);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("commentaire", commentaire.getId().toString()))
            .body(result);
    }

    /**
     * GET  /commentaires : get all the commentaires .
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of commentaires in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
/*     */
    @GetMapping("/commentaires")
    @Timed
    public ResponseEntity<List<Commentaire>> getAllCommentaires(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Commentaires");
        Page<Commentaire> page = commentaireService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/commentaires");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /commentaires : get all the commentaires for product.
     *
     * @param id the Id of product information
     * @return the ResponseEntity with status 200 (OK) and the list of commentaires in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/produit/commentaires/{id}")
    @Timed
    public ResponseEntity<List<Commentaire>> getAllCommentairesForProduct(@PathVariable Long id)
        throws URISyntaxException {
        log.debug("REST request to get a page of Commentaires");
        Page<Commentaire> page = commentaireService.findAllByProduct(id);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/commentaires");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /commentaires/:id : get the "id" commentaire.
     *
     * @param id the id of the commentaire to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the commentaire, or with status 404 (Not Found)
     */
    @GetMapping("/commentaires/{id}")
    @Timed
    public ResponseEntity<Commentaire> getCommentaire(@PathVariable Long id) {
        log.debug("REST request to get Commentaire : {}", id);
        Commentaire commentaire = commentaireService.findOne(id);
        return Optional.ofNullable(commentaire)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /commentaires/:id : delete the "id" commentaire.
     *
     * @param id the id of the commentaire to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/commentaires/{id}")
    @Timed
    public ResponseEntity<Void> deleteCommentaire(@PathVariable Long id) {
        log.debug("REST request to delete Commentaire : {}", id);
        commentaireService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("commentaire", id.toString())).build();
    }

    /**
     * SEARCH  /_search/commentaires?query=:query : search for the commentaire corresponding
     * to the query.
     *
     * @param query the query of the commentaire search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/commentaires")
    @Timed
    public ResponseEntity<List<Commentaire>> searchCommentaires(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Commentaires for query {}", query);
        Page<Commentaire> page = commentaireService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/commentaires");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
