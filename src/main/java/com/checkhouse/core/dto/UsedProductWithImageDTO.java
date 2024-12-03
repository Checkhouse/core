package com.checkhouse.core.dto;

import java.util.List;
import java.util.UUID;

import com.checkhouse.core.entity.enums.UsedProductState;

public record UsedProductWithImageDTO(
    UUID usedProductId,
    String title,
    String description,
    int price,
    UsedProductState state,
    boolean negoState,
    UUID userId,
    UUID originProductId,
    List<String> imageUrls
) {
    public static UsedProductWithImageDTO of(UsedProductDTO product, List<String> imageUrls) {
        return new UsedProductWithImageDTO(
            product.usedProductId(),
            product.title(),
            product.description(),
            product.price(),
            product.status(),
            product.negoState(),
            product.userId(),
            product.originProductId(),
            imageUrls
        );
    }
} 