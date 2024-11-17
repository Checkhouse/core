package com.checkhouse.core.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;
public class UserRequest {

    @Builder
    public record AddUserRequest (
            @NotNull
            String username,

            @NotNull
            @Email
            String email,
            // nullable
            String nickname,
            String password,
            String provider,
            String providerId,
            @NotNull
            String role
    ) {}
    @Builder

    public record UpdateUserInfo(
            @NotNull
            UUID userId,
            String username,
            // nullable
            String nickname,
            String email
    ){}

    @Builder
    public record UpdateUserState (
            @NotNull
            UUID userId,
            String email,
            // nullable
            Boolean state
    ) {}

    public record FindUser (
            @NotNull
            UUID userId
    ) {}
}
