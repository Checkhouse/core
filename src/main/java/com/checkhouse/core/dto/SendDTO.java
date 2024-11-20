package com.checkhouse.core.dto;

import java.util.UUID;

import com.checkhouse.core.entity.enums.DeliveryState;

public record SendDTO(
    UUID sendId,
    TransactionDTO transactionDTO,
    DeliveryDTO deliveryDTO,
    DeliveryState state
) {}    


