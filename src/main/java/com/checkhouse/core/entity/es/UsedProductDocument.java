package com.checkhouse.core.entity.es;

import com.checkhouse.core.entity.UsedProduct;
import com.checkhouse.core.entity.enums.UsedProductState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "used_products")
public class UsedProductDocument {
    @Id
    private String id;

    private String usedProductId;

    private String title;

    private String description;

    private int price;

    private boolean isNegoAllow = true;

    private String state;

    private UUID userId;

    private String thumbnailUrl;

    private List<String> imageUrls;

    public static UsedProductDocument from(UsedProduct usedProduct) {
        return UsedProductDocument.builder()
                .usedProductId(usedProduct.getUsedProductId().toString())
                .title(usedProduct.getTitle())
                .description(usedProduct.getDescription())
                .price(usedProduct.getPrice())
                .isNegoAllow(usedProduct.isNegoAllow())
                .state(usedProduct.getState().name())
                .userId(usedProduct.getUsedProductId())
                .build();
    }
    // 해당 예시에서는 실제  dto는 아니지만 실제에서는 dto로 적용
    public UsedProduct toDto() {
        return UsedProduct.builder()
                .usedProductId(UUID.fromString(this.getUsedProductId()))
                .title(this.getTitle())
                .description(this.getDescription())
                .price(this.getPrice())
                .isNegoAllow(this.isNegoAllow())
                .state(UsedProductState.valueOf(this.getState()))
                .usedProductId(this.getUserId())
                .build();
    }
}
