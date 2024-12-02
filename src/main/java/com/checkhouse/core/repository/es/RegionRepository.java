package com.checkhouse.core.repository.es;

import com.checkhouse.core.entity.es.RegionDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;


public interface RegionRepository extends ElasticsearchRepository<RegionDocument, Integer> {
    @Query("""
            {
                "geo_shape": {
                  "location": {
                    "relation": "intersects",
                    "shape": {
                      "type": "point",
                      "coordinates": [?0, ?1]
                    }
                  }
                }
            }
            """)
    List<RegionDocument> findRegionDocumentByPoint(double longitude, double latitude);
}
