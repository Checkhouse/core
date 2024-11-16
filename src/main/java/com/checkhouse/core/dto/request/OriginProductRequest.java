package com.checkhouse.core.dto.request;

import java.util.List;
import java.util.UUID;

public class OriginProductRequest {
    public record AddOriginProductRequest(
            String name,
            String company, // todo 회사별로 모아보기 하려면 어떻게 해야하지?
            UUID categoryId,
            List<String> imageUrls
    ){}

    public record UpdateOriginProductInfo(
            UUID originProductId,
            String name,
            String company
    ) {}
    public record UpdateOriginProductCategory(
            UUID categoryId,
            UUID originProductId
    ) {}

    public record DeleteOriginProduct() {}
}
