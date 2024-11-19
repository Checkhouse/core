package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.DeliveryDTO;
import com.checkhouse.core.entity.Address;
import com.checkhouse.core.entity.Delivery;
import com.checkhouse.core.entity.enums.DeliveryState;
import com.checkhouse.core.repository.mysql.AddressRepository;
import com.checkhouse.core.repository.mysql.DeliveryRepository;
import com.checkhouse.core.request.DeliveryRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final AddressRepository addressRepository;

    /**
     * 배송 등록
     */
    DeliveryDTO addDelivery(DeliveryRequest.AddDeliveryRequest req) {
        Address address = addressRepository.findById(req.addressId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._ADDRESS_ID_NOT_FOUND));

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
    DeliveryDTO updateDeliveryState(DeliveryRequest.UpdateDeliveryStateRequest req) {
        //존재하지 않는 배송 정보가 있을 수 있으므로 예외처리
        Delivery delivery = deliveryRepository.findById(req.deliveryId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._DELIVERY_ID_NOT_FOUND));

        //배송 상태 업데이트
        delivery.UpdateDeliveryState(req.deliveryState());
        return delivery.toDTO();
    }

    /**
     * 송장 번호 등록
     */
    DeliveryDTO registerTrackingCode(DeliveryRequest.RegisterTrackingCodeRequest req) {
        Delivery delivery = deliveryRepository.findById(req.deliveryId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._DELIVERY_ID_NOT_FOUND));

        delivery.UpdateTrackingCode(req.trackingCode());
        return delivery.toDTO();
    }
}
