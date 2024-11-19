package com.checkhouse.core.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.data.geo.Point;

import java.util.UUID;

public class AddressRequest {

    @Builder
    public record AddAddressRequest (
            @NotNull
            UUID addressId,
            @NotNull
            String name,
            @NotNull
            String address,
            @NotNull
            int zipcode,
            @NotNull
            String phone,
            String addressDetail,
            @NotNull
            Point location
    ) {}
    @Builder
    public record UpdateAddressRequest (
            @NotNull
            UUID addressId,
            String name,
            String address,
            int zipcode,
            String phone,
            String addressDetail,
            Point location
    ) {}
    @Builder
    public record GetAddressByIdRequest (@NotNull UUID addressId) {}
    @Builder
    public record DeleteAddressRequest (@NotNull UUID addressId) {}

}
