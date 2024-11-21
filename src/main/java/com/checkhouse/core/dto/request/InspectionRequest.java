package com.checkhouse.core.dto.request;

import java.util.UUID;

import com.checkhouse.core.dto.UserDTO;

import lombok.Builder;

public class InspectionRequest {
    @Builder
    public record AddInspectionRequest(
        UUID usedProductId,
        String description,
        boolean isDone,
        UUID userId
    ) {}

    @Builder
    public record UpdateInspectionRequest(
        UUID inspectionId,
        boolean isDone,
        UserDTO user
    ) {}
}
