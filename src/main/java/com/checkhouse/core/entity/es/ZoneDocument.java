package com.checkhouse.core.entity.es;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "zone")
public class ZoneDocument {
    @Id
    private Integer zoneId;

    private List<Integer> areasIds;
}
