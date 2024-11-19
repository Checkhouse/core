package com.checkhouse.core.dto.request;

import com.checkhouse.core.entity.enums.NegotiationState;

import java.util.UUID;

public class NegotiationRequest {
    // 네고 등록
    public record AddNegotiationRequest(
            UUID negotiationId,
            UUID usedProductId,
            UUID sellerId,
            UUID buyerId,
            int price
    ){ }
    // 네고 상태 변경
    public record UpdateNegotiationRequest(
            UUID negotiationId,
            NegotiationState state
    ){ }

    // 제안한 네고 조회
    public record GetNegotiationByBuyerRequest(
            UUID buyerId
    ){ }
    // 받은 네고 조회
    public record GetNegotiationBySellerRequest(
            UUID sellerId
    ){ }

    // 네고 삭제
    public record DeleteNegotiationRequest(
            UUID negotiationId
    ){ }

}
