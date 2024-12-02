package com.checkhouse.core.repository.es;

import com.checkhouse.core.entity.es.StoreDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.UUID;

public interface StoreDocumentRepository extends ElasticsearchRepository<StoreDocument, UUID> {
    List<StoreDocument> findAllByLocationWithin(Double longitude, Double latitude, Integer distance);
}
