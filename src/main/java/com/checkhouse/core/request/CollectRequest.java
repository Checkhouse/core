package com.checkhouse.core.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CollectRequest {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterCollectRequest {
        @NotNull
        private UUID deliveryId;
        @NotNull
        private UUID usedProductId;
        
    }
}
