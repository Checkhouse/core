package com.checkhouse.core.dto;

import com.checkhouse.core.entity.enums.DeliveryState;

import java.util.UUID;

public record DeliveryDTO(
        UUID deliveryId,
        String trackingCode,
        DeliveryState state,
        AddressDTO addressDTO
) {}
