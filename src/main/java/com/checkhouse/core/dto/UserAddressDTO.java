package com.checkhouse.core.dto;

import java.util.UUID;

public record UserAddressDTO(
        UUID userAddressId,
        AddressDTO address,
        UserDTO user,
        HubDTO hub
) {}