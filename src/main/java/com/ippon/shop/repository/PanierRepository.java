package com.ippon.shop.repository;

import com.ippon.shop.domain.Panier;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Panier entity.
 */
@SuppressWarnings("unused")
public interface PanierRepository extends JpaRepository<Panier,Long> {

    @Query("select distinct panier from Panier panier left join fetch panier.produits")
    List<Panier> findAllWithEagerRelationships();

    @Query("select panier from Panier panier left join fetch panier.produits where panier.id =:id")
    Panier findOneWithEagerRelationships(@Param("id") Long id);

}
