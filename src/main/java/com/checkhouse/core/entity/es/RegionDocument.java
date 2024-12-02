package com.checkhouse.core.entity.es;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.GeoShapeField;
import org.springframework.data.elasticsearch.core.geo.GeoJsonPolygon;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "region")
public class RegionDocument {
    @Id
    private Integer regionId;

    private String name;

    @GeoShapeField
    private GeoJsonPolygon location;
}
