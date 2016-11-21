package com.ippon.shop.repository.search;

import com.ippon.shop.domain.Marque;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Marque entity.
 */
public interface MarqueSearchRepository extends ElasticsearchRepository<Marque, Long> {
}
