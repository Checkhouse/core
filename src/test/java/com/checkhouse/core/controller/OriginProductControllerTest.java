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
import com.checkhouse.core.service.OriginProductService;

@WebMvcTest(OriginProductController.class)
@AutoConfigureMockMvc
public class OriginProductControllerTest extends BaseIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OriginProductService originProductService;

    @BeforeEach
    void setup() {

    }
    //원본 상품 등록 성공
    @Test
    @DisplayName("원본 상품 등록 성공")
    public void addOriginProductSuccess() throws Exception {

    }
    //원본 상품 정보 수정 성공
    @Test
    @DisplayName("원본 상품 정보 수정 성공")
    public void updateOriginProductInfoSuccess() throws Exception {

    }
    //원본 상품 정보 조회 성공
    @Test
    @DisplayName("원본 상품 정보 조회 성공")
    public void getOriginProductInfoSuccess() throws Exception {

    }
    //원본 상품 목록 조회 성공
    @Test
    @DisplayName("원본 상품 목록 조회 성공")
    public void getOriginProductsSuccess() throws Exception {

    }   
    //카테고리별 원본 상품 목록 조회 성공
    @Test
    @DisplayName("카테고리별 원본 상품 목록 조회 성공")
    public void getOriginProductsWithCategorySuccess() throws Exception {

    }
    //원본 상품 검색 성공
    @Test
    @DisplayName("원본 상품 검색 성공")
    public void searchOriginProductsSuccess() throws Exception {

    }
    //원본 상품 삭제 성공
    @Test
    @DisplayName("원본 상품 삭제 성공")
    public void deleteOriginProductSuccess() throws Exception {

    }
}
