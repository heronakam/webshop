package com.ippon.shop.repository.search;

import com.ippon.shop.domain.Panier;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Panier entity.
 */
public interface PanierSearchRepository extends ElasticsearchRepository<Panier, Long> {
}
