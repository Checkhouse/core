package com.checkhouse.core.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class AddressRequest {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddAddressRequest {
        private UUID addressId = UUID.randomUUID();

        @NotNull
        String name;
        @NotNull
        String address;
        @NotNull
        int zipcode;
        @NotNull
        String phone;

        String AddressDetail;
    }

}
