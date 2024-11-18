package com.checkhouse.core.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class ImageRequest {

    public record AddImageRequest (
            UUID imageId,
            String imageURL
    ) {}

    public record GetImageRequest (
        UUID imageId
    ) {}
    public record DeleteImageRequest (
            UUID imageId
    ) {}

}
