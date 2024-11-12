package com.checkhouse.core.service;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class InspectionServiceTest {
    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {}

    @DisplayName("검수 물품 등록")
    @Test
    void SUCCESS_addInspectionItem() {}

    @DisplayName("검수 상태 업데이트")
    @Test
    void SUCCESS_updateInspectionStatus() {}

    @DisplayName("검수 사진 등록 - 상태 업데이트")
    @Test
    void SUCCESS_addInspectionPhoto_updateStatus() {}

    @DisplayName("검수 리스트 조회 - 관리자")
    @Test
    void SUCCESS_getInspectionListForAdmin() {}

    // todo?
    @DisplayName("판매로 등록된 상품과 검수 상품이 다를 때 실패")
    @Test
    void FAIL_addInspectionItem_mismatched_product() {}

    @DisplayName("판매 등록이 안된 상품일 때 실패")
    @Test
    void FAIL_addInspectionItem_not_registered_for_sale() {}

    @DisplayName("존재하지 않는 검수자가 검수했을 때 실패")
    @Test
    void FAIL_inspectItem_not_found_inspector() {}
}
