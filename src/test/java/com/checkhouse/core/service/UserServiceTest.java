package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.UserDTO;
import com.checkhouse.core.dto.request.UserRequest;
import com.checkhouse.core.entity.User;
import com.checkhouse.core.entity.enums.Role;
import com.checkhouse.core.repository.mysql.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private UserService userService;


    private User mockkedUser;
    private User mockkedUserWithEmail;
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
        mockkedUserWithEmail = User.builder()
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

    @DisplayName("사용자 저장: OAuth")
    @Test
    void SUCCESS_addUser_oauth() {
        // 사용자 정보
        UserRequest.AddUserRequest request = UserRequest.AddUserRequest.builder()
                .username("test user")
                .email("test@test.com")
                .nickname("test nickname")
                .password(null)
                .role(Role.ROLE_USER.name())
                .provider("naver")
                .providerID("random id naver")
                .build();

        // mocking
        when(userRepository.findUserByEmail( any() )).thenReturn(Optional.empty());
        when(userRepository.save( any() )).thenReturn(mockkedUser);
        //when
        UserDTO result = userService.addUser(request);

        assertNotNull(result);
        assertEquals(mockkedUser.getUserID(), result.userID());

        // 사용자 찾기는 한 번만 이뤄져야함.
        verify(userRepository, times(1)).findUserByEmail( any() );

    }

    @DisplayName("사용자 저장: email")
    @Test
    void SUCCESS_addUser_email() {
        // 사용자 정보
        UserRequest.AddUserRequest request = UserRequest.AddUserRequest.builder()
                .username("test user")
                .email("test@test.com")
                .nickname("test nickname")
                .password("test password")
                .role(Role.ROLE_USER.name())
                .build();

        // mocking
        when(userRepository.findUserByEmail( any() )).thenReturn(Optional.empty());
        when(userRepository.save( any() )).thenReturn(mockkedUserWithEmail);

        //when
        UserDTO result = userService.addUser(request);


        //then
        assertNotNull(result);
        assertEquals(mockkedUserWithEmail.getUserID(), result.userID());
        assertNull(result.provider());

        // 사용자 찾기는 한 번만 이뤄져야함.
        verify(userRepository, times(1)).findUserByEmail( any() );



    }
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

    @DisplayName("이미 존재하는 사용자 이메일의 경우 사용자 저장 실패")
    @Test
    void FAIL_addUser_already_exist() {
        // 사용자 정보
        UserRequest.AddUserRequest request = UserRequest.AddUserRequest
                .builder()
                .username("test user")
                .email("test@test.com")
                .nickname("test nickname")
                .password(null)
                .role(Role.ROLE_USER.name())
                .provider("naver")
                .providerID("random id naver")
                .build();

        // mocking
        when(userRepository.findUserByEmail( any() )).thenReturn(Optional.of(mockkedUser));
        //when
        assertThrows(GeneralException.class, () -> {
            userService.addUser(request);
        });

        // 사용자 찾기는 한 번만 이뤄져야함.
        verify(userRepository, times(1)).findUserByEmail( any() );
    }

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
