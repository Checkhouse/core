package com.checkhouse.core.service;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StoreServiceTest {
    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {}

    @DisplayName("스토어 저장 성공")
    @Test
    void SUCCESS_addStore() {}

    @DisplayName("스토어 리스트 조회 성공")
    @Test
    void SUCCESS_getStoreList() {}

    @DisplayName("특정 스토어 조회 성공")
    @Test
    void SUCCESS_getStoreDetails() {}

    @DisplayName("스토어 삭제 성공")
    @Test
    void SUCCESS_deleteStore() {}

    @DisplayName("스토어 코드 확인 성공")
    @Test
    void SUCCESS_verifyStoreCode() {}

    @DisplayName("잘못된 주소일 때 스토어 저장 실패")
    @Test
    void FAIL_addStore_invalid_address() {}

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
