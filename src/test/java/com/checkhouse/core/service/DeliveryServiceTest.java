package com.checkhouse.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.any;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.DeliveryDTO;
import com.checkhouse.core.dto.request.DeliveryRequest;
import com.checkhouse.core.entity.Address;
import com.checkhouse.core.entity.Delivery;
import com.checkhouse.core.entity.enums.DeliveryState;
import com.checkhouse.core.repository.mysql.AddressRepository;
import com.checkhouse.core.repository.mysql.DeliveryRepository;

import org.junit.jupiter.api.BeforeEach;

/**
 * 거래 생성 후 등록할 물건을 수거하러 갈 떄
 */

@ExtendWith(MockitoExtension.class)
public class DeliveryServiceTest {
    @Mock
    private DeliveryRepository deliveryRepository;
    @Mock
    private AddressRepository addressRepository;
    @InjectMocks
    private DeliveryService deliveryService;

    private Address del1addr;
    private Address del2addr;

    private Delivery del1;
    private Delivery del2;
    
    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {
        del1addr = Address.builder()
            .addressId(UUID.randomUUID())
            .name("test1")
            .address("test주소1")
            .zipcode(12345)
            .phone("01012345678")
            .build();
    
        del2addr = Address.builder()
            .addressId(UUID.randomUUID())
            .name("test2")
            .address("test주소2")
            .zipcode(12346)
            .phone("01012345678")
            .build();

        del1 = Delivery.builder()
            .deliveryId(UUID.randomUUID())
            .trackingCode("1234567890")
            .deliveryState(DeliveryState.PRE_DELIVERY)
            .address(del1addr)
            .build();
        
        del2 = Delivery.builder()
            .deliveryId(UUID.randomUUID())
            .trackingCode("1234567890")
            .deliveryState(DeliveryState.PRE_DELIVERY)
            .address(del2addr)
            .build();
    }
    

    
    @Test
    @DisplayName("배송 등록 성공")
    void SUCCESS_addDelivery() {
        // 데이터 생성
        DeliveryRequest.AddDeliveryRequest req = new DeliveryRequest.AddDeliveryRequest(
            del1addr.getAddressId(),
            "1234567890"
        );
        //given
        when(addressRepository.findById(del1addr.getAddressId())).thenReturn(Optional.of(del1addr));
        doReturn(del1).when(deliveryRepository).save(any(Delivery.class));
        //when
        DeliveryDTO result = deliveryService.addDelivery(req);
        //then
        assertEquals(del1.getDeliveryId(), result.deliveryId());
    };



    @DisplayName("배송 상태 업데이트")
    @Test
    void SUCCESS_updateDeliveryStatus() {
        // 데이터 생성
        DeliveryRequest.UpdateDeliveryStateRequest req = new DeliveryRequest.UpdateDeliveryStateRequest(
            del1.getDeliveryId(),
            DeliveryState.DELIVERING
        );
        //given
        when(deliveryRepository.findById(del1.getDeliveryId())).thenReturn(Optional.of(del1));
        //when
        DeliveryDTO result = deliveryService.updateDeliveryState(req);
        //then
        assertEquals(DeliveryState.DELIVERING, result.state());
    }

    @DisplayName("송장 번호 등록")
    @Test
    void SUCCESS_registerTrackingNumber() {
        // 데이터 생성
        DeliveryRequest.RegisterTrackingCodeRequest req = new DeliveryRequest.RegisterTrackingCodeRequest(
            del1.getDeliveryId(),
            "34567890"
        );
        //given
        when(deliveryRepository.findById(del1.getDeliveryId())).thenReturn(Optional.of(del1));
        //when
        DeliveryDTO result = deliveryService.registerTrackingCode(req);
        //then
        assertEquals("34567890", result.trackingCode());
    }

    @DisplayName("존재하지 않는 배송지 ID일 경우 배송 등록 실패")
    @Test
    void FAIL_addDelivery_invalid_address() {
        //given
        when(addressRepository.findById(del1addr.getAddressId())).thenReturn(Optional.empty());
        //when
        DeliveryRequest.AddDeliveryRequest req = new DeliveryRequest.AddDeliveryRequest(
            del1addr.getAddressId(),
            "1234567890"
        );
        //then
        assertThrows(GeneralException.class, () -> deliveryService.addDelivery(req));
    }

    @DisplayName("존재하지 않는 배송 상태로 변경시 업데이트 실패")
    @Test
    void FAIL_updateDeliveryStatus_invalid_status() {
        //when
        when(deliveryRepository.findById(del1.getDeliveryId())).thenReturn(Optional.of(del1));
        //given
        DeliveryRequest.UpdateDeliveryStateRequest req = new DeliveryRequest.UpdateDeliveryStateRequest(
            del1.getDeliveryId(),
            DeliveryState.PRE_COLLECT
        );
        //then
        assertThrows(GeneralException.class, () -> deliveryService.updateDeliveryState(req));
    }
    @DisplayName("존재하지 않는 배송 ID 상태 수정 실패")
    @Test
    void FAIL_updateDeliveryStatus_invalid_deliveryId() {
        //given
        when(deliveryRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        
        //when
        UUID randomId = UUID.randomUUID();
        DeliveryRequest.UpdateDeliveryStateRequest req = new DeliveryRequest.UpdateDeliveryStateRequest(
            randomId,
            DeliveryState.DELIVERING
        );
        
        //then
        assertThrows(GeneralException.class, () -> deliveryService.updateDeliveryState(req));
    }
    @DisplayName("존재하지 않는 배송 ID 송장 번호 등록 실패 ")
    @Test
    void FAIL_registerTrackingNumber_invalid_deliveryId() {
        //given 
        when(deliveryRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        //when
        DeliveryRequest.RegisterTrackingCodeRequest req = new DeliveryRequest.RegisterTrackingCodeRequest(
            UUID.randomUUID(),
            "1234567890"
        );
        //then
        assertThrows(GeneralException.class, () -> deliveryService.registerTrackingCode(req));
    }

    //todo: 송장 번호 검증 테스트 추가
}