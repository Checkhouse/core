package com.checkhouse.core.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.config.JpaMetamodelMappingContextFactoryBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import com.checkhouse.core.dto.request.DeliveryRequest;
import com.checkhouse.core.entity.Address;
import com.checkhouse.core.entity.Delivery;
import com.checkhouse.core.entity.enums.DeliveryState;
import com.checkhouse.core.integration.BaseIntegrationTest;
import com.checkhouse.core.service.DeliveryService;


@WebMvcTest(value = DeliveryController.class, 
    excludeAutoConfiguration = {
        DataJpaTest.class,
        JpaMetamodelMappingContextFactoryBean.class
    })
@AutoConfigureMockMvc
class DeliveryControllerTest {
    private static final String BASE_URL = "/api/v1/delivery";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DeliveryService deliveryService;

    private Delivery delivery;

    @BeforeEach
    void setup() {
        delivery = Delivery.builder()
            .deliveryId(UUID.randomUUID())
            .address(Address.builder()
                .address("test address")
                .build())
            .trackingCode("test tracking code")
            .deliveryState(DeliveryState.PRE_DELIVERY)
            .build();
    }
    @Test
    @DisplayName("배송 등록 성공")
    void SUCCESS_addDelivery() throws Exception {
        DeliveryRequest.AddDeliveryRequest req = new DeliveryRequest.AddDeliveryRequest(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "test tracking code"
        );
        when(deliveryService.addDelivery(req)).thenReturn(delivery.toDto());
        mvc.perform(MockMvcRequestBuilders.post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isOk());
    }
    @Test
    @DisplayName("배송 수정 성공")
    public void SUCCESS_updateDelivery() throws Exception {
        
    }
    @Test
    @DisplayName("배송 조회 성공")
    void SUCCESS_getDelivery() throws Exception {
        
    }
    @Test
    @DisplayName("배송 삭제 성공")
    void SUCCESS_deleteDelivery() throws Exception {
        
    }
    @Test
    @DisplayName("배송 조회 실패")
    void FAIL_getDelivery() throws Exception {
        
    }
    @Test
    @DisplayName("배송 수정 실패")
    void FAIL_updateDelivery() throws Exception {
        
    }
    @Test
    @DisplayName("배송 삭제 실패")
    void FAIL_deleteDelivery() throws Exception {
        
    }
}
