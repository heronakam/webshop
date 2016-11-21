package com.ippon.shop.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ippon.shop.domain.Caracteristique;
import com.ippon.shop.security.AuthoritiesConstants;
import com.ippon.shop.service.CaracteristiqueService;
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
 * REST controller for managing Caracteristique.
 */
@RestController
@RequestMapping("/api")
public class CaracteristiqueResource {

    private final Logger log = LoggerFactory.getLogger(CaracteristiqueResource.class);

    @Inject
    private CaracteristiqueService caracteristiqueService;

    /**
     * POST  /caracteristiques : Create a new caracteristique.
     *
     * @param caracteristique the caracteristique to create
     * @return the ResponseEntity with status 201 (Created) and with body the new caracteristique, or with status 400 (Bad Request) if the caracteristique has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/caracteristiques")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Caracteristique> createCaracteristique(@Valid @RequestBody Caracteristique caracteristique) throws URISyntaxException {
        log.debug("REST request to save Caracteristique : {}", caracteristique);
        if (caracteristique.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("caracteristique", "idexists", "A new caracteristique cannot already have an ID")).body(null);
        }
        Caracteristique result = caracteristiqueService.save(caracteristique);
        return ResponseEntity.created(new URI("/api/caracteristiques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("caracteristique", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /caracteristiques : Updates an existing caracteristique.
     *
     * @param caracteristique the caracteristique to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated caracteristique,
     * or with status 400 (Bad Request) if the caracteristique is not valid,
     * or with status 500 (Internal Server Error) if the caracteristique couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/caracteristiques")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Caracteristique> updateCaracteristique(@Valid @RequestBody Caracteristique caracteristique) throws URISyntaxException {
        log.debug("REST request to update Caracteristique : {}", caracteristique);
        if (caracteristique.getId() == null) {
            return createCaracteristique(caracteristique);
        }
        Caracteristique result = caracteristiqueService.save(caracteristique);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("caracteristique", caracteristique.getId().toString()))
            .body(result);
    }

    /**
     * GET  /caracteristiques : get all the caracteristiques.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of caracteristiques in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/caracteristiques")
    @Timed
    public ResponseEntity<List<Caracteristique>> getAllCaracteristiques(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Caracteristiques");
        Page<Caracteristique> page = caracteristiqueService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/caracteristiques");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /caracteristiques/:id : get the "id" caracteristique.
     *
     * @param id the id of the caracteristique to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the caracteristique, or with status 404 (Not Found)
     */
    @GetMapping("/caracteristiques/{id}")
    @Timed
    public ResponseEntity<Caracteristique> getCaracteristique(@PathVariable Long id) {
        log.debug("REST request to get Caracteristique : {}", id);
        Caracteristique caracteristique = caracteristiqueService.findOne(id);
        return Optional.ofNullable(caracteristique)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /caracteristiques/:id : delete the "id" caracteristique.
     *
     * @param id the id of the caracteristique to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/caracteristiques/{id}")
    @Timed
    public ResponseEntity<Void> deleteCaracteristique(@PathVariable Long id) {
        log.debug("REST request to delete Caracteristique : {}", id);
        caracteristiqueService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("caracteristique", id.toString())).build();
    }

    /**
     * SEARCH  /_search/caracteristiques?query=:query : search for the caracteristique corresponding
     * to the query.
     *
     * @param query the query of the caracteristique search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/caracteristiques")
    @Timed
    public ResponseEntity<List<Caracteristique>> searchCaracteristiques(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Caracteristiques for query {}", query);
        Page<Caracteristique> page = caracteristiqueService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/caracteristiques");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
