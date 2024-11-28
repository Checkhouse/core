package com.checkhouse.core.dto.request;

import com.checkhouse.core.entity.enums.UsedProductState;
import lombok.Builder;

import java.util.UUID;
public class UsedProductRequest {

    @Builder
    public record AddUsedProductRequest(
            String title,
            String description,
            int price,
            boolean isNegoAllow,
            UUID userId,
            UUID originProductId,
            String addressId
    ) {}

    @Builder
    public record UpdateUsedProductInfo(
            UUID usedProductId,
            String title,
            String description,
            int price
    ) {}

    @Builder
    public record UpdateUsedProductNegoState(
            UUID usedProductId,
            boolean state

    ) {}

    @Builder
    public record UpdateUsedProductState(
            UUID usedProductId,
            UsedProductState status
    ) {}

    @Builder
    public record DeleteUsedProduct(
            UUID usedProductId
    ) {}
}
