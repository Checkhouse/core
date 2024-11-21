package com.checkhouse.core.dto;

import com.checkhouse.core.entity.enums.NegotiationState;

import java.util.UUID;


public record NegotiationDTO (
        UUID negotiationId,
        UsedProductDTO usedProduct,
        UserDTO seller,
        UserDTO buyer,
        NegotiationState state,
        int price
) {}

