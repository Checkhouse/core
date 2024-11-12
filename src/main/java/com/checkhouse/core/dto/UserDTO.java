package com.checkhouse.core.dto;

import com.checkhouse.core.entity.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserDTO(
        UUID userId,
        String username,
        String email,

        String nickname,

        String password,

        String provider,
        String providerID,
        Role role

) {}
