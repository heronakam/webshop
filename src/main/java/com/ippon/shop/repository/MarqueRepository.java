package com.ippon.shop.repository;

import com.ippon.shop.domain.Marque;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Marque entity.
 */
@SuppressWarnings("unused")
public interface MarqueRepository extends JpaRepository<Marque,Long> {

}
