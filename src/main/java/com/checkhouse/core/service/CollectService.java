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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        //존재하지 않는 중고 상품 정보 예외처리
        UsedProduct usedProduct = usedProductRepository.findById(req.usedProductId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._USED_PRODUCT_ID_NOT_FOUND));
        
        //이미 수거 등록이 된 상품은 수거 등록 실패
        if(collectRepository.findByUsedProduct(usedProduct).isPresent()) {
            throw new GeneralException(ErrorStatus._COLLECT_ALREADY_EXISTS);
        }

        //새로운 배송 정보 생성
        Delivery delivery = Delivery.builder()
            .deliveryId(UUID.randomUUID())
            .deliveryState(DeliveryState.COLLECTING)
            .address(usedProduct.getAddress())
            .build();
        delivery = deliveryRepository.save(delivery);

        //수거 등록
        Collect collect = Collect.builder()
            .delivery(delivery)
            .usedProduct(usedProduct)
            .build();
        Collect savedCollect = collectRepository.save(collect);
        return savedCollect.toDto();
    }
    //수거 상태 수정
    public CollectDTO updateCollect(CollectRequest.UpdateCollectRequest req) {
        //존재하지 않는 수거 정보가 있을 수 있으므로 예외처리
        Collect collect = collectRepository.findById(req.collectId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._COLLECT_ID_NOT_FOUND));
        collect.updateCollectState(req.deliveryState());
        Collect updatedCollect = collectRepository.save(collect);
        return updatedCollect.toDto();
    }

    //수거 삭제
    public void deleteCollect(CollectRequest.DeleteCollectRequest req) {
        Collect collect = collectRepository.findById(req.collectId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._COLLECT_ID_NOT_FOUND));
            

        collectRepository.delete(collect);
        
    }
    //수거 리스트 조회
    public List<CollectDTO> getCollectList() {
        return collectRepository.findAll().stream()
            .map(Collect::toDto)
            .collect(Collectors.toList());
    }
}
