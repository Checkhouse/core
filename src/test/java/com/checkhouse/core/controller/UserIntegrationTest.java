package com.checkhouse.core.controller;

import com.checkhouse.core.apiPayload.BaseResponse;
import com.checkhouse.core.dto.UserDTO;
import com.checkhouse.core.dto.request.UserRequest;
import com.checkhouse.core.entity.User;
import com.checkhouse.core.entity.enums.Role;
import com.checkhouse.core.integration.BaseIntegrationTest;
import com.checkhouse.core.repository.mysql.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLEncoder;
import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserIntegrationTest extends BaseIntegrationTest {

    private static String baseUrl = "/user";

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    private User savedUser;
    @BeforeEach
    void setup() {

        User user = User.builder()
                .username("test user")
                .email("test@email.com")
                .nickname("test nickname")
                .password(null)
                .role(Role.ROLE_USER)
                .provider("naver")
                .providerId("random id naver")
                .isActive(true)
                .build();
        savedUser = userRepository.save(user);

    }
    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
    }
    @Test
    void addUserTest() throws Exception {
        UserRequest.AddUserRequest request = UserRequest.AddUserRequest.builder()
                .username("test user")
                .email("test2@email.com")
                .nickname("test nickname")
                .role(Role.ROLE_USER.name())
                .provider("naver")
                .providerId("random id naver")
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.post(baseUrl)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)

        ).andExpect(status().isOk());
    }
    @Test
    void SUCCESS_getUserInfo() throws Exception {
        // 헤더에 있는 값을 기반으로 사용자를 추정할 수 있음
        // todo 근데 이게 맞아?

        mockMvc.perform(
                        MockMvcRequestBuilders.get(baseUrl))
                .andExpect(status().isOk())
                .andReturn();
    }

}