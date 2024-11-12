package com.checkhouse.core.service;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OriginProductServiceTest {
    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {}

    @DisplayName("원본 상품 등록")
    @Test
    void SUCCESS_addOriginProduct() {}

    @DisplayName("원본 상품 수정: 상품 정보만")
    @Test
    void SUCCESS_updateOriginProductInfo() {}
    @DisplayName("원본 상품 수정: 카테고리만")
    @Test
    void SUCCESS_updateOriginProductCategory() {}

    @DisplayName("원본 상품 삭제")
    @Test
    void SUCCESS_deleteOriginProduct() {}

    @DisplayName("원본 상품 조회")
    @Test
    void SUCCESS_getOriginProducts() {}
    @DisplayName("카테고리별 원본 상품 리스트 조회")
    @Test
    void SUCCESS_getOriginProductsByCategory() {}
    @DisplayName("원본 상품 정보 조회")
    @Test
    void SUCCESS_getOriginProductInfo() {}

    @DisplayName("원본 상품 검색")
    @Test
    void SUCCESS_searchOriginProduct() {}

    @DisplayName("중복된 원본 상품일 경우 저장 실패")
    @Test
    void FAIL_addOriginProduct_already_exist() {

    }
    @DisplayName("존재하지 않는 원본 상품에 대한 정보 수정 실패")
    @Test
    void FAIL_updateOriginProductInfo_not_found() {

    }

    @DisplayName("존재하지 않는 원본 상품에 대한 정보 조회 실패")
    @Test
    void FAIL_getOriginProductInfo_not_found() {

    }
    @DisplayName("존재하지 않은 카테고리로 원본 상품 조회 실패")
    @Test
    void FAIL_getOriginProductsByCategory_not_found() {}
}
