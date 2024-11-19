package com.checkhouse.core.dto;

import com.checkhouse.core.entity.UsedProduct;
import com.checkhouse.core.entity.User;
import com.checkhouse.core.entity.enums.NegotiationState;

import java.util.UUID;


public record NegotiationDTO (
        UUID negotiationId,
        UsedProduct usedProduct,
        User seller,
        User buyer,
        NegotiationState state,
        int price
) {}

