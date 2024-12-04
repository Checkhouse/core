package com.checkhouse.core.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    ROLE_USER, ROLE_ADMIN, ROLE_INSPECTOR;

    @JsonCreator
    public static Role of(String name) {
        return valueOf(name.toUpperCase());
    }
}
