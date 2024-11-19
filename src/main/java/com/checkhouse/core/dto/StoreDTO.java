package com.checkhouse.core.dto;

import java.util.UUID;

public record StoreDTO(
        UUID storeId,
        String name,
        String code,
        AddressDTO addressDTO
) {}