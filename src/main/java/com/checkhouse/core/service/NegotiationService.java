package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.NegotiationDTO;

import com.checkhouse.core.entity.Negotiation;
import com.checkhouse.core.entity.UsedProduct;
import com.checkhouse.core.entity.User;
import com.checkhouse.core.entity.enums.NegotiationState;

import com.checkhouse.core.repository.mysql.NegotiationRepository;
import com.checkhouse.core.dto.request.NegotiationRequest;
import com.checkhouse.core.repository.mysql.UsedProductRepository;
import com.checkhouse.core.repository.mysql.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLDelete;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@SQLDelete(sql="")
public class NegotiationService {
    private final UserRepository userRepository;
    private final NegotiationRepository negotiationRepository;
    private final UsedProductRepository usedProductRepository;

// 네고 등록
    public NegotiationDTO addNegotiation(NegotiationRequest.AddNegotiationRequest request){
        // 중고 상품 확인
        UsedProduct usedProduct = usedProductRepository
                .findById(request.usedProductId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._USED_PRODUCT_NOT_FOUND)
        );

        // 네고거부 상태인지 확인
        if(!usedProduct.isNegoAllow()){
            throw new GeneralException(ErrorStatus._NEGOTIATION_NOT_ALLOWED);
        }

        // 등록 가격 비교
        if(usedProduct.getPrice() <= request.price()){
            throw new GeneralException(ErrorStatus._NEGOTIATION_PRICE_ERROR);
        }

        // 판매자 및 구매자 확인
        User seller = userRepository.findById(request.sellerId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));
        User buyer = userRepository.findById(request.buyerId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));
        // 판매자 체크
        if(!seller.getUserId().equals(usedProduct.getUser().getUserId())){
            throw new GeneralException(ErrorStatus._NEGOTIATION_USER_ERROR);
        }

        // 네고 등록
        // toDo 레디스 시간관리 추가
        Negotiation savedNegotiation = negotiationRepository.save(
            Negotiation.builder()
                    .usedProduct(usedProduct)
                    .seller(seller)
                    .buyer(buyer)
                    .state(NegotiationState.WAITING)
                    .price(request.price())
                    .build()
        );

        return savedNegotiation.toDto();
    }

    // 네고 상태 변경 (승인, 거절, 취소)
    public NegotiationDTO updateNegotiationState(NegotiationRequest.UpdateNegotiationRequest request){
        // 네고 조회
        Negotiation negotiation = negotiationRepository.findById(request.negotiationId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._NEGOTIATION_NOT_FOUND));

        // 상태 중복 체크
        if(negotiation.getState() == request.state()){
            throw new GeneralException(ErrorStatus._NEGOTIATION_STATE_DUPLICATE);
        }
        // 상태 변경
        switch(negotiation.getState()){
            case CANCELLED: // 취소된 경우
                throw new GeneralException(ErrorStatus._NEGOTIATION_ALREADY_CANCELLED);
            case ACCEPTED: // 승인된 경우
                throw new GeneralException(ErrorStatus._NEGOTIATION_ALREADY_ACCEPTED);
            case DENIED: // 거절된 경우
                throw new GeneralException(ErrorStatus._NEGOTIATION_ALREADY_DENIED);
        }


        // 상태 변경
        negotiation.updateState(request.state());

        return negotiation.toDto();
    }

    // 제안한 네고 조회
    public List<NegotiationDTO> getNegotiationByBuyer(NegotiationRequest.GetNegotiationByBuyerRequest request){
            User buyer = userRepository.findById(request.buyerId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        return negotiationRepository.findAllByBuyerId(request.buyerId())
                .stream().map(Negotiation::toDto).collect(Collectors.toList());
    }

    // 받은 네고 조회
    public List<NegotiationDTO> getNegotiationBySeller(NegotiationRequest.GetNegotiationBySellerRequest request){
        User seller = userRepository.findById(request.sellerId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        return negotiationRepository.findAllBySellerId(seller.getUserId())
                .stream().map(Negotiation::toDto).collect(Collectors.toList());
    }

    // 네고 삭제
    void deleteNegotiation(NegotiationRequest.DeleteNegotiationRequest request){
        Negotiation negotiation = negotiationRepository.findById(request.negotiationId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._NEGOTIATION_NOT_FOUND));
        negotiationRepository.delete(negotiation);
    }

}
