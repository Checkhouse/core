package com.checkhouse.core.dto.request;

import com.checkhouse.core.entity.enums.DeliveryState;
import lombok.Builder;
import org.springframework.lang.Nullable;

import java.util.UUID;

public class DeliveryRequest {

    @Builder
    public record AddDeliveryRequest(
            UUID addressId,
            @Nullable
            String trackingCode
    ) {}

    @Builder
    public record UpdateDeliveryStateRequest(
            UUID deliveryId,
            DeliveryState deliveryState
    ) {}

    @Builder
    public record RegisterTrackingCodeRequest(
            UUID deliveryId,
            String trackingCode
    ) {}

    @Builder
    public record DeleteDeliveryRequest(
            UUID deliveryId
    ) {}

    @Builder
    public record GetDeliveryListRequest(
            UUID userId
    ) {}
}
