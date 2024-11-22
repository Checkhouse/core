package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.HubDTO;
import com.checkhouse.core.entity.Address;
import com.checkhouse.core.entity.Hub;
import com.checkhouse.core.entity.Stock;
import com.checkhouse.core.entity.UsedProduct;
import com.checkhouse.core.entity.enums.UsedProductState;
import com.checkhouse.core.repository.mysql.AddressRepository;
import com.checkhouse.core.repository.mysql.HubRepository;
import com.checkhouse.core.dto.request.HubRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HubServiceTest {
    @Mock
    private HubRepository hubRepository;
    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private HubService hubService;

    private Address hub1addr;
    private Address hub2addr;

    private Hub hub1;
    private Hub hub2;

    private UsedProduct usedProduct1;
    private Stock stock;

    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {
        hub1addr = Address.builder()
                .addressId(UUID.randomUUID())
                .name("홍길동")
                .address("서울특별시 동작구 상도로 369")
                .zipcode(6978)
                .phone("01012345678")
                .addressDetail("정보과학관 지하 1층")
                .build();
        hub2addr = Address.builder()
                .addressId(UUID.randomUUID())
                .name("김길동")
                .address("서울특별시 동작구 상도로 45길 12")
                .zipcode(12345)
                .phone("01097063979")
                .addressDetail("12345호")
                .build();

        hub1 = Hub.builder()
                .hubId(UUID.randomUUID())
                .address(hub1addr)
                .name("허브1")
                .clusteredId(1)
                .build();
        hub2 = Hub.builder()
                .hubId(UUID.randomUUID())
                .address(hub2addr)
                .name("허브2")
                .clusteredId(2)
                .build();
        stock = Stock.builder()
                .stockId(UUID.randomUUID())
                .usedProductId(UUID.randomUUID())
                .hub(hub1)
                .area("A-1")
                .build();
    }

    @Test
    @DisplayName("허브 추가 성공")
    void SUCCESS_addHub() {
        // 데이터 생성
        HubRequest.AddHubRequest req = new HubRequest.AddHubRequest(
                hub1.getHubId(),
                hub1addr,
                "허브1",
                1
        );

        // Given
        when(hubRepository.findHubByName("허브1")).thenReturn(Optional.empty());
        when(hubRepository.findHubByClusteredId(1)).thenReturn(Optional.empty());
        when(hubRepository.save(any(Hub.class))).thenReturn(hub1);
        when(addressRepository.findById(hub1addr.getAddressId())).thenReturn(Optional.of(hub1addr));

        // When
        HubDTO result = hubService.addHub(req);

        // Then
        assertNotNull(result);
        assertEquals(result.name(), "허브1");
        assertEquals(result.clusteredId(), 1);
        verify(hubRepository, times(1)).findHubByName(any());
        verify(hubRepository, times(1)).findHubByClusteredId(1);
        verify(hubRepository, times(1)).save(any(Hub.class));
    }

    @Test
    @DisplayName("허브 정보 업데이트 성공")
    void SUCCESS_updateHub() {
        // 데이터 생성
        HubRequest.UpdateHubRequest req = new HubRequest.UpdateHubRequest(
                hub1.getHubId(),
                hub1addr,
                "허브1234",
                5
        );

        // Given
        when(hubRepository.findById(hub1.getHubId())).thenReturn(Optional.of(hub1));
        when(hubRepository.findHubByName("허브1234")).thenReturn(Optional.empty());
        when(hubRepository.findHubByClusteredId(5)).thenReturn(Optional.empty());
        when(addressRepository.findById(hub1addr.getAddressId())).thenReturn(Optional.of(hub1addr));

        // When
        HubDTO result = hubService.updateHub(req);

        // Then
        assertNotNull(result);
        assertEquals(result.name(), "허브1234");
        assertEquals(result.clusteredId(), 5);
        verify(hubRepository, times(1)).findById(hub1.getHubId());
        verify(hubRepository, times(1)).findHubByName(any());
        verify(hubRepository, times(1)).findHubByClusteredId(5);
        verify(hubRepository, never()).save(any(Hub.class));
    }

    @Test
    @DisplayName("허브 삭제 성공")
    void SUCCESS_deleteHub() {
        // 스토어 정보
        UUID hubId = hub1.getHubId();
        HubRequest.DeleteHubRequest req = new HubRequest.DeleteHubRequest(
                hubId
        );

        // given
        when(hubRepository.findById(hubId)).thenReturn(Optional.of(hub1));

        // when
        hubService.deleteHub(req);

        // then
        verify(hubRepository, times(1)).findById(hubId);
    }

    /**
     * 입력된 주소에 해당하는 허브를 배정
     */
    @DisplayName("허브 구역 할당 성공")
    @Test
    void SUCCESS_allocateHub() {
        // data
        HubRequest.AllocateHubRequest req = new HubRequest.AllocateHubRequest(
                hub2addr
        );
        // given


        // when

        // then
    }
    @DisplayName("허브 리스트 조회 성공")
    @Test
    void SUCCESS_getHubList() {
        // given
        when(hubRepository.findAll()).thenReturn(List.of(hub1, hub2));

        // when
        List<HubDTO> result = hubService.getHubs();

        // then
        assertNotNull(result);
        assertEquals(result.size(), 2);
        verify(hubRepository, times(1)).findAll();
    }

    /**
     * 허브 저장이 실패하는 경우는 다음과 같음
     * 1. 이미 존재하는 허브
     * 2. 잘못된 주소
     * 3. 잘못된 존 매핑
     *
     */
    @DisplayName("이미 존재하는 경우 허브 저장 실패")
    @Test
    void FAIL_addHub_already_exist() {
        // 데이터 생성
        HubRequest.AddHubRequest req = new HubRequest.AddHubRequest(
                hub1.getHubId(),
                hub1addr,
                "허브2",
                1
        );

        // Given
        when(hubRepository.findHubByName("허브2")).thenReturn(Optional.of(hub2));

        // When
        GeneralException exception = assertThrows(GeneralException.class, () -> hubService.addHub(req));

        // Then
        assertEquals(ErrorStatus._HUB_ALREADY_EXISTS, exception.getCode());
        verify(hubRepository, times(1)).findHubByName(any());

    }
    @DisplayName("잘못된 주소의 경우 허브 저장 실패")
    @Test
    void FAIL_addHub_invalid_address() {
        // 데이터 생성
        HubRequest.AddHubRequest req = new HubRequest.AddHubRequest(
                UUID.randomUUID(),
                hub1addr,
                "허브1",
                1
        );

        // Given
        when(hubRepository.findHubByName(hub1.getName())).thenReturn(Optional.empty());
        when(hubRepository.findHubByClusteredId(1)).thenReturn(Optional.empty());
        when(addressRepository.findById(hub1addr.getAddressId())).thenReturn(Optional.empty());

        // When
        GeneralException exception = assertThrows(GeneralException.class, () -> hubService.addHub(req));

        // Then
        assertEquals(ErrorStatus._ADDRESS_ID_NOT_FOUND, exception.getCode());
    }

    @DisplayName("잘못된 존 매핑의 경우 허브 저장 실패")
    @Test
    void FAIL_addHub_invalid_zone() {
        // 허브 정보가 주어짐

        // 허브 저장

        // 저장된 허브 정보 확인
    }

    /**
     * 허브 정보 수정이 실패하는 경우는 다음과 같음
     * 1. 존재하지 않는 허브
     * 2. 잘못된 주소 수정
     * 3. 존재하지 않는 존 ID
     */
    @DisplayName("존재하지 않는 허브의 경우 허브 정보 수정 실패")
    @Test
    void FAIL_updateHub_not_found_hub() {
        // 데이터 생성
        UUID invalidHubId = UUID.randomUUID();
        HubRequest.UpdateHubRequest req = new HubRequest.UpdateHubRequest(
                invalidHubId,
                hub1addr,
                "허브1",
                1
        );

        // Given
        when(hubRepository.findById(invalidHubId)).thenReturn(Optional.empty());

        // When
        GeneralException exception = assertThrows(GeneralException.class, () -> hubService.updateHub(req));

        // Then
        assertEquals(ErrorStatus._HUB_ID_NOT_FOUND, exception.getCode());
        verify(hubRepository, times(1)).findById(any());
    }

    @DisplayName("이미 존재하는 이름의 경우 허브 정보 수정 실패")
    @Test
    void FAIL_updateHub_exist_hub() {
        // 데이터 생성
        HubRequest.UpdateHubRequest req = new HubRequest.UpdateHubRequest(
                hub1.getHubId(),
                hub1addr,
                "허브2",
                1
        );

        // Given
        when(hubRepository.findById(hub1.getHubId())).thenReturn(Optional.of(hub1));
        when(hubRepository.findHubByName("허브2")).thenReturn(Optional.of(hub2));

        // When
        GeneralException exception = assertThrows(GeneralException.class, () -> hubService.updateHub(req));

        // Then
        assertEquals(ErrorStatus._HUB_ALREADY_EXISTS, exception.getCode());
        verify(hubRepository, times(1)).findById(any());
        verify(hubRepository, times(1)).findHubByName(any());
    }

    @DisplayName("잘못된 주소의 경우 허브 정보 수정 실패")
    @Test
    void FAIL_updateHub_invalid_address() {
        // 데이터 생성
        HubRequest.UpdateHubRequest req = new HubRequest.UpdateHubRequest(
                hub1.getHubId(),
                hub2addr,
                "허브1",
                1
        );

        // Given
        when(hubRepository.findById(hub1.getHubId())).thenReturn(Optional.of(hub1));
        when(addressRepository.findById(hub2addr.getAddressId())).thenReturn(Optional.empty());

        // When
        GeneralException exception = assertThrows(GeneralException.class, () -> hubService.updateHub(req));

        // Then
        assertEquals(ErrorStatus._ADDRESS_ID_NOT_FOUND, exception.getCode());
    }

    @DisplayName("수정한 담당 존이 존재하지 않는 경우 정보 수정 실패")
    @Test
    void FAIL_updateHub_invalid_zone() {

    }

    /**
     * 허브 삭제에 실패하는 경우는 다음과 같음
     * 1. 존재하지 않는 허브
     */
    @DisplayName("존재하지 않는 허브의 경우 허브 삭제 실패")
    @Test
    void FAIL_deleteHub_not_found_hub() {
        // 스토어 정보
        UUID invalidHub = UUID.randomUUID();
        HubRequest.DeleteHubRequest req = new HubRequest.DeleteHubRequest(
                invalidHub
        );

        // given
        when(hubRepository.findById(invalidHub)).thenReturn(Optional.empty());

        // when
        GeneralException exception = assertThrows(GeneralException.class, () -> hubService.deleteHub(req));

        // Then
        assertEquals(ErrorStatus._HUB_ID_NOT_FOUND, exception.getCode());
        verify(hubRepository, times(1)).findById(invalidHub);

    }    /**
     * 허브 할당에 실패하는 경우는 다음과 같음
     * 1. 잘못된 주소
     * 2. 매핑할 허브가 없는 경우
     */
    @DisplayName("잘못된 주소의 경우 허브 할당 실패")
    @Test
    void FAIL_allocHub_invalid_address() {}

    @DisplayName("매핑할 허브가 없는 주소의 경우 실패")
    @Test
    void FAIL_allocHub_not_found_zone() {}



    /* -------------------- STOCK TEST -------------------- */
    @DisplayName("허브에 상품 추가 성공")
    @Test
    void SUCCESS_addStock() {

    }
    @DisplayName("상품 ID로 조회 성공")
    @Test
    void SUCCESS_getStockByUsedProductId() {

    }
    @DisplayName("허브에 상품 위치 수정 성공")
    @Test
    void SUCCESS_updateStockArea() {

    }
    @DisplayName("허브에 상품 삭제 성공")
    @Test
    void SUCCESS_deleteStock() {

    }
    @DisplayName("허브의 상품 리스트 성공")
    @Test
    void SUCCESS_getStocksByHubId() {

    }
    @DisplayName("허브의 위치의 상품 리스트 성공")
    @Test
    void SUCCESS_getStocksByArea() {

    }
    //Fail
    @DisplayName("재고 추가 실패: 허브가 존재하지 않음")
    @Test
    void FAIL_addStock_not_found_hub() {

    }
    @DisplayName("재고 수정 실패: 재고가 존재하지 않음")
    @Test
    void FAIL_updateStock_not_found() {

    }
    @DisplayName("재고 조회 실패: 재고가 존재하지 않음")
    @Test
    void FAIL_getStock_not_found() {

    }
    @DisplayName("재고 삭제 실패: 재고가 존재하지 않음")
    @Test
    void FAIL_deleteStock_not_found() {

    }
    @DisplayName("허브 재고 리스트 실패: 허브가 존재하지 않음")
    @Test
    void FAIL_getStocksByHub_not_found_hub() {

    }
    @DisplayName("허브 위치 재고 리스트 실패: 허브가 존재하지 않음")
    @Test
    void FAIL_getStocksByArea_not_found_hub() {

    }
    @DisplayName("허브 위치 재고 리스트 실패: 존재하지 않는 위치")
    @Test
    void FAIL_getStocksByArea_not_found_area() {

    }
}
