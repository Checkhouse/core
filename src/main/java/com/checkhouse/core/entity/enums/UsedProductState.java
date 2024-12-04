package com.checkhouse.core.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UsedProductState {
    PRE_SALE, ON_SALE, POST_SALE;

    @JsonCreator
    public static UsedProductState fromString(String state) {
        return valueOf(state.toUpperCase());
    }
}