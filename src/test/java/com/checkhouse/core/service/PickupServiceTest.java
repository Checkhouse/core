package com.checkhouse.core.service;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PickupServiceTest {
    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {}

    @DisplayName("픽업 생성")
    @Test
    void SUCCESS_addUserPickupList() {}

    @DisplayName("사용자의 픽업 리스트 조회 성공")
    @Test
    void SUCCESS_getUserPickupList() {}

    @DisplayName("사용자의 특정 픽업 정보 조회 성공")
    @Test
    void SUCCESS_getUserPickupDetails() {}

    @DisplayName("픽업 확인 - 스토어 관리자가 qr 찍은 후")
    @Test
    void SUCCESS_verifyPickupWithQR() {}

    @DisplayName("픽업 확인 - 관리자 id 입력해서")
    @Test
    void SUCCESS_verifyPickupWithAdminId() {}

    @DisplayName("픽업 확인 - qr이랑 맞는 지 확인")
    @Test
    void SUCCESS_verifyPickupWithQRMatch() {}

    @DisplayName("특정 스토어의 픽업 리스트 조회 성공")
    @Test
    void SUCCESS_getStorePickupListForAdmin() {}
    @DisplayName("존재하지 않는 거래에 대한 픽업 생성 실패")
    @Test
    void FAIL_addUserPickupList_transaction_not_found() {}
    @DisplayName("존재하지 않는 스토어에 대한 픽업 생성 실패")
    @Test
    void FAIL_addUserPickupList_store_not_found() {}
    @DisplayName("존재하지 않는 사용자의 경우 픽업 리스트 조회 실패")
    @Test
    void FAIL_getUserPickupList_user_not_found() {}

    @DisplayName("존재하지 않는 픽업의 경우 조회 실패")
    @Test
    void FAIL_getPickupDetails_not_found() {}

    @DisplayName("존재하지 않는 픽업의 경우 픽업 확인 실패")
    @Test
    void FAIL_verifyPickup_not_found() {}

    @DisplayName("픽업 확인 시 코드가 다른 경우 픽업 확인 실패")
    @Test
    void FAIL_verifyPickup_invalid_code() {}

    @DisplayName("존재하지 않는 스토어의 경우 픽업 리스트 조회 실패")
    @Test
    void FAIL_getStorePickupList_store_not_found() {}

}
