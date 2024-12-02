package com.checkhouse.core.repository.es;

import com.checkhouse.core.entity.es.UsedProductDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsedProductDocumentRepository extends ElasticsearchRepository<UsedProductDocument, UUID> {
    Optional<UsedProductDocument> findByUsedProductId(UUID id);

    List<UsedProductDocument> findByTitleContainsIgnoreCase(String title);

    List<UsedProductDocument> findByTitleAndDescriptionContainingIgnoreCase(String query);

    Page<UsedProductDocument> findByTitleContainsIgnoreCase(String title, Pageable pageable);
}
