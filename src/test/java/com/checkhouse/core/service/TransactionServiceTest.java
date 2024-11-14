package com.checkhouse.core.service;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TransactionServiceTest {

    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {}

    @DisplayName("거래 생성")
    @Test
    void SUCCESS_addTransaction() {}

    @DisplayName("거래 상태 조회")
    @Test
    void SUCCESS_getTransactionStatus() {}

    @DisplayName("거래 상태 변경")
    @Test
    void SUCCESS_updateTransactionStatus() {}

    @DisplayName("특정 사용자 거래 리스트 조회")
    @Test
    void SUCCESS_getTransactionsByUser() {}

    @DisplayName("관리자 거래 리스트 조회")
    @Test
    void SUCCESS_getTransactionsForAdmin() {}

    @DisplayName("존재하지 않는 거래 정보 조회 실패")
    @Test
    void FAIL_getTransaction_not_found() {}

    @DisplayName("같은 중고 상품에 대해 거래가 이미 생성된 경우 실패")
    @Test
    void FAIL_addTransaction_already_exists_error() {}

    @DisplayName("존재하지 않는 중고 물품에 대한 거래 등록은 실패")
    @Test
    void FAIL_addTransaction_not_found() {}

    @DisplayName("존재하지 않는 거래 상태는 변경 실패")
    @Test
    void FAIL_updateTransactionStatus_invalid_status() {}

    @DisplayName("사용자 특정 실패 시 리스트 조회 실패")
    @Test
    void FAIL_getTransactionsByUser_user_not_found() {}

}
