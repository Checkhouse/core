package com.checkhouse.core.dto;

import org.springframework.data.geo.Point;

import java.util.UUID;

public record AddressDTO(
        UUID addressId,
        String name,
        String address,
        String zipcode,
        String phone,

        String addressDetail,
        Point location
) {}