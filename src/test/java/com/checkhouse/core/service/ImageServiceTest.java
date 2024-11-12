package com.checkhouse.core.service;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ImageServiceTest {
    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {}

    @DisplayName("이미지 URL 저장 성공")
    @Test
    void SUCCESS_saveImageUrl() {}

    @DisplayName("이미지 조회 성공")
    @Test
    void SUCCESS_getImage() {}

    @DisplayName("존재하지 않는 이미지 아이디의 경우 조회 실패")
    @Test
    void FAIL_getImage_not_found() {}

    @DisplayName("정상적이지 않는 URL의 경우 이미지 URL 저장 실패")
    @Test
    void FAIL_saveImageUrl_invalid_url() {}
}
