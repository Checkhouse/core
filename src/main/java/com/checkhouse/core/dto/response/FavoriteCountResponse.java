package com.checkhouse.core.dto.response;

import lombok.Builder;

@Builder
public record FavoriteCountResponse(
    int count
) {} 