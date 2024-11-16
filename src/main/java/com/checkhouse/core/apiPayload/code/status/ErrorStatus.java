package com.checkhouse.core.apiPayload.code.status;

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

    // token
    _INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN401", "잘못된 코드입니다."),


    // address
    // FIXME: Id관련 오류는 통합해도 되지 않을까? (존재하지 않는 Uuid)
    _ADDRESS_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "ADDR4041", "Address ID를 찾을 수 없습니다."),

    // category
    _CATEGORY_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY4041", "Category ID를 찾을 수 없습니다."),
    _CATEGORY_NAME_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY4042", "카테고리를 찾을 수 없습니다."),
    _CATEGORY_ALREADY_EXIST(HttpStatus.CONFLICT, "CATEGORY409", "이미 존재하는 카테고리 입니다.")

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
