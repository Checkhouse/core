package com.checkhouse.core.dto.request;

import java.util.UUID;

import com.checkhouse.core.entity.enums.DeliveryState;
import lombok.Builder;

public class SendRequest {
    @Builder
    public record AddSendRequest(
        UUID sendId,
        UUID transactionId,
        UUID deliveryId
    ) {}

    @Builder
    public record UpdateSendStateRequest(
        UUID sendId,
        DeliveryState deliveryState
    ) {}

    @Builder
    public record UpdateSendTransactionRequest(
        UUID transactionId
    ) {}

    @Builder
    public record UpdateSendDeliveryRequest(
        UUID deliveryId
    ) {}
}