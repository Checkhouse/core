package com.checkhouse.core.dto;

import java.util.UUID;

public record Token (
        String accessToken,
        String refreshToken
) {}
