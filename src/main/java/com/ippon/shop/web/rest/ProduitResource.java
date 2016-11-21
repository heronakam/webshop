package com.ippon.shop.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ippon.shop.domain.Commentaire;
import com.ippon.shop.domain.Produit;
import com.ippon.shop.repository.CommentaireRepository;
import com.ippon.shop.service.ProduitService;
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
 * REST controller for managing Produit.
 */
@RestController
@RequestMapping("/api")
public class ProduitResource {

    private final Logger log = LoggerFactory.getLogger(ProduitResource.class);

    @Inject
    private ProduitService produitService;

    @Inject
    private CommentaireRepository   commentaireRepository;

    /**
     * POST  /produits : Create a new produit.
     *
     * @param produit the produit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new produit, or with status 400 (Bad Request) if the produit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/produits")
    @Timed
    public ResponseEntity<Produit> createProduit(@Valid @RequestBody Produit produit) throws URISyntaxException {
        log.debug("REST request to save Produit : {}", produit);
        if (produit.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("produit", "idexists", "A new produit cannot already have an ID")).body(null);
        }
        Produit result = produitService.save(produit);
        return ResponseEntity.created(new URI("/api/produits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("produit", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /produits : Updates an existing produit.
     *
     * @param produit the produit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated produit,
     * or with status 400 (Bad Request) if the produit is not valid,
     * or with status 500 (Internal Server Error) if the produit couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/produits")
    @Timed
    public ResponseEntity<Produit> updateProduit(@Valid @RequestBody Produit produit) throws URISyntaxException {
        log.debug("REST request to update Produit : {}", produit);
        if (produit.getId() == null) {
            return createProduit(produit);
        }
        Produit result = produitService.save(produit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("produit", produit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /produits : get all the produits.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of produits in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/produits")
    @Timed
    public ResponseEntity<List<Produit>> getAllProduits(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Produits");
        Page<Produit> page = produitService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/produits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /produits/:id : get the "id" produit.
     *
     * @param id the id of the produit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the produit, or with status 404 (Not Found)
     */
    @GetMapping("/produits/{id}")
    @Timed
    public ResponseEntity<Produit> getProduit(@PathVariable Long id) {
        log.debug("REST request to get Produit : {}", id);
        Produit produit = produitService.findOne(id);
        List<Commentaire>commentaires=commentaireRepository.findByProductIsCurrentProduct(id);
        for (int i=0;i<commentaires.size();i++) {
            Commentaire Alternatif=new Commentaire();
            Alternatif.setNote(commentaires.get(i).getNote());
            Alternatif.setAvis(commentaires.get(i).getAvis());
            Alternatif.setUser(commentaires.get(i).getUser());
            produit.addCommentaire(Alternatif);
        }
        return Optional.ofNullable(produit)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /produits/:id : delete the "id" produit.
     *
     * @param id the id of the produit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/produits/{id}")
    @Timed
    public ResponseEntity<Void> deleteProduit(@PathVariable Long id) {
        log.debug("REST request to delete Produit : {}", id);
        produitService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("produit", id.toString())).build();
    }

    /**
     * SEARCH  /_search/produits?query=:query : search for the produit corresponding
     * to the query.
     *
     * @param query the query of the produit search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/produits")
    @Timed
    public ResponseEntity<List<Produit>> searchProduits(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Produits for query {}", query);
        Page<Produit> page = produitService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/produits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
