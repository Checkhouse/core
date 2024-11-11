package com.checkhouse.core.service;

import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

/**
 * 거래 생성 후 등록할 물건을 수거하러 갈 떄
 */
public class DeliveryServiceTest {
    @BeforeClass
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {}

    /**
     * 거래 생성 시 배송 생성
     */
    @Test
    void addDelivery() {}

    /**
     * 배송 상태 업데이트
     */
    @Test
    void updateDeliveryState() {}

    /**
     *
     * 수거 시작 시 송장 번호 업데이트
     */
    @Test
    void updateDeliveryCode() {}



}
