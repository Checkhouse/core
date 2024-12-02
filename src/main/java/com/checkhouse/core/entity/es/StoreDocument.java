package com.checkhouse.core.entity.es;

import com.checkhouse.core.entity.Store;
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
@Document(indexName = "store")
public class StoreDocument {
    @Id
    private UUID storeId;

    private String name;

    private String address;

    private GeoJsonPoint location;

    public static StoreDocument from(Store store) {
        return StoreDocument.builder()
                .storeId(store.getStoreId())
                .name(store.getName())
                .location(GeoJsonPoint.of(store.getAddress().getLocation()))
                .address(store.getAddress().getAddress())
                .build();
    }
    public Store toDto() {
        return Store.builder()
                .storeId(this.getStoreId())
                .name(this.getName())
                .build();
    }
}
