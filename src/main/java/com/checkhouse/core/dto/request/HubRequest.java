package com.checkhouse.core.dto.request;

import lombok.Builder;

import java.util.UUID;

public class HubRequest {

    @Builder
    public record AddHubRequest(
            UUID hubId,
            UUID addressId,
            String name,
            int clusteredId
    ) {}
    @Builder
    public record UpdateHubRequest(
            UUID hubId,
            UUID addressId,
            String name,
            int clusteredId
    ) {}
    @Builder
    public record DeleteHubRequest(
            UUID hubId
    ) {}

    @Builder
    public record AllocateHubRequest(
            UUID addressId
    ) {}

    @Builder
    public record AddStockRequest(
            UUID stockId,
            UUID usedProductId,
            String area,
            UUID hubId
    ) {}
    @Builder
    public record GetStockByUsedProductIdRequest(
            UUID usedProductId
    ) {}

    @Builder
    public record UpdateStockAreaRequest(
            UUID stockId,
            String area
    ) {}

    @Builder
    public record DeleteStockRequest(
            UUID stockId
    ) {}

    @Builder
    public record GetStocksByHubIdRequest(
            UUID hubId
    ) {}

    @Builder
    public record GetStocksByAreaRequest(
            UUID hubId,
            String area
    ) {}


}