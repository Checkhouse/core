package com.checkhouse.core.dto;

import java.util.List;
import java.util.UUID;

public record OriginProductWithImagesDTO(
    UUID originProductId,
    String name,
    String company,
    CategoryDTO categoryDTO,
    List<OriginImageDTO> images
) {
    public static OriginProductWithImagesDTO of(OriginProductDTO product, List<OriginImageDTO> images) {
        return new OriginProductWithImagesDTO(
            product.originProductId(),
            product.name(),
            product.company(),
            product.categoryDTO(),
            images
        );
    }
}