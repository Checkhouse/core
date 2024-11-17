package com.checkhouse.core.dto.request;

import java.util.UUID;
public class FavoriteRequest {

    public record AddToFavoriteRequest(UUID userId, UUID originProductId) {}

    public record RemoveFromFavoriteRequest(UUID userId, UUID originProductId) {}

    public record GetUserFavoriteOrigins(UUID userId) {}

    public record AddUsedProductLikeRequest(UUID userId, UUID usedProductId) {}

    public record RemoveUsedProductLikeRequest(UUID userId, UUID usedProductId) {}

    public record GetUserFavoriteUsed(UUID userId) {}
}
