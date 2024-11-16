package com.checkhouse.core.dto;

import java.util.UUID;

public record ImageDTO(
        UUID imageId,
        String imageURL
) {}