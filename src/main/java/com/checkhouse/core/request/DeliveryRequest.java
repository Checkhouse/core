package com.checkhouse.core.request;

import com.checkhouse.core.entity.enums.DeliveryState;

import java.util.UUID;

public class DeliveryRequest {

    public record AddDeliveryRequest(
            UUID deliveryId,
            UUID addressId,
            String trackingCode
    ) {}

    public record UpdateDeliveryStateRequest(
            UUID deliveryId,
            DeliveryState deliveryState
    ) {}

    public record RegisterTrackingCodeRequest(
            UUID deliveryId,
            String trackingCode
    ) {}

    public record DeleteDeliveryRequest(
            UUID deliveryId
    ) {}
}
