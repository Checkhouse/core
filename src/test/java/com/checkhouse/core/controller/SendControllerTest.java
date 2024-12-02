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
import com.checkhouse.core.service.SendService;

@WebMvcTest(SendController.class)
@AutoConfigureMockMvc
public class SendControllerTest extends BaseIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SendService sendService;

    @BeforeEach
    void setup() {

    }   
    //발송 등록 성공
    @Test
    @DisplayName("발송 등록 성공")
    void addSendSuccess() throws Exception {

    }
    //발송 상태 업데이트 성공
    @Test
    @DisplayName("발송 상태 업데이트 성공")
    void updateSendStateSuccess() throws Exception {

    }
    //발송 삭제 성공
    @Test
    @DisplayName("발송 삭제 성공")
    void deleteSendSuccess() throws Exception {

    }
    //발송 목록 조회 성공
    @Test
    @DisplayName("발송 목록 조회 성공")
    void getSendListSuccess() throws Exception {

    }
}
