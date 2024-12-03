package com.checkhouse.core.controller;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.checkhouse.core.service.CollectService;

@WebMvcTest(CollectController.class)
@AutoConfigureMockMvc
public class CollectControllerTest {
    private static final String BASE_URL = "/collect";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CollectService collectService;

    @BeforeEach
    void setup() {

    }
//    @Test
//    @DisplayName("수거 등록 성공")
//    void SUCCESS_addCollect() throws Exception {
//
//    }
//    @Test
//    @DisplayName("수거 상태 업데이트 성공")
//    void SUCCESS_updateCollect() throws Exception {
//
//    }
//    @Test
//    @DisplayName("수거 리스트 조회 성공")
//    void SUCCESS_getCollectList() throws Exception {
//
//    }
//    @Test
//    @DisplayName("수거 삭제 성공")
//    void SUCCESS_deleteCollect() throws Exception {
        
//    }
}
