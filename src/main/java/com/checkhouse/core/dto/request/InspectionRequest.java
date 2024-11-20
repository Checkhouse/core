package com.checkhouse.core.dto.request;

import java.util.UUID;

public record InspectionRequest(
        UUID usedProductId,
        String description,
        boolean isDone
) {
    public record CreateInspectionRequest(
            UUID usedProductId,
            String description,
            boolean isDone
    ) {}
    public record UpdateInspectionRequest(
            boolean isDone
    ) {} 
    public record UpdateInspectionImageRequest(
            String imageUrl
    ) {} 
    public record UpdateInspectionStateRequest(
            boolean isDone
    ) {}
}
