package com.ippon.shop.repository;

import com.ippon.shop.domain.Produit;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Produit entity.
 */
@SuppressWarnings("unused")
public interface ProduitRepository extends JpaRepository<Produit,Long> {

    @Query("select distinct produit from Produit produit left join fetch produit.caracteristiques left join fetch produit.couleurs")
    List<Produit> findAllWithEagerRelationships();

    @Query("select produit from Produit produit left join fetch produit.caracteristiques left join fetch produit.couleurs where produit.id =:id")
    Produit findOneWithEagerRelationships(@Param("id") Long id);

}
