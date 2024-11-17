package com.checkhouse.core.request;

import com.checkhouse.core.entity.Address;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class StoreRequest {

    // Store 추가 파라미터
    // Id, address, name, code
    public record AddStoreRequest (
            UUID storeId,
            Address address,
            String name,
            String code
    ){ }

    public record UpdateStoreRequest (
            UUID storeId,
            Address address,
            String name
    ) { }
    public record GetStoreRequest (
            UUID storeId
    ) { }

    public record UpdateStoreCodeRequest (
            UUID storeId,
            String code
    ) { }

    public record DeleteStoreRequest (
            UUID storeId
    ) { }

    public record VerifyCodeRequest (
            UUID storeId,
            String code
    ) { }


}
