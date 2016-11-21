package com.ippon.shop.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ippon.shop.domain.Panier;
import com.ippon.shop.service.PanierService;
import com.ippon.shop.web.rest.util.HeaderUtil;
import com.ippon.shop.web.rest.util.PaginationUtil;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Panier.
 */
@RestController
@RequestMapping("/api")
public class PanierResource {

    private final Logger log = LoggerFactory.getLogger(PanierResource.class);
        
    @Inject
    private PanierService panierService;

    /**
     * POST  /paniers : Create a new panier.
     *
     * @param panier the panier to create
     * @return the ResponseEntity with status 201 (Created) and with body the new panier, or with status 400 (Bad Request) if the panier has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/paniers")
    @Timed
    public ResponseEntity<Panier> createPanier(@Valid @RequestBody Panier panier) throws URISyntaxException {
        log.debug("REST request to save Panier : {}", panier);
        if (panier.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("panier", "idexists", "A new panier cannot already have an ID")).body(null);
        }
        Panier result = panierService.save(panier);
        return ResponseEntity.created(new URI("/api/paniers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("panier", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /paniers : Updates an existing panier.
     *
     * @param panier the panier to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated panier,
     * or with status 400 (Bad Request) if the panier is not valid,
     * or with status 500 (Internal Server Error) if the panier couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/paniers")
    @Timed
    public ResponseEntity<Panier> updatePanier(@Valid @RequestBody Panier panier) throws URISyntaxException {
        log.debug("REST request to update Panier : {}", panier);
        if (panier.getId() == null) {
            return createPanier(panier);
        }
        Panier result = panierService.save(panier);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("panier", panier.getId().toString()))
            .body(result);
    }

    /**
     * GET  /paniers : get all the paniers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of paniers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/paniers")
    @Timed
    public ResponseEntity<List<Panier>> getAllPaniers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Paniers");
        Page<Panier> page = panierService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/paniers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /paniers/:id : get the "id" panier.
     *
     * @param id the id of the panier to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the panier, or with status 404 (Not Found)
     */
    @GetMapping("/paniers/{id}")
    @Timed
    public ResponseEntity<Panier> getPanier(@PathVariable Long id) {
        log.debug("REST request to get Panier : {}", id);
        Panier panier = panierService.findOne(id);
        return Optional.ofNullable(panier)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /paniers/:id : delete the "id" panier.
     *
     * @param id the id of the panier to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/paniers/{id}")
    @Timed
    public ResponseEntity<Void> deletePanier(@PathVariable Long id) {
        log.debug("REST request to delete Panier : {}", id);
        panierService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("panier", id.toString())).build();
    }

    /**
     * SEARCH  /_search/paniers?query=:query : search for the panier corresponding
     * to the query.
     *
     * @param query the query of the panier search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/paniers")
    @Timed
    public ResponseEntity<List<Panier>> searchPaniers(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Paniers for query {}", query);
        Page<Panier> page = panierService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/paniers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
