package com.checkhouse.core.repository.es;

import com.checkhouse.core.entity.es.ZoneDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ZoneRepository extends ElasticsearchRepository<ZoneDocument, Integer> {
    @Query("""
            {
                 "terms": {
                   "areasIds": [?0]
                 }
             }
            """)
    List<ZoneDocument> findByAreasIdsContaining(Integer id);
}
