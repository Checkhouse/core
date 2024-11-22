package com.checkhouse.core.controller;

import com.checkhouse.core.apiPayload.BaseResponse;
import com.checkhouse.core.dto.UserDTO;
import com.checkhouse.core.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@Tag(name = "user apis")
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("{userId}")
    public BaseResponse<UserDTO> getUserInfo(
            @PathVariable UUID userId
    ) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDTO user = userService.getUserInfo(userEmail);

        return BaseResponse.onSuccess(user);
    }
}
