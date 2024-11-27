package com.checkhouse.core.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.checkhouse.core.apiPayload.BaseResponse;
import com.checkhouse.core.dto.UsedProductDTO;
import com.checkhouse.core.dto.request.UsedProductRequest;
import com.checkhouse.core.service.UsedProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import com.checkhouse.core.entity.enums.UsedProductState;

@Slf4j
@Tag(name = "used-product apis", description = "중고 상품 관련 API - 중고 상품 등록, 수정, 삭제, 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/used-products")
@Validated
public class UsedProductController {
    private final UsedProductService usedProductService;
    //중고 상품 등록
    @Operation(summary = "중고 상품 등록")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "등록 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    })
    @PostMapping
    public BaseResponse<UsedProductDTO> addUsedProduct(
        @Valid @RequestBody UsedProductRequest.AddUsedProductRequest req
    ) {
        log.info("[중고 상품 등록] request: {}", req);
        return BaseResponse.onSuccess(usedProductService.addUsedProduct(req));
    }
    //중고 상품 정보 수정
    @Operation(summary = "중고 상품 정보 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    })
    @PutMapping("/{productId}")
    public BaseResponse<UsedProductDTO> updateUsedProduct(
        @PathVariable UUID productId,
        @Valid @RequestBody UsedProductRequest.UpdateUsedProductInfo req
    ) {
        log.info("[중고 상품 수정] productId: {}, request: {}", productId, req);
        return BaseResponse.onSuccess(usedProductService.updateUsedProductInfo(req));
    }
    //중고 상품 네고 상태 수정
    @Operation(summary = "중고 상품 네고 상태 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    })
    @PutMapping("/nego/{productId}")
    public BaseResponse<UsedProductDTO> updateUsedProductNegoState(
        @PathVariable UUID productId,
        @Valid @RequestBody UsedProductRequest.UpdateUsedProductNegoState req
    ) {
        log.info("[중고 상품 네고 상태 수정] productId: {}, request: {}", productId, req);
        return BaseResponse.onSuccess(usedProductService.updateUsedProductNegoState(req));
    }
    //중고 상품 상태 수정
    @Operation(summary = "중고 상품 상태 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    })
    @PutMapping("/state/{productId}")
    public BaseResponse<UsedProductDTO> updateUsedProductStatus(
        @PathVariable UUID productId,
        @Valid @RequestBody UsedProductRequest.UpdateUsedProductState req
    ) {
        log.info("[중고 상품 상태 수정] productId: {}, request: {}", productId, req);
        return BaseResponse.onSuccess(usedProductService.updateUsedProductStatus(req));
    }
    //중고 상품 취소
    @Operation(summary = "중고 상품 취소")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "취소 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    })
    @DeleteMapping("/{productId}")
    public BaseResponse<Void> cancelUsedProduct(
        @PathVariable UUID usedProductId
    ) {
        log.info("[중고 상품 취소] productId: {}", usedProductId);
        usedProductService.cancelAddUsedProduct(usedProductId);
        return BaseResponse.onSuccess(null);
    }
    //중고 상품 상세 조회
    @Operation(summary = "중고 상품 상세 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    })
    @GetMapping("/{productId}")
    public BaseResponse<UsedProductDTO> getUsedProductDetails(
        @PathVariable UUID usedProductId
    ) {
        log.info("[중고 상품 상세 조회] productId: {}", usedProductId);
        return BaseResponse.onSuccess(usedProductService.getUsedProductDetails(usedProductId));
    }
    //중고 상품 상태별 목록 조회
    @Operation(summary = "중고 상품 상태별 목록 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    })
    @GetMapping("/search/{status}")
    public BaseResponse<List<UsedProductDTO>> getUsedProductsByStatus(
        @PathVariable UsedProductState status
    ) {
        log.info("[중고 상품 상태별 목록 조회] status: {}", status);
        return BaseResponse.onSuccess(usedProductService.getUsedProductsByStatus(status.name()));
    }
    //중고 상품 유저별 목록 조회
    @Operation(summary = "중고 상품 유저별 목록 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    })
    @GetMapping("/search/{userId}")
    public BaseResponse<List<UsedProductDTO>> getUsedProductsByUser(
        @PathVariable UUID userId
    ) {
        log.info("[중고 상품 유저별 목록 조회] userId: {}", userId);
        return BaseResponse.onSuccess(usedProductService.getUsedProductsByUser(userId));
    }
    //중고 상품 유저별 중고상품 유저id 확인
    @Operation(summary = "중고 상품 유저별 중고상품 유저id 확인")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    })
    @GetMapping("/products/{productId}/user")
    public BaseResponse<UUID> getUserIdByUsedProduct(
        @PathVariable UUID usedProductId
    ) {
        log.info("[중고 상품 유저별 중고상품 유저id 확인] usedProductId: {}", usedProductId);
        return BaseResponse.onSuccess(usedProductService.findUsedProduct(usedProductId).getUser().getUserId());
    }
}
