package com.checkhouse.core.service;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FavoriteOriginServiceTest {
    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {}

    @DisplayName("좋아요 목록에 등록")
    @Test
    void SUCCESS_addToFavoriteList() {}

    @DisplayName("좋아요 목록에서 제거")
    @Test
    void SUCCESS_removeFromFavoriteList() {}

    @DisplayName("사용자의 원본 좋아요 리스트 조회")
    @Test
    void SUCCESS_getUserFavoriteOriginList() {}

    @DisplayName("모종의 이유로 실패")
    @Test
    void FAIL_genericReason() {}
}
