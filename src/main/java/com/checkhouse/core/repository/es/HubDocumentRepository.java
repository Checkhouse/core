package com.checkhouse.core.repository.es;

import com.checkhouse.core.entity.es.HubDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.UUID;

public interface HubDocumentRepository extends ElasticsearchRepository<HubDocument, String> {
    List<HubDocument> findByZoneId(Integer zoneId);
}
