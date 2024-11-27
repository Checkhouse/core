package com.checkhouse.core.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import com.checkhouse.core.dto.UsedProductDTO;
import com.checkhouse.core.dto.request.UsedProductRequest;
import com.checkhouse.core.entity.User;
import com.checkhouse.core.entity.UsedProduct;
import com.checkhouse.core.entity.enums.UsedProductState;
import com.checkhouse.core.integration.BaseIntegrationTest;
import com.checkhouse.core.service.UsedProductService;

@WebMvcTest(UsedProductController.class)
public class UsedProductControllerTest {

    private static final String BASE_URL = "/used-products";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsedProductService usedProductService;

    private UUID testUserId;
    private UUID testProductId;
    private UsedProductState state;
    private UsedProductDTO testProductDTO;

    @BeforeEach
    void setup() {
    }

    @Test
    @DisplayName("중고 상품 등록 성공")
    public void addUsedProductSuccess() throws Exception {
        
    }

    @Test
    @DisplayName("중고 상품 정보 수정 성공")
    public void updateUsedProductInfoSuccess() throws Exception {
        
    }

    @Test
    @DisplayName("중고 상품 네고 상태 수정 성공")
    public void updateUsedProductNegoStateSuccess() throws Exception {
       
    }

    @Test
    @DisplayName("중고 상품 상태 수정 성공")
    public void updateUsedProductStatusSuccess() throws Exception {
       
    }

    @Test
    @DisplayName("중고 상품 취소 성공")
    public void cancelUsedProductSuccess() throws Exception {
        
    }

    @Test
    @DisplayName("중고 상품 상세 조회 성공")
    public void getUsedProductDetailsSuccess() throws Exception {
       
    }

    @Test
    @DisplayName("중고 상품 상태별 목록 조회 성공")
    public void getUsedProductsByStatusSuccess() throws Exception {
       
    }

    @Test
    @DisplayName("중고 상품 유저별 목록 조회 성공")
    public void getUsedProductsByUserSuccess() throws Exception {
        
    }

    @Test
    @DisplayName("중고 상품 유저별 중고상품 유저id 확인 성공")
    public void getUserIdByUsedProductSuccess() throws Exception {
    }
}

