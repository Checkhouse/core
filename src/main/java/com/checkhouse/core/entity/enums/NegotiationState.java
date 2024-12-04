package com.checkhouse.core.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum NegotiationState {
    WAITING, ACCEPTED, DENIED, CANCELLED;

    @JsonCreator
    public static NegotiationState fromString(String value) {
        return NegotiationState.valueOf(value.toUpperCase());
    }
}
