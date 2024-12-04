package com.checkhouse.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.UUID;
import java.util.Optional;

import com.checkhouse.core.repository.mysql.AddressRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.CollectDTO;
import com.checkhouse.core.dto.request.CollectRequest;
import com.checkhouse.core.dto.request.CollectRequest.AddCollectRequest;
import com.checkhouse.core.dto.request.CollectRequest.UpdateCollectRequest;
import com.checkhouse.core.entity.Collect;
import com.checkhouse.core.entity.Delivery;
import com.checkhouse.core.entity.UsedProduct;
import com.checkhouse.core.entity.enums.DeliveryState;
import com.checkhouse.core.entity.enums.Role;
import com.checkhouse.core.repository.mysql.CollectRepository;
import com.checkhouse.core.repository.mysql.DeliveryRepository;
import com.checkhouse.core.repository.mysql.UsedProductRepository;
import com.checkhouse.core.entity.User;
import com.checkhouse.core.entity.OriginProduct;
import com.checkhouse.core.entity.Address;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class CollectServiceTest {

    @Mock
    private CollectRepository collectRepository;
    @Mock
    private DeliveryRepository deliveryRepository;
    @Mock
    private UsedProductRepository usedProductRepository;
    @Mock
    private AddressRepository addressRepository;
    @InjectMocks
    private CollectService collectService;

    

    private static Collect collect1;
    private static UsedProduct usedProduct1;
    private static UsedProduct usedProduct2;
    private static Delivery delivery1;
    private static Delivery delivery2;
    private static User user;
    private static OriginProduct originProduct;

    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {
        user = User.builder()
            .userId(UUID.randomUUID())
            .nickname("testUser")
            .email("test@test.com")
            .password("test1234")
            .providerId("test1234")
            .isActive(true)
            .build();

        originProduct = OriginProduct.builder()
            .id(UUID.randomUUID())
            .build();

        usedProduct1 = UsedProduct.builder()
            .usedProductId(UUID.randomUUID())
            .user(user)
            .originProduct(originProduct)
            .build();
        
        delivery1 = Delivery.builder()
            .deliveryId(UUID.randomUUID())
            .address(Address.builder()
                    .addressId(UUID.randomUUID())
                .zipcode("12345")
                .address("서울시 강남구")
                .build())
            .build();

        collect1 = Collect.builder()
            .collectId(UUID.randomUUID())
            .usedProduct(usedProduct1)
            .delivery(delivery1)
            .state(DeliveryState.PRE_COLLECT)
            .build();

        usedProduct2 = UsedProduct.builder()
            .usedProductId(UUID.randomUUID())
            .user(user)
            .originProduct(originProduct)
            .build();
        
        delivery2 = Delivery.builder()
            .deliveryId(UUID.randomUUID())
            .address(Address.builder()
                .zipcode("54321")
                .address("서울시 서초구")
                .build())
            .build();
    }
    
    @DisplayName("수거 등록 성공")
    @Test
    void SUCCESS_addCollect() {
        // given
        AddCollectRequest req = AddCollectRequest.builder()
            .usedProductId(usedProduct1.getUsedProductId())
                .addressId(delivery1.getAddress().getAddressId())
            .build();

        Delivery updatedDelivery = Delivery.builder()
            .deliveryId(delivery1.getDeliveryId())
            .address(delivery1.getAddress())
            .deliveryState(DeliveryState.PRE_COLLECT)
            .build();

        when(usedProductRepository.findById(any(UUID.class))).thenReturn(Optional.of(usedProduct1));
        when(collectRepository.save(any(Collect.class))).thenReturn(collect1);
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(updatedDelivery);
        when(addressRepository.findById(any(UUID.class))).thenReturn(Optional.of(delivery1.getAddress()));

        // when
        CollectDTO collectDTO = collectService.addCollect(req);

        // then
        assertEquals(collect1.getCollectId(), collectDTO.collectId());
        verify(collectRepository).save(any(Collect.class));
        verify(deliveryRepository).save(any(Delivery.class));
    }

    @DisplayName("수거 상태 수정")
    @Test
    void SUCCESS_updateCollectState() {
        // given
        UUID collectId = collect1.getCollectId();
        DeliveryState newState = DeliveryState.PRE_COLLECT;
        
        UpdateCollectRequest req = UpdateCollectRequest.builder()
            .collectId(collectId)
            .deliveryState(newState)
            .build();
        
        Collect updatedCollect = Collect.builder()
            .collectId(collectId)
            .state(newState)
            .usedProduct(usedProduct1)
            .delivery(delivery1)
            .build();
        
        when(collectRepository.findById(collectId)).thenReturn(Optional.of(collect1));
        when(collectRepository.save(any(Collect.class))).thenReturn(updatedCollect);

        // when
        CollectDTO collectDTO = collectService.updateCollect(req);

        // then
        assertEquals(newState, collectDTO.state());
        verify(collectRepository).findById(collectId);
        verify(collectRepository).save(any(Collect.class));
    }

    @DisplayName("존재하지 않는 배송일 경우 등록 실패")
    @Test
    void FAIL_addCollect_not_found() {
        // given
        AddCollectRequest req = AddCollectRequest.builder()
            .usedProductId(UUID.randomUUID())
            .build();

        when(usedProductRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // when & then
        assertThrows(GeneralException.class, () -> collectService.addCollect(req));
    }

    @DisplayName("존재하지 않는 중고 상품의 경우 등록 실패")
    @Test
    void FAIL_addCollect_not_found_used_product() {
        // given
        AddCollectRequest req = AddCollectRequest.builder()
            .usedProductId(UUID.randomUUID())
            .build();

        when(usedProductRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // when & then
        assertThrows(GeneralException.class, () -> collectService.addCollect(req));
    }

    @DisplayName("존재하지 않는 수거 상태로 수정하는 경우 실패")
    @Test
    void FAIL_updateCollectState_invalid_status() {
        // given
        UUID collectId = collect1.getCollectId();
        DeliveryState invalidState = null;
        
        UpdateCollectRequest req = new UpdateCollectRequest(
            collectId,
            invalidState
        );

        // when & then
        assertThrows(GeneralException.class, 
            () -> collectService.updateCollect(req));
    }

    @DisplayName("이미 수거 등록이 된 상품은 수거 등록 실패")
    @Test
    void FAIL_addCollect_already_collected() {
        // given
        AddCollectRequest req = AddCollectRequest.builder()
            .usedProductId(usedProduct1.getUsedProductId())
            .build();

        when(usedProductRepository.findById(any(UUID.class))).thenReturn(Optional.of(usedProduct1));
        when(collectRepository.findByUsedProduct(usedProduct1)).thenReturn(Optional.of(collect1));

        // when & then
        assertThrows(GeneralException.class, () -> collectService.addCollect(req));
        verify(collectRepository).findByUsedProduct(usedProduct1);
    }

    @DisplayName("중고 상품으로 수거 조회")
    @Test
    void SUCCESS_getCollectByUsedProduct() {
        // given
        when(usedProductRepository.findById(any(UUID.class))).thenReturn(Optional.of(usedProduct1));
        when(collectRepository.findByUsedProduct(any(UsedProduct.class))).thenReturn(Optional.of(collect1));

        // when
        CollectRequest.GetCollectByUsedProductRequest req = new CollectRequest.GetCollectByUsedProductRequest(usedProduct1.getUsedProductId());
        CollectDTO result = collectService.findByUsedProduct(req);

        // then
        assertEquals(collect1.getCollectId(), result.collectId());
        verify(collectRepository).findByUsedProduct(usedProduct1);
    }
}
