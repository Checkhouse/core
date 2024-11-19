package com.checkhouse.core.dto;

import java.util.UUID;

import com.checkhouse.core.entity.enums.DeliveryState;

public record CollectDTO(
    UUID collectId,
    UsedProductDTO usedProductDTO,
    DeliveryDTO deliveryDTO,
    DeliveryState state
) {}
