package com.checkhouse.core.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

public class AuthRequest {
    @Builder
    public record SigninRequest(
            @NotNull
            @Email
            String email,
            @NotNull
            String password
    ) {}

    @Builder
    public record SignupRequest(
            @NotNull
            @Email
            String email,

            @NotNull
            String name,

            @NotNull
            String password
    ){}
}
