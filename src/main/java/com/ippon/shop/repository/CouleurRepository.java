package com.ippon.shop.repository;

import com.ippon.shop.domain.Couleur;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Couleur entity.
 */
@SuppressWarnings("unused")
public interface CouleurRepository extends JpaRepository<Couleur,Long> {

}
