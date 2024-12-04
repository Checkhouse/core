package com.checkhouse.core.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum DeliveryState {
    //배송
    PRE_DELIVERY, //배송 전
    DELIVERING, //배송 중
    DELIVERED, //배송 완료
    //수거
    PRE_COLLECT, //수거 전
    // COLLECTING, //수거 중
    COLLECTED, //수거 완료
    //발송
    PRE_SEND, //발송 전
    // SENDING, //발송 중
    SENT //발송 완료
    ;

    @JsonCreator
    public static DeliveryState fromString(String state) {
        return DeliveryState.valueOf(state.toUpperCase());
    }
}