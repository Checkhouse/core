package com.checkhouse.core.dto;

import com.checkhouse.core.entity.Category;

import java.util.List;
import java.util.UUID;

public record OriginProductDTO(
        UUID originProductId,
        String name,
        String company,
        Category category
) {
}
