package com.checkhouse.core.request;

import com.checkhouse.core.entity.Address;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class StoreRequest {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddStoreRequest {
        @NotNull
        UUID storeId;

        @NotNull
        Address address;

        @NotNull
        String name;

        @NotNull
        String code;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateStoreRequest {
        @NotNull
        UUID storeId;

        @NotNull
        Address address;

        @NotNull
        String name;

        @NotNull
        String code;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VerifyCodeRequest {
        @NotNull
        UUID storeId;

        @NotNull
        String code;
    }



}
