package com.checkhouse.core.service;

import com.checkhouse.core.dto.StoreDTO;
import com.checkhouse.core.entity.Address;
import com.checkhouse.core.entity.Store;
import com.checkhouse.core.repository.mysql.StoreRepository;
import com.checkhouse.core.request.StoreRequest;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class StoreServiceTest {
    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreService storeService;

    private Address store1addr;
    private Address store2addr;

    private Store store1;
    private Store store2;

    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {
        store1addr = Address.builder()
            .addressId(UUID.randomUUID())
            .name("홍길동")
            .address("서울특별시 동작구 상도로 369")
            .zipcode(6978)
            .phone("01012345678")
            .addressDetail("정보과학관 지하 1층")
            .build();
        store2addr = Address.builder()
                .addressId(UUID.randomUUID())
                .name("김길동")
                .address("서울특별시 동작구 상도로 45길 12")
                .zipcode(12345)
                .phone("01097063979")
                .addressDetail("12345호")
                .build();

        store1 = Store.builder()
                .storeId(UUID.randomUUID())
                .address(store1addr)
                .name("정보섬티맥스")
                .code("SSUBEST")
                .build();
        store2 = Store.builder()
                .storeId(UUID.randomUUID())
                .address(store2addr)
                .name("숭실대 CU")
                .code("SMALLCU")
                .build();
    }

    @DisplayName("스토어 저장 성공")
    @Test
    void SUCCESS_addStore() {
        // 스토어 정보
        StoreRequest.AddStoreRequest req = StoreRequest.AddStoreRequest
                .builder()
                .storeId(store1.getStoreId())
                .name("정보섬티맥스")
                .code("SSUBEST")
                .address(store1addr)
                .build();

        // given
        when(storeRepository.save(any(Store.class))).thenReturn(store1);

        // when
        StoreDTO result = storeService.addStore(req);

        // then
        assertNotNull(result);
        assertEquals("정보섬티맥스", result.name());
        assertEquals("SSUBEST", result.code());
        assertEquals(store1addr.getAddressId(), result.addressDTO().addressId());;
        verify(storeRepository, times(1)).findStoreByName("정보섬티맥스");
        verify(storeRepository, times(1)).save(any(Store.class));
    }

    @DisplayName("스토어 리스트 조회 성공")
    @Test
    void SUCCESS_getStoreList() {
        // 스토어 정보

        // given

        // when

        // then
    }

    @DisplayName("특정 스토어 조회 성공")
    @Test
    void SUCCESS_getStoreDetails() {
        // 스토어 정보

        // given

        // when

        // then
    }

    @DisplayName("스토어 삭제 성공")
    @Test
    void SUCCESS_deleteStore() {
        // 스토어 정보

        // given

        // when

        // then
    }

    @DisplayName("스토어 정보 수정 성공")
    @Test
    void SUCCESS_updateStoreName(){
        // 스토어 정보

        // given

        // when

        // then
    }

    @DisplayName("스토어 코드 수정 성공")
    @Test
    void SUCCESS_updateStoreCode(){
        // 스토어 정보

        // given

        // when

        // then
    }

    @DisplayName("스토어 코드 확인 성공")
    @Test
    void SUCCESS_verifyStoreCode() {
        // 스토어 정보

        // given

        // when

        // then
    }

    @DisplayName("잘못된 주소일 때 스토어 저장 실패")
    @Test
    void FAIL_addStore_invalid_address() {}

    @DisplayName("잘못된 이름의 경우 스토어 수정 실패")
    @Test
    void FAIL_updateStore_name() {}

    @DisplayName("잘못된 코드의 경우 스토어 수정 실패")
    @Test
    void FAIL_updateStore_code() {}

    @DisplayName("존재하지 않는 스토어의 경우 스토어 삭제 실패")
    @Test
    void FAIL_deleteStore_not_found() {}

    @DisplayName("존재하지 않는 스토어의 경우 특정 스토어 조회 실패")
    @Test
    void FAIL_getStoreDetails_not_found() {}

    @DisplayName("스토어 코드와 입력된 코드가 일치하지 않는 경우 실패")
    @Test
    void FAIL_verifyStoreCode_mismatch() {}


}
