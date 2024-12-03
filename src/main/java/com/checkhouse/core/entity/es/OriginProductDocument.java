package com.checkhouse.core.entity.es;

import com.checkhouse.core.entity.OriginProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "origins")
public class OriginProductDocument {
    @Id
    private String id;

    private String originProductId;

    private String title;

    public static OriginProductDocument from(OriginProduct origin) {
        return OriginProductDocument.builder()
                .originProductId(origin.getOriginProductId().toString())
                .title(origin.getName())
                .build();
    }
}
