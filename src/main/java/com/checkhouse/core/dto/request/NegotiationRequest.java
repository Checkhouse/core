package com.checkhouse.core.dto.request;

import com.checkhouse.core.entity.Negotiation;
import com.checkhouse.core.entity.UsedProduct;
import com.checkhouse.core.entity.User;
import com.checkhouse.core.entity.enums.NegotiationState;
import lombok.Builder;

public class NegotiationRequest {
    // 네고 등록
    @Builder
    public record AddNegotiationRequest(
            UsedProduct usedProduct,
            User seller,
            User buyer,
            int price
    ){ }
    // 네고 상태 변경
    @Builder
    public record UpdateNegotiationRequest(
            Negotiation negotiation,
            NegotiationState state
    ){ }

    // 제안한 네고 조회
    public record GetNegotiationByBuyerRequest(
            User buyer
    ){ }
    // 받은 네고 조회
    public record GetNegotiationBySellerRequest(
            User seller
    ){ }

    // 네고 삭제
    public record DeleteNegotiationRequest(
            Negotiation negotiation
    ){ }

}
