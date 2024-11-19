package com.checkhouse.core.dto;

import java.util.UUID;

public record FavoriteDTO(
        UUID id,
        UUID productId,
        UUID userId,
        String type
) { }
