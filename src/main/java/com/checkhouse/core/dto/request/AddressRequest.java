package com.checkhouse.core.dto.request;

import com.checkhouse.core.entity.Hub;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.data.geo.Point;

import java.util.UUID;

public class AddressRequest {

    @Builder
    public record AddAddressRequest (
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

    @Builder
    public record AddUserAddressRequest (
            UUID userAddressId,
            @NotNull
            UUID userId,
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
            Point location,
            @NotNull
            UUID hubId
    ) {}
    @Builder
    public record GetUserAddressRequest (
            @NotNull
            UUID userAddressId
    ) {}
    @Builder
    public record UpdateUserAddressHubRequest (
            @NotNull
            UUID userAddressId,
            @NotNull
            UUID hubId
    ) {}
    @Builder
    public record DeleteUserAddressRequest (@NotNull UUID userAddressId) {}
    @Builder
    public record GetAllUserAddressesByIdRequest (
            @NotNull
            UUID userId
    ) {}

}
