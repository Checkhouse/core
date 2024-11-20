package com.checkhouse.core.dto.request;

import java.util.UUID;

import com.checkhouse.core.entity.enums.DeliveryState;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public class CollectRequest {
    @Builder
    public record AddCollectRequest(
        UUID deliveryId,
        UUID usedProductId
    ) {}

    @Builder
    public record UpdateCollectStateRequest(
        UUID collectId,
        DeliveryState deliveryState
    ) {}

}
