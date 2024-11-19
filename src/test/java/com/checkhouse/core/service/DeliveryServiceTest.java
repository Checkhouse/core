package com.checkhouse.core.service;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;

/**
 * 거래 생성 후 등록할 물건을 수거하러 갈 떄
 */

@ExtendWith(MockitoExtension.class)
public class DeliveryServiceTest {
    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {}

    @DisplayName("배송 등록 성공")
    @Test
    void SUCCESS_addDelivery() {}

    @DisplayName("배송 리스트 조회 성공")
    @Test
    void SUCCESS_getDeliveries() {}

    @DisplayName("배송 상태 업데이트")
    @Test
    void SUCCESS_updateDeliveryStatus() {}

    @DisplayName("송장 번호 등록")
    @Test
    void SUCCESS_registerTrackingNumber() {}

    @DisplayName("존재하지 않는 배송지 ID일 경우 배송 등록 실패")
    @Test
    void FAIL_addDelivery_invalid_address() {}

    @DisplayName("존재하지 않는 배송 상태로 변경시 업데이트 실패")
    @Test
    void FAIL_updateDeliveryStatus_invalid_status() {}

    // todo 필요한 테스트의 경우 담당자가 재량에 따라 추가

}