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

import java.util.List;
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
                .userId(UUID.randomUUID())
                .username("test user")
                .email("test@test.com")
                .nickname("test nickname")
                .password(null)
                .role(Role.ROLE_USER)
                .provider("naver")
                .providerId("random id naver")
                .build();

        // email 로그인 사용자
        mockkedUserWithEmail = User.builder()
                .userId(UUID.randomUUID())
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
        assertEquals(mockkedUser.getUserId(), result.userId());

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
        assertEquals(mockkedUserWithEmail.getUserId(), result.userId());
        assertNull(result.provider());

        // 사용자 찾기는 한 번만 이뤄져야함.
        verify(userRepository, times(1)).findUserByEmail( any() );

    }
    @DisplayName("사용자 정보 조회")
    @Test
    void SUCCESS_getUserInfo() {
        // given
        // 사용자 email
        String userId = "test@test.com";
        // when
        // 사용자 정보를 저장
        when(userRepository.findUserByEmail( any() )).thenReturn(Optional.of(mockkedUser));

        UserDTO result = userService.getUserInfo(userId);

        assertEquals(mockkedUser.getEmail(), result.email());
        // then
        // 저장된 사용자 정보 일치 확인

        verify(userRepository, times(1)).findUserByEmail( any() );
    }
    @DisplayName("사용자 정보 수정")
    @Test
    void SUCCESS_updateUserInfo() {
        // 수정된 사용자 정보가 주어짐
        UserRequest.UpdateUserInfo request = UserRequest.UpdateUserInfo.builder()
                .username("test@test.com2")
                .nickname("test nickname2")
                .build();


        when(userRepository.findUserByEmail( any() )).thenReturn(Optional.of(mockkedUser));
        // todo 더 깔끔한 방법은 없을까?
        when(userRepository.save( any() )).thenReturn(
                User.builder()
                        .userId(mockkedUser.getUserId())
                        .nickname(request.getNickname())
                        .username(request.getUsername())
                        .role(mockkedUser.getRole())
                        .isActive(mockkedUser.getIsActive())
                        .provider(mockkedUser.getProvider())
                        .providerId(mockkedUser.getProviderId())
                        .build()
        );

        // 수정된 사용자 정보를 저장
        UserDTO result = userService.updateUserInfo(request);


        // 저장된 사용자 정보 일치 확인
        assertEquals(mockkedUser.getNickname(), result.nickname());

        verify(userRepository, times(1)).findUserByEmail( any() );
    }

    @DisplayName("사용자 리스트 조회")
    @Test
    void SUCCESS_getUsers() {
        // 사용자 조회
        when(userRepository.findAll( )).thenReturn(List.of(mockkedUser, mockkedUserWithEmail));
        // todo 더 깔끔한 방법은 없을까?

        List<UserDTO> result = userService.getUsers();

        assertEquals(result.size(), 2);
        // 조회횐 사용자 정보 일치 확인
    }
    @DisplayName("사용자 상태 수정")
    @Test
    void SUCCESS_updateUserState() {
        // given
        UserRequest.UpdateUserState request = UserRequest.UpdateUserState.builder()
                .email("test@test.com")
                .state(false)
                .build();

        when(userRepository.findUserByEmail(any())).thenReturn(Optional.of(mockkedUser));
        when(userRepository.save( any() )).thenReturn(
                User.builder()
                        .userId(mockkedUser.getUserId())
                        .nickname(mockkedUser.getNickname())
                        .username(mockkedUser.getUsername())
                        .role(mockkedUser.getRole())
                        .isActive(request.getState())
                        .provider(mockkedUser.getProvider())
                        .providerId(mockkedUser.getProviderId())
                        .build()
        );

        // when
        UserDTO result = userService.updateUserState(request);
        assertEquals(result.isActive(), false);
        // then
        verify(userRepository, times(1)).findUserByEmail(any());
        verify(userRepository, times(1)).save(any(User.class));
    }

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
        String userId = "nonexistent@test.com";

        // when
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.empty());

        // then
        assertThrows(GeneralException.class, () -> {
            userService.getUserInfo(userId);
        });

        verify(userRepository, times(1)).findUserByEmail(any());
    }

    @DisplayName("존재하지 않는 사용자의 경우 사용자 정보 수정 실패")
    @Test
    void FAIL_updateUserInfo() {
        // 수정된 사용자 정보가 주어짐
        UserRequest.UpdateUserInfo request = UserRequest.UpdateUserInfo.builder()
                .username("nonexistent@test.com")
                .nickname("nonexistent nickname")
                .build();

        when(userRepository.findUserByEmail(any())).thenReturn(Optional.empty());

        assertThrows(GeneralException.class, () -> {
            userService.updateUserInfo(request);
        });

        verify(userRepository, times(1)).findUserByEmail(any());
    }

    @DisplayName("존재하지 않는 사용자의 경우 사용자 상태 수정 실패")
    @Test
    void FAIL_updateUserState_not_found() {
        // given
        UserRequest.UpdateUserState request = UserRequest.UpdateUserState.builder()
                .state(false)
                .email("test@testtest.com")
                .build();

        // when
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.empty());

        // then
        assertThrows(GeneralException.class, () -> {
            userService.updateUserState(request);
        });

        verify(userRepository, times(1)).findUserByEmail(any());

    }
}
