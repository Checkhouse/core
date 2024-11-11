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
    void setup() {
        // naver 로그인 사용자
        mockkedUser = User.builder()
                .userID(UUID.randomUUID())
                .username("test user")
                .email("test@test.com")
                .nickname("test nickname")
                .password(null)
                .role(Role.ROLE_USER)
                .provider("naver")
                .providerID("random id naver")
                .build();

        // email 로그인 사용자
        mockkedUser = User.builder()
                .userID(UUID.randomUUID())
                .username("test user")
                .email("test@test.com")
                .nickname("test nickname")
                .password("test password")
                .role(Role.ROLE_USER)
                .build();
    }

    @AfterEach
    void cleanEach() {}

    @AfterAll
    public static void cleanUp() {
        System.out.println("After all");
    }

    /**
     * 혹시 몰라서 일단 추가해둠.
     */
    @Ignore
    void addUser() {
        // 사용자 정보
        UUID userID = UUID.randomUUID();
        User user = User.builder()
                .userID(userID)
                .username("test user")
                .email("test@test.com")
                .nickname("test nickname")
                .password(null)
                .role(Role.ROLE_USER)
                .provider("naver")
                .providerID("random id naver")
                .build();
    }

    @Test
    void SUCCESS_getUserInfo() {
        // given
        // 사용자 ID

        // when
        // 사용자 정보를 저장

        // then
        // 저장된 사용자 정보 일치 확인
    }

    @Test
    void SUCCESS_updateUserInfo() {
        // 수정된 사용자 정보가 주어짐

        // 수정된 사용자 정보를 저장합

        // 저장된 사용자 정보 일치 확인
    }

    @Test
    void SUCCESS_getUsers() {
        //

        // 사용자 조회

        // 조회횐 사용자 정보 일치 확인
    }

    /**
     * 사용자 조회는 다음과 같을 때 실패함
     * 1. 사용자가 없는 경우
     */
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

    /**
     * 사용자 정보 수정은 다음과 같을 때 실패함
     * 1. 존재하지 않는 사용자
     * todo validation에서 타입 체크가 안되는 경우가 있나?
     */
    @DisplayName("존재하지 않는 사용자의 경우 사용자 정보 수정 실패")
    @Test
    void FAIL_updateUserInfo() {}

    /**
     * 사용자 목록 조회는 다음과 같을 떄 실패함
     * 1. 모든 사용자 조회가 실패하는 경우가 있나...?
     */
    @DisplayName("모종의 이유로 실패")
    @Test
    void FAIL_getUsers() {}
}
