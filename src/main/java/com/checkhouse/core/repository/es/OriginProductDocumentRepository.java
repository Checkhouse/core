package com.checkhouse.core.repository.es;

import com.checkhouse.core.entity.es.OriginProductDocument;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface OriginProductDocumentRepository  extends ElasticsearchRepository<OriginProductDocument, String> {
    List<OriginProductDocument> findByTitleContaining (String query);
}
