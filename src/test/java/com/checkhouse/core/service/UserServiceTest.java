package com.checkhouse.core.service;

import com.checkhouse.core.entity.User;
import com.checkhouse.core.entity.enums.Role;
import com.checkhouse.core.repository.mysql.UserRepository;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static reactor.core.publisher.Mono.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private User mockkedUser;

    @InjectMocks
    private UserService userService;

    @BeforeAll
    public static void beforeAll() {
        System.out.println("Before all");
    }

    @BeforeEach
    void setup() { }

    @AfterEach
    void cleanEach() {}

    @AfterAll
    public static void cleanUp() {
        System.out.println("After all");
    }

    /**
     * 혹시 몰라서 일단 추가해둠.
     */

    @DisplayName("사용자 저장")
    @Test
    void SUCCESS_addUser() { }
    @DisplayName("사용자 정보 조회")
    @Test
    void SUCCESS_getUserInfo() {
        // given
        // 사용자 ID

        // when
        // 사용자 정보를 저장

        // then
        // 저장된 사용자 정보 일치 확인
    }
    @DisplayName("사용자 정보 수정")
    @Test
    void SUCCESS_updateUserInfo() {
        // 수정된 사용자 정보가 주어짐

        // 수정된 사용자 정보를 저장합

        // 저장된 사용자 정보 일치 확인
    }

    @DisplayName("사용자 리스트 조회")
    @Test
    void SUCCESS_getUsers() {
        //

        // 사용자 조회

        // 조회횐 사용자 정보 일치 확인
    }
    @DisplayName("사용자 상태 수정")
    @Test
    void SUCCESS_updateUserState() {}

    @DisplayName("존재하지 않는 사용자의 경우 사용자 정보 조회 실패")
    @Test
    void FAIL_getUserInfo() {
        // given
        // 사용자 정보가 주어짐

        // when
        // 사용자 정보를 저장

        // then
        // 저장된 사용자 정보 일치 확인
    }

    @DisplayName("존재하지 않는 사용자의 경우 사용자 정보 수정 실패")
    @Test
    void FAIL_updateUserInfo() {}

    @DisplayName("존재하지 않는 사용자의 경우 사용자 상태 수정 실패")
    @Test
    void FAIL_updateUserState_not_found() {}

    @DisplayName("존재하지 않는 사용자 상태로 상태를 변경하려는 경우 실패")
    @Test
    void FAIL_updateUserState_invalid_state() {}
}