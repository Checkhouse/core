package com.checkhouse.core.dto.request;

import lombok.Builder;

import java.util.UUID;

public class AddressRequest {

    @Builder
    public record AddAddressRequest (
            UUID addressId,
            String name,
            String address,
            int zipcode,
            String phone,
            String addressDetail
    ) {}
    @Builder
    public record UpdateAddressRequest (
            UUID addressId,
            String name,
            String address,
            int zipcode,
            String phone,
            String addressDetail
    ) {}
    @Builder
    public record GetAddressByIdRequest (UUID addressId) {}
    @Builder
    public record DeleteAddressRequest (UUID addressId) {}

}
