package com.ippon.shop.repository.search;

import com.ippon.shop.domain.Produit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Produit entity.
 */
public interface ProduitSearchRepository extends ElasticsearchRepository<Produit, Long> {
}
