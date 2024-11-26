package com.checkhouse.core.controller;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.checkhouse.core.integration.BaseIntegrationTest;
import com.checkhouse.core.service.UsedProductService;

@WebMvcTest(UsedProductController.class)
@AutoConfigureMockMvc
public class UsedProductControllerTest extends BaseIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsedProductService usedProductService;

    @BeforeEach
    void setup() {

    }
    //중고 상품 등록 성공
    @Test
    @DisplayName("중고 상품 등록 성공")
    public void addUsedProductSuccess() throws Exception {

    }
    //중고 상품 정보 수정 성공
    @Test
    @DisplayName("중고 상품 정보 수정 성공")
    public void updateUsedProductInfoSuccess() throws Exception {

    }
    //중고 상품 네고 상태 수정 성공
    @Test
    @DisplayName("중고 상품 네고 상태 수정 성공")
    public void updateUsedProductNegoStateSuccess() throws Exception {

    }
    //중고 상품 상태 수정 성공
    @Test
    @DisplayName("중고 상품 상태 수정 성공")
    public void updateUsedProductStatusSuccess() throws Exception {

    }
    //중고 상품 삭제 성공
    @Test
    @DisplayName("중고 상품 삭제 성공")
    public void deleteUsedProductSuccess() throws Exception {

    }
    //중고 상품 취소 성공
    @Test
    @DisplayName("중고 상품 취소 성공")
    public void cancelUsedProductSuccess() throws Exception {

    }
    //중고 상품 상세 조회 성공
    @Test
    @DisplayName("중고 상품 상세 조회 성공")
    public void getUsedProductDetailsSuccess() throws Exception {

    }
    //중고 상품 상태별 목록 조회 성공
    @Test
    @DisplayName("중고 상품 상태별 목록 조회 성공")
    public void getUsedProductsByStatusSuccess() throws Exception {

    }
    //중고 상품 유저별 목록 조회 성공
    @Test
    @DisplayName("중고 상품 유저별 목록 조회 성공")
    public void getUsedProductsByUserSuccess() throws Exception {

    }
    //중고 상품 유저별 중고상품 유저id 확인 성공
    @Test
    @DisplayName("중고 상품 유저별 중고상품 유저id 확인 성공")
    public void getUserIdByUsedProductSuccess() throws Exception {

    }
}

