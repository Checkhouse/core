package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.UserDTO;
import com.checkhouse.core.dto.request.UserRequest;
import com.checkhouse.core.entity.User;
import com.checkhouse.core.entity.enums.Role;
import com.checkhouse.core.repository.mysql.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDTO addUser(UserRequest.AddUserRequest req) {
        // 중복 검사
        userRepository.findUserByEmail(req.email()).ifPresent(
                (a) -> {
                    throw new GeneralException(ErrorStatus._USER_ALREADY_EXIST);
                }
        );
        User savedUser = userRepository.save(
                User.builder()
                        .username(req.username())
                        .email(req.email())
                        .nickname(req.nickname())
                        .password(req.password())
                        .provider(req.provider())
                        .providerId(req.providerId())
                        .role(Role.valueOf(req.role()))
                        .isActive(true)
                        .build()
        );
        // 중복되지 않는 사용자의 경우
        return savedUser.toDto();
    }
    public UserDTO  getUserInfo(String userEmail) {
        log.info(userEmail);

        User user = userRepository.findUserByEmail(userEmail).orElseThrow(
                () -> new GeneralException(ErrorStatus._USER_NOT_FOUND)
        );

        return user.toDto();
    }
    public UserDTO updateUserInfo(UserRequest.UpdateUserInfo req) {
        User user = userRepository.findUserByEmail(req.email()).orElseThrow(
                () -> new GeneralException(ErrorStatus._USER_NOT_FOUND)
        );

        user.updateUserInfo(req.username(), req.nickname());

        try {
            userRepository.save(user);
        } catch (RuntimeException e) {
            throw new RuntimeException("사용자 저장 에러");
        }

        return user.toDto();
    }
    public UserDTO updateUserState(UserRequest.UpdateUserState req) {
        User user = userRepository.findUserByEmail(req.email()).orElseThrow(
                () -> new GeneralException(ErrorStatus._USER_NOT_FOUND)
        );

        user.updateUserState(req.state());

        try {
            userRepository.save(user);
        } catch (RuntimeException e) {
            throw new RuntimeException("사용자 저장 에러");
        }

        return user.toDto();
    }
    public List<UserDTO> getUsers() {
        try {
            return userRepository.findAll()
                    .stream().map(User::toDto).toList();
        } catch(RuntimeException e) {
            throw new RuntimeException("사용자 리스트 조회 에러");
        }
    }

    // 내부 로직용
    public User findUser(UUID userId){
        return userRepository.findById(userId).orElseThrow(
                () -> new GeneralException(ErrorStatus._USER_NOT_FOUND)
        );
    }

}
