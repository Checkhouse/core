package com.checkhouse.core.dto.request;

import lombok.Builder;

import java.util.UUID;
public class FavoriteRequest {
    @Builder
    public record AddToFavoriteRequest(UUID userId, UUID originProductId) {}

    @Builder
    public record RemoveFromFavoriteRequest(UUID userId, UUID originProductId) {}

    @Builder
    public record GetUserFavoriteOrigins(UUID userId) {}

    @Builder
    public record AddUsedProductLikeRequest(UUID userId, UUID usedProductId) {}

    @Builder
    public record RemoveUsedProductLikeRequest(UUID userId, UUID usedProductId) {}

    @Builder
    public record GetUserFavoriteUsed(UUID userId) {}

    @Builder
    public record GetUsedProductFavoriteCountRequest(UUID usedProductId) {}
}
