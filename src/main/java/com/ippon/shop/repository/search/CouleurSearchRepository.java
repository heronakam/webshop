package com.ippon.shop.repository.search;

import com.ippon.shop.domain.Couleur;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Couleur entity.
 */
public interface CouleurSearchRepository extends ElasticsearchRepository<Couleur, Long> {
}
