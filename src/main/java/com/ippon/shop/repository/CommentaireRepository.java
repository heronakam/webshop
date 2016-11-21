package com.ippon.shop.repository;

import com.ippon.shop.domain.Commentaire;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Spring Data JPA repository for the Commentaire entity.
 */
@SuppressWarnings("unused")
public interface CommentaireRepository extends JpaRepository<Commentaire,Long> {

    @Query("select commentaire from Commentaire commentaire where commentaire.user.login = ?#{principal.username}")
    List<Commentaire> findByUserIsCurrentUser();

    @Query("select commentaire from Commentaire commentaire where commentaire.produit.id =:id")
    List<Commentaire> findByProductIsCurrentProduct(@Param("id") Long id);

}
