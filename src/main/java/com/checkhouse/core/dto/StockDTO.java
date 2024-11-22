package com.checkhouse.core.dto;

import org.springframework.data.geo.Point;

import java.util.UUID;

public record StockDTO(
        UUID stockId,
        UUID usedProductId,
        String area,
        HubDTO hubDTO
) {}