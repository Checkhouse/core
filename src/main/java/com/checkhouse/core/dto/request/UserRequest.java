package com.checkhouse.core.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserRequest {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddUserRequest {
        @NotNull
        String username;

        @NotNull
        @Email
        String email;

        // nullable
        String nickname = null;
        String password = null;
        String provider = null;
        String providerID = null;

        @NotNull
        String role;
    }
}
