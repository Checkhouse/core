package com.checkhouse.core.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SendServiceTest {
    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {}

    @DisplayName("발송 등록 성공")
    @Test
    void SUCCESS_addSend() {}

    @DisplayName("발송 상태 수정")
    @Test
    void SUCCESS_updateSendStatus() {}

    @DisplayName("존재하지 않는 배송일 경우 등록 실패")
    @Test
    void FAIL_addSend_invalid_delivery() {}

    @DisplayName("존재하지 않는 거래일 경우 등록 실패")
    @Test
    void FAIL_addSend_not_found() {}

    @DisplayName("존재하지 않는 발송 상태로 수정하는 경우 실패")
    @Test
    void FAIL_updateSendStatus_invalid_status() {}
}
