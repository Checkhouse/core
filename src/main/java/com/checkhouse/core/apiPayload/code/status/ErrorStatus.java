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

    // used product
    _USED_PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "UP404", "중고 상품이 존재하지 않습니다."),

    //favorite
    _FAVORITE_NOT_FOUND(HttpStatus.NOT_FOUND, "FAV404", "찾을 수 없습니다."),
    // token
    _INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN401", "잘못된 코드입니다."),


    // address
    _ADDRESS_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "ADDR4041", "Address ID를 찾을 수 없습니다."),
    _USER_ADDRESS_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "UADDR404", "User Address ID를 찾을 수 없습니다."),

    // category
    _CATEGORY_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY4041", "Category ID를 찾을 수 없습니다."),
    _CATEGORY_NAME_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY4042", "카테고리를 찾을 수 없습니다."),
    _CATEGORY_ALREADY_EXIST(HttpStatus.CONFLICT, "CATEGORY409", "이미 존재하는 카테고리 입니다."),

    // image
    _IMAGE_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "IMAGE404", "Image ID를 찾을 수 없습니다."),
    _IMAGE_URL_NOT_EXIST(HttpStatus.NOT_FOUND, "IMAGE4041", "유효하지 않는 URL입니다."),
    _ORIGIN_IMAGE_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "OIMG404", "Origin Image ID를 찾을 수 없습니다."),
    _USED_IMAGE_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "UIMG404", "Used Image ID를 찾을 수 없습니다."),

    // store
    _STORE_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE404", "Store ID를 찾을 수 없습니다."),
    _STORE_ALREADY_EXISTS(HttpStatus.CONFLICT, "STORE409", "중복된 스토어 이름이 존재합니다."),
    // Hub
    _HUB_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "HUB404", "Hub ID를 찾을 수 없습니다."),
    _HUB_ALREADY_EXISTS(HttpStatus.CONFLICT, "HUB409", "중복된 허브 이름이 존재합니다."),
    _HUB_CLUSTERED_ID_ALREADY_EXISTS(HttpStatus.CONFLICT, "HUB4091", "중복된 클러스터링 존이 존재합니다."),
    _STOCK_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "STOCK404", "Stock ID를 찾을 수 없습니다.")
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
