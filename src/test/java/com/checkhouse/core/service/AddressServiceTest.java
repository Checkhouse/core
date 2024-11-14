package com.checkhouse.core.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AddressServiceTest {
    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {}

    @DisplayName("주소 등록 성공")
    @Test
    void SUCCESS_addAddress() {}

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
