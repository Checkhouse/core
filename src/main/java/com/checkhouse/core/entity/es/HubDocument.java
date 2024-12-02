package com.checkhouse.core.entity.es;

import com.checkhouse.core.dto.HubDTO;
import com.checkhouse.core.entity.Hub;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.geo.GeoJsonPoint;

import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "hub")
public class HubDocument {
    @Id
    private String id;

    private String hubId;

    private String address;

    private String name;

    private GeoJsonPoint location;

    private Integer zoneId;

    public static HubDocument from(Hub hub) {
        return HubDocument.builder()
                .hubId(hub.getHubId().toString())
                .name(hub.getName())
                .location(GeoJsonPoint.of(hub.getAddress().getLocation()))
                .zoneId(hub.getClusteredId())
                .build();

    }
    public HubDTO toDto() {
        return Hub.builder()
                .hubId(UUID.fromString(this.getHubId()))
                .name(this.getName())
                .clusteredId(this.getZoneId())
                .build().toDto();
    }
}
