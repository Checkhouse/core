package com.checkhouse.core.request;

import com.checkhouse.core.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class HubRequest {

    public record AddHubRequest(
            UUID hubId,
            Address address,
            String name,
            int clusteredId
    ) {}
    public record UpdateHubRequest(
            UUID hubId,
            Address address,
            String name,
            int clusteredId
    ) {}
    public record DeleteHubRequest(
            UUID hubId
    ) {}

    public record AllocateHubRequest(
            Address address
    ) {}



}
