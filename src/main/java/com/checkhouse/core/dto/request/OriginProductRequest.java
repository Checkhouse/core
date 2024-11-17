package com.checkhouse.core.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

public class OriginProductRequest {
    @Builder
    public record AddOriginProductRequest(
            @NotNull
            String name,
            @NotNull
            String company, // todo 회사별로 모아보기 하려면 어떻게 해야하지?
            @NotNull
            UUID categoryId,
            List<String> imageUrls
    ){}

    @Builder
    public record UpdateOriginProductInfo(
            @NotNull
            UUID originProductId,
            String name,
            String company
    ) {}

    @Builder
    public record UpdateOriginProductCategory(
            @NotNull
            UUID categoryId,
            @NotNull
            UUID originProductId
    ) {}

    @Builder
    public record DeleteOriginProduct(
            @NotNull
            UUID originProductId
    ) {}
}
