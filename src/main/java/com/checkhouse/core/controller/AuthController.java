package com.checkhouse.core.controller;


import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.Token;
import com.checkhouse.core.dto.UserDTO;
import com.checkhouse.core.dto.request.AuthRequest;
import com.checkhouse.core.entity.User;
import com.checkhouse.core.entity.enums.Role;
import com.checkhouse.core.repository.mysql.UserRepository;
import com.checkhouse.core.service.RedisService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "auth apis", description = "auth")
@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final RedisService redisService;
    @PostMapping("/signin")
    public String signin(
            @Valid @RequestBody AuthRequest.SigninRequest req
    ) {
        User user = userRepository.findUserByEmail(req.email()).orElseThrow(
                () -> new GeneralException(ErrorStatus._USER_ID_NOT_FOUND)
        );
        if(req.password().equals(user.getPassword())) {
            Token token = redisService.saveTokens(user.getEmail());

            return token.accessToken();
        } else throw new RuntimeException("invaild password");

    }
    @PostMapping("/signup")
    public UserDTO signup(
            @Valid @RequestBody AuthRequest.SignupRequest req
    ) {
        return userRepository.save(
                User.builder()
                        .username(req.name())
                        .email(req.email())
                        .password(req.password())
                        .isActive(true)
                        .role(Role.ROLE_ADMIN)
                        .build()
        ).toDto();
    }
}
