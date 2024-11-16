package com.checkhouse.core.dto;

import java.util.UUID;

public record HubDTO(
        UUID hubId,
        String name,
        int clusteredId,
        AddressDTO addressDTO
) {}