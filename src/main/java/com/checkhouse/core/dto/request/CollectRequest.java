package com.checkhouse.core.dto.request;

import java.util.UUID;

import com.checkhouse.core.entity.enums.DeliveryState;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public class CollectRequest {
    @Builder
    public record AddCollectRequest(
        UUID usedProductId
    ) {}

    @Builder
    public record UpdateCollectRequest(
        UUID collectId,
        DeliveryState deliveryState
    ) {}

    @Builder
    public record DeleteCollectRequest(
        UUID collectId
    ) {}
}
