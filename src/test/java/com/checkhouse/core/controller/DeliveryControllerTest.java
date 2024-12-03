package com.checkhouse.core.controller;

import java.util.UUID;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.checkhouse.core.dto.request.DeliveryRequest;
import com.checkhouse.core.entity.Delivery;
import com.checkhouse.core.integration.BaseIntegrationTest;
import com.checkhouse.core.service.DeliveryService;


@WebMvcTest(controllers = DeliveryController.class)
public class DeliveryControllerTest extends BaseIntegrationTest {
    private static final String BASE_URL = "/delivery";

    @Autowired
    MockMvc mvc;

    @MockBean
    private DeliveryService deliveryService;

    @BeforeEach
    void setup() {
    }
//    @Test
//    @DisplayName("배송 등록 성공")
//    void SUCCESS_addDelivery() throws Exception {
//    }
//    @Test
//    @DisplayName("배송 수정 성공")
//    void SUCCESS_updateDelivery() throws Exception {
//
//    }
//    @Test
//    @DisplayName("배송 조회 성공")
//    void SUCCESS_getDelivery() throws Exception {
//
//    }
//    @Test
//    @DisplayName("배송 삭제 성공")
//    void SUCCESS_deleteDelivery() throws Exception {
//
//    }
//    @Test
//    @DisplayName("배송 조회 실패")
//    void FAIL_getDelivery() throws Exception {
//
//    }
//    @Test
//    @DisplayName("배송 수정 실패")
//    void FAIL_updateDelivery() throws Exception {
//
//    }
//    @Test
//    @DisplayName("배송 삭제 실패")
//    void FAIL_deleteDelivery() throws Exception {
//
//    }
}
