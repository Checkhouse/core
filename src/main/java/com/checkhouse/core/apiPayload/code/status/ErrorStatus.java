package com.checkhouse.core.apiPayload.code.status;

import co.elastic.clients.elasticsearch.nodes.Http;
import com.checkhouse.core.apiPayload.code.BaseErrorCode;
import com.checkhouse.core.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // 기본 에러
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // user
    _USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER404", "존재하지 않는 유저입니다."),
    _USER_ALREADY_EXIST(HttpStatus.CONFLICT, "USER409", "이미 존재하는 사용자입니다."),

    // origin product
    _ORIGIN_PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "OG404", "원본 상품이 존재하지 않습니다."),
    _ORIGIN_PRODUCT_ALREADY_EXISTS(HttpStatus.CONFLICT, "OG409", "중복된 원본 상품 이름이 존재합니다."),
    _ORIGIN_PRODUCT_UPDATE_FAILED(HttpStatus.BAD_REQUEST, "OG400", "원본 상품 정보 업데이트에 실패하였습니다."),
    _DELETE_ORIGIN_PRODUCT_FAILED(HttpStatus.BAD_REQUEST, "OG400", "원본 상품 삭제에 실패하였습니다."),

    _USED_PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "UP404", "중고 상품이 존재하지 않습니다."),

    // token
    _INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN401", "잘못된 코드입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder().message(message).code(code).isSuccess(false).build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
