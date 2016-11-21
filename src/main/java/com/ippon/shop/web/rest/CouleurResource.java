package com.ippon.shop.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ippon.shop.domain.Couleur;
import com.ippon.shop.service.CouleurService;
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
 * REST controller for managing Couleur.
 */
@RestController
@RequestMapping("/api")
public class CouleurResource {

    private final Logger log = LoggerFactory.getLogger(CouleurResource.class);
        
    @Inject
    private CouleurService couleurService;

    /**
     * POST  /couleurs : Create a new couleur.
     *
     * @param couleur the couleur to create
     * @return the ResponseEntity with status 201 (Created) and with body the new couleur, or with status 400 (Bad Request) if the couleur has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/couleurs")
    @Timed
    public ResponseEntity<Couleur> createCouleur(@Valid @RequestBody Couleur couleur) throws URISyntaxException {
        log.debug("REST request to save Couleur : {}", couleur);
        if (couleur.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("couleur", "idexists", "A new couleur cannot already have an ID")).body(null);
        }
        Couleur result = couleurService.save(couleur);
        return ResponseEntity.created(new URI("/api/couleurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("couleur", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /couleurs : Updates an existing couleur.
     *
     * @param couleur the couleur to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated couleur,
     * or with status 400 (Bad Request) if the couleur is not valid,
     * or with status 500 (Internal Server Error) if the couleur couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/couleurs")
    @Timed
    public ResponseEntity<Couleur> updateCouleur(@Valid @RequestBody Couleur couleur) throws URISyntaxException {
        log.debug("REST request to update Couleur : {}", couleur);
        if (couleur.getId() == null) {
            return createCouleur(couleur);
        }
        Couleur result = couleurService.save(couleur);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("couleur", couleur.getId().toString()))
            .body(result);
    }

    /**
     * GET  /couleurs : get all the couleurs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of couleurs in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/couleurs")
    @Timed
    public ResponseEntity<List<Couleur>> getAllCouleurs(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Couleurs");
        Page<Couleur> page = couleurService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/couleurs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /couleurs/:id : get the "id" couleur.
     *
     * @param id the id of the couleur to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the couleur, or with status 404 (Not Found)
     */
    @GetMapping("/couleurs/{id}")
    @Timed
    public ResponseEntity<Couleur> getCouleur(@PathVariable Long id) {
        log.debug("REST request to get Couleur : {}", id);
        Couleur couleur = couleurService.findOne(id);
        return Optional.ofNullable(couleur)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /couleurs/:id : delete the "id" couleur.
     *
     * @param id the id of the couleur to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/couleurs/{id}")
    @Timed
    public ResponseEntity<Void> deleteCouleur(@PathVariable Long id) {
        log.debug("REST request to delete Couleur : {}", id);
        couleurService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("couleur", id.toString())).build();
    }

    /**
     * SEARCH  /_search/couleurs?query=:query : search for the couleur corresponding
     * to the query.
     *
     * @param query the query of the couleur search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/couleurs")
    @Timed
    public ResponseEntity<List<Couleur>> searchCouleurs(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Couleurs for query {}", query);
        Page<Couleur> page = couleurService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/couleurs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
