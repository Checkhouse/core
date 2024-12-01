package com.checkhouse.core.controller;

import com.checkhouse.core.apiPayload.BaseResponse;
import com.checkhouse.core.apiPayload.code.status.SuccessStatus;
import com.checkhouse.core.dto.UserDTO;
import com.checkhouse.core.dto.request.UserRequest;
import com.checkhouse.core.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@Tag(name = "user apis")
@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public BaseResponse<UserDTO> getUserInfo() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info(userEmail);
        UserDTO user = userService.getUserInfo(userEmail);

        return BaseResponse.onSuccess(user);
    }
    @PostMapping
    public BaseResponse<UserDTO> saveUser(
            @RequestBody UserRequest.AddUserRequest request
    ) {
        UserDTO user = userService.addUser(request);
        return BaseResponse.onSuccess(user);
    }

}
