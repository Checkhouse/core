package com.checkhouse.core.dto.request;

import java.util.UUID;

import com.checkhouse.core.entity.enums.DeliveryState;

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
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCollectStateRequest {
        private UUID collectId;
        private DeliveryState deliveryState;
    }

}
