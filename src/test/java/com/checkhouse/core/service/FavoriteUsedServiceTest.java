package com.checkhouse.core.service;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FavoriteUsedServiceTest {
    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {}

    @DisplayName("중고 상품 좋아요 추가")
    @Test
    void SUCCESS_addUsedProductLike() {}

    @DisplayName("중고 상품 좋아요 삭제")
    @Test
    void SUCCESS_deleteUsedProductLike() {}

    @DisplayName("중고 상품 좋아요 리스트")
    @Test
    void SUCCESS_getUsedProductLikeList() {}

    @DisplayName("모종의 이유로 실패")
    @Test
    void FAIL_genericReason() {}
}
