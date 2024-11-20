package com.checkhouse.core.dto.request;

import java.util.UUID;

import com.checkhouse.core.entity.enums.DeliveryState;

public class SendRequest {
    public record RegisterSendRequest(
        UUID sendId,
        UUID transactionId,
        UUID deliveryId
    ) {}
    public record UpdateSendStateRequest(
        DeliveryState deliveryState
    ) {}
    public record UpdateSendTransactionRequest(
        UUID transactionId
    ) {}
    public record UpdateSendDeliveryRequest(
        UUID deliveryId
    ) {}
}
