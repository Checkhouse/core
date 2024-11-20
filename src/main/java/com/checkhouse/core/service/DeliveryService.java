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
import org.springframework.stereotype.Service;


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
        //존재하지 않는 주소 정보가 있을 수 있으므로 예외처리
        Address address = addressRepository.findById(req.addressId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._ADDRESS_ID_NOT_FOUND));
        //배송 등록
        Delivery savedDelivery = deliveryRepository.save(
            Delivery.builder()
                .deliveryId(req.deliveryId())
                .trackingCode(req.trackingCode())
                .deliveryState(DeliveryState.PRE_DELIVERY)
                .address(address)
                .build()
        );
        return savedDelivery.toDTO();
    }
    
    /**
     * 배송 상태 업데이트
     */
    public DeliveryDTO updateDeliveryState(DeliveryRequest.UpdateDeliveryStateRequest req) {
        Delivery delivery = deliveryRepository.findById(req.deliveryId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._DELIVERY_ID_NOT_FOUND));
        delivery.UpdateDeliveryState(req.deliveryState());
        return delivery.toDTO();
    }

    /**
     * 송장 번호 등록
     */
        public DeliveryDTO registerTrackingCode(DeliveryRequest.RegisterTrackingCodeRequest req) {
        Delivery delivery = deliveryRepository.findById(req.deliveryId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._DELIVERY_ID_NOT_FOUND));
        delivery.UpdateTrackingCode(req.trackingCode());
        return delivery.toDTO();
    }
}
