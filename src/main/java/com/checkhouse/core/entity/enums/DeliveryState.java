package com.checkhouse.core.entity.enums;

public enum DeliveryState {
    //배송
    PRE_DELIVERY, //배송 전
    DELIVERING, //배송 중
    DELIVERED, //배송 완료
    //수거
    COLLECTING, //수거 전
    //발송
    SENDING //발송 전
;

    boolean isValidState(DeliveryState deliveryState) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isValidState'");
    }

}