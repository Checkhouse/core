package com.checkhouse.core.service;

import com.checkhouse.core.dto.CollectDTO;
import com.checkhouse.core.entity.Collect;
import com.checkhouse.core.entity.Delivery;
import com.checkhouse.core.entity.enums.DeliveryState;
import com.checkhouse.core.repository.mysql.CollectRepository;
import com.checkhouse.core.repository.mysql.DeliveryRepository;
import com.checkhouse.core.request.CollectRequest;

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
    //수거 등록 성공
    public CollectDTO addCollect(CollectRequest.RegisterCollectRequest req) {
        //존재하지 않는 배송 정보가 있을 수 있으므로 예외처리
        Delivery delivery = deliveryRepository.findById(req.getDeliveryId())
        .orElseThrow(() -> new RuntimeException("Delivery not found"));

        //존재하지 않는 중고 상품 정보가 있을 수 있으므로 예외처리
        // UsedProduct usedProduct = usedProductRepository.findById(collectDTO.usedProductId())
        // .orElseThrow(() -> new RuntimeException("UsedProduct not found"));
        
        //수거 등록
        Collect collect = Collect.builder()
            .delivery(delivery)
            //.usedProduct(usedProduct)
            .state(DeliveryState.COLLECTING)
            .build();
        return collect.toDTO();

    }
    //수거 상태 수정
    CollectDTO updateCollectState(UUID collectId, DeliveryState deliveryState) {
        Collect collect = collectRepository.findById(collectId)
        .orElseThrow(() -> new RuntimeException("Collect not found"));
        collect.updateCollectState(deliveryState);
        return collect.toDTO();
    }
}
