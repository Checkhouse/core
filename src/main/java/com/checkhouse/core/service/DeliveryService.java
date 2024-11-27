package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.DeliveryDTO;
import com.checkhouse.core.dto.request.DeliveryRequest;
import com.checkhouse.core.entity.Address;
import com.checkhouse.core.entity.Delivery;
import com.checkhouse.core.entity.enums.DeliveryState;
import com.checkhouse.core.repository.mysql.AddressRepository;
import com.checkhouse.core.repository.mysql.DeliveryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final AddressRepository addressRepository;

    /**
     * 배송 등록
     */
    public DeliveryDTO addDelivery(DeliveryRequest.AddDeliveryRequest req) {
        Address address = addressRepository.findById(req.addressId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._ADDRESS_ID_NOT_FOUND));
                
        Delivery savedDelivery = deliveryRepository.save(
            Delivery.builder()
                .address(address)
                .trackingCode(req.trackingCode())
                .deliveryState(DeliveryState.PRE_DELIVERY)
                .build()
        );
        return savedDelivery.toDto();
    }
    
    /**
     * 배송 상태 업데이트
     */
    @Transactional
    public DeliveryDTO updateDeliveryState(DeliveryRequest.UpdateDeliveryStateRequest req) {
        //존재하지 않는 배송 ID 예외 처리
        Delivery delivery = deliveryRepository.findById(req.deliveryId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._DELIVERY_ID_NOT_FOUND));
        //PRE_DELIVERY, DELIVERING, DELIVERED 외의 상태는 예외 처리
        if(!req.deliveryState().equals(DeliveryState.PRE_DELIVERY)
            && !req.deliveryState().equals(DeliveryState.DELIVERING)
            && !req.deliveryState().equals(DeliveryState.DELIVERED)) {
            throw new GeneralException(ErrorStatus._DELIVERY_STATE_CHANGE_FAILED);
        }
        
        //배송 상태 업데이트
        deliveryRepository.updateDeliveryState(req.deliveryId(), req.deliveryState());
        return delivery.toDto();
    }
    // 배송 리스트 조회
    public List<DeliveryDTO> getDeliveryList() {
        return deliveryRepository.findAll().stream()
            .map(Delivery::toDto)
            .collect(Collectors.toList());
        
    }

    /**
     * 송장 번호 등록
     */
    @Transactional
    public DeliveryDTO registerTrackingCode(DeliveryRequest.RegisterTrackingCodeRequest req) {
        //존재하지 않는 배송 ID 예외 처리
        Delivery delivery = deliveryRepository.findById(req.deliveryId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._DELIVERY_ID_NOT_FOUND));
        deliveryRepository.updateTrackingCode(req.deliveryId(), req.trackingCode());
        return delivery.toDto();
    }

    //배송 삭제
    public void deleteDelivery(DeliveryRequest.DeleteDeliveryRequest req) {
        Delivery delivery = deliveryRepository.findById(req.deliveryId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._DELIVERY_ID_NOT_FOUND));
        deliveryRepository.delete(delivery);
    }
}
