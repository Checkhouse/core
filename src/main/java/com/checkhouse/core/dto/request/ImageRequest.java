package com.checkhouse.core.dto.request;

import com.checkhouse.core.entity.ImageURL;
import com.checkhouse.core.entity.OriginProduct;
import com.checkhouse.core.entity.UsedProduct;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

public class ImageRequest {

    @Builder
    public record AddImageRequest (
            @NotNull
            String imageURL
    ) {}

    @Builder
    public record GetImageRequest (
            @NotNull
        UUID imageId
    ) {}
    @Builder
    public record DeleteImageRequest (
            @NotNull
            UUID imageId
    ) {}

    //원본 이미지
    @Builder
    public record AddOriginImageRequest (
            @NotNull
            UUID originProductId,
            @NotNull
            String imageURL
    ) {}
    @Builder
    public record GetOriginImageRequest (
            @NotNull
            UUID originImageId
    ) {}
    @Builder
    public record GetOriginImagesByOriginIdRequest (
            @NotNull
            UUID originProductId
    ) {}
    public record DeleteOriginImageRequest (
            @NotNull
            UUID originImageId
    ) {}

    //중고 이미지
    @Builder
    public record AddUsedImageRequest (
            @NotNull
            UUID usedProductId,
            @NotNull
            String imageURL
    ) {}
    @Builder
    public record GetUsedImageRequest (
            @NotNull
            UUID usedImageId
    ) {}
    @Builder
    public record GetUsedImagesByUsedIdRequest (
            @NotNull
            UUID usedProductId
    ) {}
    public record DeleteUsedImageRequest (
            @NotNull
            UUID usedImageId
    ) {}

    @Builder
    public record AddInspectionImageRequest (
            @NotNull
            UUID inspectionId,
            @NotNull
            UUID usedImageId,
            @NotNull
            String imageURL
    ) {}

    @Builder
    public record GetInspectionImageRequest (
            @NotNull
            UUID inspectionImageId
    ) {}
    @Builder
    public record GetInspectionImagesByInspectionIdRequest (
            @NotNull
            UUID inspectionId
    ) {}
    @Builder
    public record GetInspectionImageByUsedImageIdRequest (
            @NotNull
            UUID usedImageId
    ) {}
    @Builder
    public record DeleteInspectionImageRequest (
            @NotNull
            UUID inspectionId
    ) {}

}
