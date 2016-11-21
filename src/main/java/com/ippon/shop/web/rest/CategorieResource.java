package com.ippon.shop.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ippon.shop.domain.Categorie;
import com.ippon.shop.security.AuthoritiesConstants;
import com.ippon.shop.service.CategorieService;
import com.ippon.shop.web.rest.util.HeaderUtil;
import com.ippon.shop.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
 * REST controller for managing Categorie.
 */
@RestController
@RequestMapping("/api")
public class CategorieResource {

    private final Logger log = LoggerFactory.getLogger(CategorieResource.class);

    @Inject
    private CategorieService categorieService;

    /**
     * POST  /categories : Create a new categorie.
     *
     * @param categorie the categorie to create
     * @return the ResponseEntity with status 201 (Created) and with body the new categorie, or with status 400 (Bad Request) if the categorie has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/categories")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Categorie> createCategorie(@Valid @RequestBody Categorie categorie) throws URISyntaxException {
        log.debug("REST request to save Categorie : {}", categorie);
        if (categorie.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("categorie", "idexists", "A new categorie cannot already have an ID")).body(null);
        }
        Categorie result = categorieService.save(categorie);
        return ResponseEntity.created(new URI("/api/categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("categorie", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /categories : Updates an existing categorie.
     *
     * @param categorie the categorie to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated categorie,
     * or with status 400 (Bad Request) if the categorie is not valid,
     * or with status 500 (Internal Server Error) if the categorie couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/categories")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Categorie> updateCategorie(@Valid @RequestBody Categorie categorie) throws URISyntaxException {
        log.debug("REST request to update Categorie : {}", categorie);
        if (categorie.getId() == null) {
            return createCategorie(categorie);
        }
        Categorie result = categorieService.save(categorie);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("categorie", categorie.getId().toString()))
            .body(result);
    }

    /**
     * GET  /categories : get all the categories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of categories in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/categories")
    @Timed
    public ResponseEntity<List<Categorie>> getAllCategories(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Categories");
        Page<Categorie> page = categorieService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /categories/:id : get the "id" categorie.
     *
     * @param id the id of the categorie to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the categorie, or with status 404 (Not Found)
     */
    @GetMapping("/categories/{id}")
    @Timed
    public ResponseEntity<Categorie> getCategorie(@PathVariable Long id) {
        log.debug("REST request to get Categorie : {}", id);
        Categorie categorie = categorieService.findOne(id);
        return Optional.ofNullable(categorie)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /categories/:id : delete the "id" categorie.
     *
     * @param id the id of the categorie to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/categories/{id}")
    @Timed
    public ResponseEntity<Void> deleteCategorie(@PathVariable Long id) {
        log.debug("REST request to delete Categorie : {}", id);
        categorieService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("categorie", id.toString())).build();
    }

    /**
     * SEARCH  /_search/categories?query=:query : search for the categorie corresponding
     * to the query.
     *
     * @param query the query of the categorie search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/categories")
    @Timed
    public ResponseEntity<List<Categorie>> searchCategories(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Categories for query {}", query);
        Page<Categorie> page = categorieService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
