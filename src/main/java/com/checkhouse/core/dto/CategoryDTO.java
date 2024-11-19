package com.checkhouse.core.dto;

import java.util.UUID;

public record CategoryDTO(
        UUID categoryId,
        String name
) {}