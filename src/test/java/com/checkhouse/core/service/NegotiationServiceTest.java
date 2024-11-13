package com.checkhouse.core.service;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class NegotiationServiceTest {
    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {}

    @DisplayName("네고 생성")
    @Test
    void SUCCESS_addNegotiation() {}

    @DisplayName("네고 승인 - 수락 or 거절")
    @Test
    void SUCCESS_approveNegotiation() {}

    @DisplayName("제안한 네고 내역 리스트 조회")
    @Test
    void SUCCESS_getProposedNegotiations() {}

    @DisplayName("제안 받은 네고 내역 리스트 조회")
    @Test
    void SUCCESS_getReceivedNegotiations() {}

    @DisplayName("네고 취소")
    @Test
    void SUCCESS_cancelNegotiation() {}

    @DisplayName("등록 가격보다 높거나 같은 가격의 경우 네고 등록 실패")
    @Test
    void FAIL_addNegotiation_invalid_price() {}

    @DisplayName("존재하지 않는 네고 승인은 실패")
    @Test
    void FAIL_approveNegotiation_not_found() {}
    @DisplayName("이미 승인된 네고에 대한 승인은 실패")
    @Test
    void FAIL_approveNegotiation_already_approved() {}

    @DisplayName("이미 거절된 네고에 대한 거절은 실패")
    @Test
    void FAIL_approveNegotiation_already_denied() {}

    @DisplayName("네고를 거부한 중고 상품에 대한 내고 생성은 실패")
    @Test
    void FAIL_addNegotiation_nego_is_not_allowed() {}

    @DisplayName("중고상품이 없는 경우 가격 제안 실패")
    @Test
    void FAIL_addNegotiation_no_used_product() {}

    @DisplayName("네고 승인 시에 제안이 이미 취소된 경우 실패")
    @Test
    void FAIL_approveNegotiation_already_cancelled() {}

    @DisplayName("네고 취소 시 제안이 이미 승인된 경우 실패")
    @Test
    void FAIL_cancelNegotiation_already_approved() {}



}
