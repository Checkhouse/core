package com.checkhouse.core.dto;

import java.util.UUID;

public record InspectionDTO(
        UUID inspectionId,
        boolean isDone,
        String description,
        UsedProductDTO usedProductDTO,
        UserDTO userDTO
) {}
