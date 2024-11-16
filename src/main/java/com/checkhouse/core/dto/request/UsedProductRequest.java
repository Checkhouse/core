package com.checkhouse.core.dto.request;

import com.checkhouse.core.entity.enums.UsedProductState;

import java.util.UUID;
public class UsedProductRequest {
    public record AddUsedProductRequest(
            String title,
            String description,
            int price,
            boolean isNegoAllow,
            UUID userId,
            UUID originProductId
    ) {}

    public record UpdateUsedProductInfo(
            UUID usedProductId,
            String title,
            String description,
            int price
    ) {}

    public record UpdateUsedProductNegoState(
            UUID usedProductId,
            boolean state

    ) {}

    public record UpdateUsedProductState(
            UUID usedProductId,
            UsedProductState status
    ) {}
}
