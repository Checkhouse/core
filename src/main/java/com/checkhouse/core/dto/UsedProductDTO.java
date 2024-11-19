package com.checkhouse.core.dto;


public class UsedProductDTO {

}

import com.checkhouse.core.entity.enums.UsedProductState;

import java.util.UUID;

public record UsedProductDTO(
        UUID usedProductId,
        String title,
        String description,
        int price,
        UsedProductState status,
        boolean negoState,
        UUID userId,
        UUID originProductId
) {}

