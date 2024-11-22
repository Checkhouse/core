package com.checkhouse.core.controller;

import com.checkhouse.core.apiPayload.BaseResponse;
import com.checkhouse.core.dto.UserDTO;
import com.checkhouse.core.dto.request.UserRequest;
import com.checkhouse.core.entity.User;
import com.checkhouse.core.entity.enums.Role;
import com.checkhouse.core.integration.BaseIntegrationTest;
import com.checkhouse.core.repository.mysql.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.net.URLEncoder;
import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserIntegrationTest extends BaseIntegrationTest {

    private static String baseUrl = "/user/";

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
    @Test
    void SUCCESS_getUserInfo() throws Exception {
        UUID userId = savedUser.getUserId();

        mockMvc.perform(
                MockMvcRequestBuilders.get(baseUrl + URLEncoder.encode(userId.toString())))
                .andExpect(status().isOk())
                .andReturn();
    }
}
