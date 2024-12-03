package com.checkhouse.core.dto.request;

import com.checkhouse.core.dto.AddressDTO;
import com.checkhouse.core.entity.Address;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

import org.springframework.data.geo.Point;

public class StoreRequest {

    // Store 추가 파라미터
    // Id, address, name, code
    @Builder
    public record AddStoreRequest (
            @NotNull
            String name,
            @NotNull
            String address,
            @NotNull
            String zipcode,
            @NotNull
            String phone,
            String addressDetail,
            @NotNull
            Point location,
            @NotNull
            String code
    ){ }
    @Builder
    public record GetStoreRequest (
            UUID storeId
    ) { }

    @Builder
    public record UpdateStoreCodeRequest (
            UUID storeId,
            String code
    ) { }

    @Builder
    public record DeleteStoreRequest (
            UUID storeId
    ) { }

    @Builder
    public record VerifyCodeRequest (
            UUID storeId,
            String code
    ) { }


}
