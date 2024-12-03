package com.checkhouse.core.entity.es;

import com.checkhouse.core.entity.OriginProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Builder
@Getter
@Document(indexName = "origin")
public class OriginProductDocument {
    @Id
    private String id;

    private String originProductId;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String title;

    public static OriginProductDocument from(OriginProduct origin) {
        return OriginProductDocument.builder()
                .originProductId(origin.getOriginProductId().toString())
                .title(origin.getName())
                .build();
    }
}
