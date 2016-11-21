package com.ippon.shop.repository.search;

import com.ippon.shop.domain.Commentaire;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Commentaire entity.
 */
public interface CommentaireSearchRepository extends ElasticsearchRepository<Commentaire, Long> {
}
