package com.checkhouse.core.service;

import com.checkhouse.core.entity.Address;
import com.checkhouse.core.repository.mysql.AddressRepository;
import com.checkhouse.core.request.AddressRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static reactor.core.publisher.Mono.when;

public class AddressServiceTest {

    @InjectMocks
    private AddressService addressService;

    private Address commonAddress;
    private Address otherAddress;
    private Address noDetailsAddress;

    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {
        commonAddress = Address.builder()
                .name("한글이름")
                .address("서울특별시 동작구 상도로 369")
                .zipcode(6978)
                .phone("01097063979")
                .addressDetail("정보과학관 지하 1층")
                .build();
    }

    @DisplayName("주소 등록 성공")
    @Test
    void SUCCESS_addAddress() {
        AddressRequest.AddAddressRequest req = AddressRequest.AddAddressRequest.builder()
                .name("장범식")
                .address("서울특별시 동작구 상도로 369")
                .zipcode(6978)
                .phone("01012345678")
                .build();

        when(AddressRepository.findAddressById(any())).thenReturn(Optional.empty());
    }

    @DisplayName("주소 수정 성공")
    @Test
    void SUCCESS_updateAddress() {}

    @DisplayName("주소 삭제 성공")
    @Test
    void SUCCESS_deleteAddress() {}

    @DisplayName("주소 조회 성공")
    @Test
    void SUCCESS_getAddress() {}

    @DisplayName("주소 리스트 조회 성공")
    @Test
    void SUCCESS_getAddressList() {}


    @DisplayName("존재하지 않는 주소의 경우 조회 실패")
    @Test
    void FAIL_getAddress_not_found() {}

    @DisplayName("존재하지 않는 주소의 경우 수정 실패")
    @Test
    void FAIL_updateAddress_not_found() {}

    @DisplayName("존재하지 않는 주소의 경우 삭제 실패")
    @Test
    void FAIL_deleteAddress_not_found() {}
}
