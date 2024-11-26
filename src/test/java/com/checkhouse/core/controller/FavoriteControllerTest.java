package com.checkhouse.core.controller;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.checkhouse.core.service.FavoriteService;

@WebMvcTest(FavoriteController.class)
@AutoConfigureMockMvc
public class FavoriteControllerTest {
    private static final String BASE_URL = "/favorite";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavoriteService favoriteService;

    @BeforeEach
    void setup() {

    }
    //origin favorite 등록 성공
    @Test
    @DisplayName("origin favorite 등록 성공")
    void addFavoriteOriginSuccess() throws Exception {

    }
    //used favorite 등록 성공
    @Test
    @DisplayName("used favorite 등록 성공")
    void addFavoriteUsedSuccess() throws Exception {

    }
    //origin favorite 삭제 성공
    @Test
    @DisplayName("origin favorite 삭제 성공")
    void removeFavoriteOriginSuccess() throws Exception {

    }
    //used favorite 삭제 성공
    @Test
    @DisplayName("used favorite 삭제 성공")
    void removeFavoriteUsedSuccess() throws Exception {

    }
    //origin favorite 조회 성공
    @Test
    @DisplayName("origin favorite 조회 성공")
    void getUserFavoriteOriginsSuccess() throws Exception {

    }
    //used favorite 조회 성공
    @Test
    @DisplayName("used favorite 조회 성공")
    void getUserFavoriteUsedSuccess() throws Exception {

    }
    //origin favorite 개수 조회 성공
    @Test
    @DisplayName("origin favorite 개수 조회 성공")
    void getOriginProductFavoriteCountSuccess() throws Exception {

    }
    //used favorite 개수 조회 성공
    @Test
    @DisplayName("used favorite 개수 조회 성공")
    void getUsedProductFavoriteCountSuccess() throws Exception {

    }

}
