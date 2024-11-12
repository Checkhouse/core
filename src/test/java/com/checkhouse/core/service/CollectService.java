package com.checkhouse.core.service;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CollectService {
    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {}
    @DisplayName("수거 등록 성공")
    @Test
    void SUCCESS_addCollect() {}

    @DisplayName("수거 상태 수정")
    @Test
    void SUCCESS_updateCollectStatus() {}

    @DisplayName("존재하지 않는 배송일 경우 등록 실패")
    @Test
    void FAIL_addCollect_not_found() {}

    @DisplayName("존재하지 않는 중고 상품의 경우 등록 실패")
    @Test
    void FAIL_addCollect_not_found_used_product() {}

    @DisplayName("존재하지 않는 수거 상태로 수정하는 경우 실패")
    @Test
    void FAIL_updateCollectStatus_invalid_status() {}
}
