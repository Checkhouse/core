package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.CollectDTO;
import com.checkhouse.core.dto.request.CollectRequest;
import com.checkhouse.core.entity.Collect;
import com.checkhouse.core.entity.Delivery;
import com.checkhouse.core.entity.UsedProduct;
import com.checkhouse.core.entity.enums.DeliveryState;
import com.checkhouse.core.repository.mysql.CollectRepository;
import com.checkhouse.core.repository.mysql.DeliveryRepository;
import com.checkhouse.core.repository.mysql.UsedProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CollectService {
    private final CollectRepository collectRepository;
    private final DeliveryRepository deliveryRepository;
    private final UsedProductRepository usedProductRepository;
    //수거 등록 성공
    public CollectDTO addCollect(CollectRequest.AddCollectRequest req) {
        //존재하지 않는 배송 정보가 있을 수 있으므로 예외처리
        Delivery delivery = deliveryRepository.findById(req.deliveryId())
        .orElseThrow(() -> new GeneralException(ErrorStatus._DELIVERY_ID_NOT_FOUND));

        //존재하지 않는 중고 상품 정보가 있을 수 있으므로 예외처리
        UsedProduct usedProduct = usedProductRepository.findById(req.usedProductId())
        .orElseThrow(() -> new GeneralException(ErrorStatus._USED_PRODUCT_ID_NOT_FOUND));
        
        //이미 수거 등록이 된 상품은 수거 등록 실패
        if(collectRepository.findByUsedProduct(usedProduct).isPresent()) {
            throw new GeneralException(ErrorStatus._COLLECT_ALREADY_EXISTS);
        }
        //배송 상태 업데이트
        delivery.UpdateDeliveryState(DeliveryState.COLLECTING);
        deliveryRepository.save(delivery);

        //수거 등록
        Collect collect = Collect.builder()
            .delivery(delivery)
            .usedProduct(usedProduct)
            .build();
        Collect savedCollect = collectRepository.save(collect);
        return savedCollect.toDTO();

    }
    //수거 상태 수정
    CollectDTO updateCollectState(UUID collectId, DeliveryState deliveryState) {
        //존재하지 않는 수거 정보가 있을 수 있으므로 예외처리
        Collect collect = collectRepository.findById(collectId)
        .orElseThrow(() -> new GeneralException(ErrorStatus._COLLECT_ID_NOT_FOUND));
        collect.updateCollectState(deliveryState);
        Collect updatedCollect = collectRepository.save(collect);
        return updatedCollect.toDTO();
    }

    //수거 삭제
    void deleteCollect(CollectRequest.DeleteCollectRequest req) {
        Collect collect = collectRepository.findById(req.collectId())
        .orElseThrow(() -> new GeneralException(ErrorStatus._COLLECT_ID_NOT_FOUND));

        collectRepository.delete(collect);
        
    }
}
