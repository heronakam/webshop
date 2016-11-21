package com.ippon.shop.repository.search;

import com.ippon.shop.domain.Caracteristique;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Caracteristique entity.
 */
public interface CaracteristiqueSearchRepository extends ElasticsearchRepository<Caracteristique, Long> {
}
