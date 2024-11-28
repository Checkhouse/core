package com.checkhouse.core.dto.request;

import java.util.UUID;

import com.checkhouse.core.dto.UserDTO;

import lombok.Builder;

public class InspectionRequest {
    @Builder
    public record AddInspectionRequest(
        UUID usedProductId,
        String description,
        UUID userId
    ) {}

    @Builder
    public record UpdateInspectionRequest(
        UUID inspectionId,
        boolean isDone
    ) {}

    @Builder
    public record DeleteInspectionRequest(
        UUID inspectionId
    ) {}

    @Builder
    public record UpdateInspectionDescriptionRequest(
        UUID inspectionId,
        String description
    ) {}
}
