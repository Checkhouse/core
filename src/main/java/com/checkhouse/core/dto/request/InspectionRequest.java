package com.checkhouse.core.dto.request;

import java.util.List;
import java.util.UUID;

import com.checkhouse.core.dto.UsedImageDTO;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import com.checkhouse.core.dto.UserDTO;

import lombok.Builder;

public class InspectionRequest {
    @Builder
    public record AddInspectionRequest(
        UUID usedProductId,
        @Nullable
        String description,
        UUID userId
    ) {}

    @Builder
    public record UpdateInspectionStateRequest(
        UUID inspectionId
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

    @Builder
    public record GetInspectionListRequest(
        UUID usedProductId
    ) {}

    @Builder
    public record UpdateInspectionRequest(
        UUID inspectionId,
        List<String> imageURL,
        List<UUID> usedImageId,
        String description
    ) {}

    @Builder
    public record StartInspectionResponse(
        UUID inspectionId,
        String usedProductName,
        String usedProductDescription,
        List<UsedImageDTO> usedImages
    ) {}

}
