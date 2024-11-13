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

    @DisplayName("이미 좋아요에 등록한 중고 물품의 경우 좋아요 등록 실패")
    @Test
    void FAIL_addToFavoriteList() {}

    @DisplayName("이미 좋아요 삭제한 중고 물픔의 경우 좋아요 삭제 실패")
    @Test
    void FAIL_removeFromFavoriteList() {}
}
