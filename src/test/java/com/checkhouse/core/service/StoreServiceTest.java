package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.StoreDTO;
import com.checkhouse.core.entity.Address;
import com.checkhouse.core.entity.Store;
import com.checkhouse.core.repository.mysql.AddressRepository;
import com.checkhouse.core.repository.mysql.StoreRepository;
import com.checkhouse.core.dto.request.StoreRequest;
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
public class StoreServiceTest {
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private AddressRepository addressRepository;

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
        StoreRequest.AddStoreRequest req = new StoreRequest.AddStoreRequest(
                store1.getStoreId(),
                store1addr,
                "정보섬티맥스",
                "SSUBEST"

        );

        // given
        when(storeRepository.save(any(Store.class))).thenReturn(store1);
        when(storeRepository.findStoreByName(store1.getName())).thenReturn(Optional.empty());
        when(addressRepository.findById(store1addr.getAddressId())).thenReturn(Optional.of(store1addr));

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
        // given
        when(storeRepository.findAll()).thenReturn(List.of(store1, store2));

        // when
        List<StoreDTO> stores = storeService.getStores();

        // then
        assertNotNull(stores);
        assertEquals(2, stores.size());
        verify(storeRepository, times(1)).findAll();
    }

    @DisplayName("특정 스토어 조회 성공")
    @Test
    void SUCCESS_getStoreDetails() {
        // 스토어 정보
        StoreRequest.GetStoreRequest req = new StoreRequest.GetStoreRequest(
                store1.getStoreId()
        );

        // given
        when(storeRepository.findById(store1.getStoreId())).thenReturn(Optional.of(store1));

        // when
        StoreDTO result = storeService.getStore(req);

        // then
        assertNotNull(result);
        assertEquals("정보섬티맥스", result.name());
        assertEquals("SSUBEST", result.code());
        verify(storeRepository, times(1)).findById(any());
    }

    @DisplayName("스토어 삭제 성공")
    @Test
    void SUCCESS_deleteStore() {
        // 스토어 정보
        UUID storeId = store1.getStoreId();
        StoreRequest.DeleteStoreRequest req = new StoreRequest.DeleteStoreRequest(
                storeId
        );

        // given
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store1));

        // when
        storeService.deleteStore(req);

        // then
        verify(storeRepository, times(1)).findById(storeId);
    }

    @DisplayName("스토어 정보 수정 성공")
    @Test
    void SUCCESS_updateStoreName(){
        // 스토어 정보
        StoreRequest.UpdateStoreRequest req = new StoreRequest.UpdateStoreRequest(
                store1.getStoreId(),
                store2addr,
                "정보섬1층"
        );

        // given
        when(storeRepository.findById(store1.getStoreId())).thenReturn(Optional.of(store1));
        when(addressRepository.findById(store2addr.getAddressId())).thenReturn(Optional.of(store2addr));
        when(storeRepository.findStoreByName(req.name())).thenReturn(Optional.empty());

        // when
        StoreDTO result = storeService.updateStore(req);

        // then
        assertNotNull(result);
        assertEquals("정보섬1층", result.name());
        assertEquals("SSUBEST", result.code());
        assertEquals(store2addr.getAddressId(), result.addressDTO().addressId());
        verify(storeRepository, times(1)).findById(store1.getStoreId());
        verify(storeRepository, times(1)).findStoreByName(any());
        verify(storeRepository, never()).save(any(Store.class));
    }

    @DisplayName("스토어 코드 수정 성공")
    @Test
    void SUCCESS_updateStoreCode(){
        // 스토어 정보
        StoreRequest.UpdateStoreCodeRequest req = new StoreRequest.UpdateStoreCodeRequest(
                store1.getStoreId(),
                "SSUISBEST"
        );

        // given
        when(storeRepository.findById(store1.getStoreId())).thenReturn(Optional.of(store1));

        // when
        StoreDTO result = storeService.updateStoreCode(req);

        // then
        assertNotNull(result);
        assertEquals("정보섬티맥스", result.name());
        assertEquals("SSUISBEST", result.code());
        assertEquals(store1addr.getAddressId(), result.addressDTO().addressId());
        verify(storeRepository, times(1)).findById(store1.getStoreId());
        verify(storeRepository, never()).save(any(Store.class));
    }

    @DisplayName("스토어 코드 확인 성공")
    @Test
    void SUCCESS_verifyStoreCode() {
        // 스토어 정보
        StoreRequest.VerifyCodeRequest req = new StoreRequest.VerifyCodeRequest(
                store1.getStoreId(),
                "SSUBEST"
        );

        // given
        when(storeRepository.findById(store1.getStoreId())).thenReturn(Optional.of(store1));

        // when
        boolean result = storeService.verifyCode(req);

        // then
        assertTrue(result);
        verify(storeRepository, times(1)).findById(store1.getStoreId());
    }

    @DisplayName("잘못된 주소일 때 스토어 저장 실패")
    @Test
    void FAIL_addStore_invalid_address() {
        // 스토어 정보
        StoreRequest.AddStoreRequest req = new StoreRequest.AddStoreRequest(
                store1.getStoreId(),
                store1addr,
                "정보섬티맥스",
                "SSUBEST"

        );

        // given
        when(storeRepository.findStoreByName(store1.getName())).thenReturn(Optional.empty());
        when(addressRepository.findById(store1addr.getAddressId())).thenReturn(Optional.empty());

        // when, then
        GeneralException exception = assertThrows(GeneralException.class, () -> storeService.addStore(req));

        assertEquals(ErrorStatus._ADDRESS_ID_NOT_FOUND, exception.getCode());
        verify(storeRepository, times(1)).findStoreByName(any());
        verify(addressRepository, times(1)).findById(any());
    }

    @DisplayName("잘못된 이름의 경우 스토어 정보 수정 실패")
    @Test
    void FAIL_updateStore_name() {
        //FIXME: 무슨뜻일까?
    }
    @DisplayName("잘못된 주소의 경우 스토어 정보 수정 실패")
    @Test
    void FAIL_updateStore_invalid_address() {
        // 스토어 정보
        StoreRequest.UpdateStoreRequest req = new StoreRequest.UpdateStoreRequest(
                store1.getStoreId(),
                store1addr,
                "test"
        );

        // given
        when(storeRepository.findById(store1.getStoreId())).thenReturn(Optional.of(store1));
        when(addressRepository.findById(store1addr.getAddressId())).thenReturn(Optional.empty());

        // when, then
        GeneralException exception = assertThrows(GeneralException.class, () -> storeService.updateStore(req));

        assertEquals(ErrorStatus._ADDRESS_ID_NOT_FOUND, exception.getCode());
        verify(storeRepository, times(1)).findById(any());
        verify(addressRepository, times(1)).findById(any());
    }

    @DisplayName("존재하지 않는 스토어의 경우 스토어 정보 수정 실패")
    @Test
    void FAIL_updateStore_not_found() {
        // 스토어 정보
        UUID invalidId = UUID.randomUUID();
        StoreRequest.UpdateStoreRequest req = new StoreRequest.UpdateStoreRequest(
                invalidId,
                store2addr,
                "정보섬1층"
        );

        // given
        when(storeRepository.findById(invalidId)).thenReturn(Optional.empty());

        // when, then
        GeneralException exception = assertThrows(GeneralException.class, () -> storeService.updateStore(req));

        assertEquals(ErrorStatus._STORE_ID_NOT_FOUND, exception.getCode());
        verify(storeRepository, times(1)).findById(any());
        verify(storeRepository, never()).save(any(Store.class));
    }
    @DisplayName("중복된 스토어가 있을 경우 스토어 정보 수정 실패")
    @Test
    void FAIL_updateStore_already_exist() {
        // 스토어 정보
        StoreRequest.UpdateStoreRequest req = new StoreRequest.UpdateStoreRequest(
                store1.getStoreId(),
                store1addr,
                "숭실대 CU"
        );

        // given
        when(storeRepository.findById(store1.getStoreId())).thenReturn(Optional.of(store1));
        when(storeRepository.findStoreByName("숭실대 CU")).thenReturn(Optional.of(store2));

        // when, then
        GeneralException exception = assertThrows(GeneralException.class, () -> storeService.updateStore(req));

        assertEquals(ErrorStatus._STORE_ALREADY_EXISTS, exception.getCode());
        verify(storeRepository, times(1)).findById(any());
        verify(storeRepository, times(1)).findStoreByName(any());
        verify(storeRepository, never()).save(any(Store.class));
    }
    @DisplayName("존재하지 않는 스토어의 경우 스토어 코드 수정 실패")
    @Test
    void FAIL_updateStoreCode_not_found() {
        // 스토어 정보
        UUID invalidId = UUID.randomUUID();
        StoreRequest.UpdateStoreCodeRequest req = new StoreRequest.UpdateStoreCodeRequest(
                invalidId,
                "code"
        );

        // given
        when(storeRepository.findById(invalidId)).thenReturn(Optional.empty());

        // when, then
        GeneralException exception = assertThrows(GeneralException.class, () -> storeService.updateStoreCode(req));

        assertEquals(ErrorStatus._STORE_ID_NOT_FOUND, exception.getCode());
        verify(storeRepository, times(1)).findById(any());
        verify(storeRepository, never()).save(any(Store.class));
    }


    @DisplayName("존재하지 않는 스토어의 경우 스토어 삭제 실패")
    @Test
    void FAIL_deleteStore_not_found() {
        // 스토어 정보
        UUID invalidId = UUID.randomUUID();
        StoreRequest.DeleteStoreRequest req = new StoreRequest.DeleteStoreRequest(
                invalidId
        );

        // given
        when(storeRepository.findById(invalidId)).thenReturn(Optional.empty());

        // when, then
        GeneralException exception = assertThrows(GeneralException.class, () -> storeService.deleteStore(req));

        assertEquals(ErrorStatus._STORE_ID_NOT_FOUND, exception.getCode());
        verify(storeRepository, times(1)).findById(any());
    }

    @DisplayName("존재하지 않는 스토어의 경우 특정 스토어 조회 실패")
    @Test
    void FAIL_getStoreDetails_not_found() {
        // 스토어 정보
        UUID invalidId = UUID.randomUUID();
        StoreRequest.GetStoreRequest req = new StoreRequest.GetStoreRequest(
                invalidId
        );

        // given
        when(storeRepository.findById(invalidId)).thenReturn(Optional.empty());

        // when, then
        GeneralException exception = assertThrows(GeneralException.class, () -> storeService.getStore(req));

        assertEquals(ErrorStatus._STORE_ID_NOT_FOUND, exception.getCode());
        verify(storeRepository, times(1)).findById(any());
    }

    @DisplayName("스토어 코드와 입력된 코드가 일치하지 않는 경우 실패")
    @Test
    void FAIL_verifyStoreCode_mismatch() {
        // 스토어 정보
        StoreRequest.VerifyCodeRequest req = new StoreRequest.VerifyCodeRequest(
                store1.getStoreId(),
                "SSUISNTBEST"
        );

        // given
        when(storeRepository.findById(store1.getStoreId())).thenReturn(Optional.of(store1));

        // when
        // FIXME: 코드 실패시 Exeption? return false?
        boolean result = storeService.verifyCode(req);

        // then
        assertFalse(result);
        verify(storeRepository, times(1)).findById(store1.getStoreId());
    }


}
