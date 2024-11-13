package com.checkhouse.core.service;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UsedProductServiceTest {
    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {}

    @DisplayName("중고 상품 등록")
    @Test
    void SUCCESS_addUsedProduct() {}

    @DisplayName("중고 상품 네고 상태 변경")
    @Test
    void SUCCESS_updateUsedProductNegoState() {}

    @DisplayName("중고 상품 상태 변경")
    @Test
    void SUCCESS_updateUsedProductStatus() {}
    @DisplayName("중고 상품 정보 수정")
    @Test
    void SUCCESS_updateUsedProductInfo() {}
    @DisplayName("중고 상품 정보 조회")
    @Test
    void SUCCESS_getUsedProductDetails() {}


    @DisplayName("중고 상품 등록 취소")
    @Test
    void SUCCESS_cancelAddUsedProduct() {}

    @DisplayName("특정 상태 중고 상품 리스트 조회")
    @Test
    void SUCCESS_getUsedProductsByStatus() {}

    @DisplayName("특정 사용자가 등록한 중고 상품 리스트 조회")
    @Test
    void SUCCESS_getUsedProductsByUser() {}

    /**
     *
     * todo 중고 상품 양식 자체는 컨트롤러에서 validate 로 걸러질텐데?
     */
    @DisplayName("중고 상품 등록 양식에 맞지 않을 때 등록 실패")
    @Test
    void FAIL_addUsedProduct_invalid_format_error() {} // 등록 양식 불일치 에러

    @DisplayName("존재하지 않는 중고 상품에 대한 네고 상태 변경은 실패")
    @Test
    void FAIL_updateUsedProductNegoState_not_exist() {}

    @DisplayName("중고 상품이 존재하지 않을 때 상태 변경 실패")
    @Test
    void FAIL_updateUsedProductStatus_not_found() {} // 존재하지 않는 상품 상태 변경 에러
    @DisplayName("중고 상품이 존재하지 않을 때 정보 수정 실패")
    @Test
    void FAIL_updateUsedProduct_not_found() {} // 존재하지 않는 상품 상태 변경 에러

    @DisplayName("중고 상품이 존재하지 않을 때 조회 실패")
    @Test
    void FAIL_getUsedProductDetails_not_found() {} // 존재하지 않는 상품 조회 에러

    @DisplayName("중고 상품이 존재하지 않을 때 취소 실패")
    @Test
    void FAIL_cancelUsedProductRegistration_not_found() {} // 존재하지 않는 상품 취소 에러

    @DisplayName("특정 상태인 중고 상품이 존재하지 않을 때 정보 조회 실패")
    @Test
    void FAIL_getUsedProductsByStatus_not_found() {} // 특정 상태 중고 상품 조회 실패 에러

    @DisplayName("사용자 특정 실패 시 리스트 조회 실패")
    @Test
    void FAIL_getUsedProductsByUser_user_not_found() {} // 사용자 특정 실패 에러
}
