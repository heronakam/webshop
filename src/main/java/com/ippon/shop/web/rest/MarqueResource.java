package com.ippon.shop.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ippon.shop.domain.Marque;
import com.ippon.shop.service.MarqueService;
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
 * REST controller for managing Marque.
 */
@RestController
@RequestMapping("/api")
public class MarqueResource {

    private final Logger log = LoggerFactory.getLogger(MarqueResource.class);
        
    @Inject
    private MarqueService marqueService;

    /**
     * POST  /marques : Create a new marque.
     *
     * @param marque the marque to create
     * @return the ResponseEntity with status 201 (Created) and with body the new marque, or with status 400 (Bad Request) if the marque has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/marques")
    @Timed
    public ResponseEntity<Marque> createMarque(@Valid @RequestBody Marque marque) throws URISyntaxException {
        log.debug("REST request to save Marque : {}", marque);
        if (marque.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("marque", "idexists", "A new marque cannot already have an ID")).body(null);
        }
        Marque result = marqueService.save(marque);
        return ResponseEntity.created(new URI("/api/marques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("marque", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /marques : Updates an existing marque.
     *
     * @param marque the marque to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated marque,
     * or with status 400 (Bad Request) if the marque is not valid,
     * or with status 500 (Internal Server Error) if the marque couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/marques")
    @Timed
    public ResponseEntity<Marque> updateMarque(@Valid @RequestBody Marque marque) throws URISyntaxException {
        log.debug("REST request to update Marque : {}", marque);
        if (marque.getId() == null) {
            return createMarque(marque);
        }
        Marque result = marqueService.save(marque);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("marque", marque.getId().toString()))
            .body(result);
    }

    /**
     * GET  /marques : get all the marques.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of marques in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/marques")
    @Timed
    public ResponseEntity<List<Marque>> getAllMarques(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Marques");
        Page<Marque> page = marqueService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/marques");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /marques/:id : get the "id" marque.
     *
     * @param id the id of the marque to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the marque, or with status 404 (Not Found)
     */
    @GetMapping("/marques/{id}")
    @Timed
    public ResponseEntity<Marque> getMarque(@PathVariable Long id) {
        log.debug("REST request to get Marque : {}", id);
        Marque marque = marqueService.findOne(id);
        return Optional.ofNullable(marque)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /marques/:id : delete the "id" marque.
     *
     * @param id the id of the marque to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/marques/{id}")
    @Timed
    public ResponseEntity<Void> deleteMarque(@PathVariable Long id) {
        log.debug("REST request to delete Marque : {}", id);
        marqueService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("marque", id.toString())).build();
    }

    /**
     * SEARCH  /_search/marques?query=:query : search for the marque corresponding
     * to the query.
     *
     * @param query the query of the marque search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/marques")
    @Timed
    public ResponseEntity<List<Marque>> searchMarques(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Marques for query {}", query);
        Page<Marque> page = marqueService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/marques");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
