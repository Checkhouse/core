package com.checkhouse.core.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.checkhouse.core.apiPayload.BaseResponse;
import com.checkhouse.core.dto.OriginProductDTO;
import com.checkhouse.core.dto.request.OriginProductRequest;
import com.checkhouse.core.service.OriginProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Slf4j
@Tag(name = "origin-product apis", description = "원본 상품 관련 API - 원본 상품 등록, 원본 상품 정보 수정, 원본 상품 정보 조회, 원본 상품 목록 조회, 카테고리별 원본 상품 목록 조회, 원본 상품 검색, 원본 상품 삭제")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/origin-product")
public class OriginProductController {
    private final OriginProductService originProductService;
    //원본 상품 등록
    @Operation(summary = "원본 상품 등록")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "등록 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping
    public BaseResponse<OriginProductDTO> addOriginProduct(
        @Valid @RequestBody OriginProductRequest.AddOriginProductRequest req) {
        try {
            log.info("[원본 상품 등록] request: {}", req);
            return BaseResponse.onSuccess(originProductService.addOriginProduct(req));
        } catch (Exception e) {
            log.error("[원본 상품 등록] request: {}, error: {}", req, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //원본 상품 정보 수정
    @Operation(summary = "원본 상품 정보 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PutMapping("/{originProductId}")
    public BaseResponse<OriginProductDTO> updateOriginProductInfo(
        @Valid @RequestBody OriginProductRequest.UpdateOriginProductInfo req) {
        try {
            log.info("[원본 상품 정보 수정] request: {}", req);
            return BaseResponse.onSuccess(originProductService.updateOriginProductInfo(req));
        } catch (Exception e) {
            log.error("[원본 상품 정보 수정] request: {}, error: {}", req, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //원본 상품 정보 조회
    @Operation(summary = "원본 상품 정보 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/{originProductId}")
    public BaseResponse<OriginProductDTO> getOriginProductInfo(
        @RequestParam UUID originProductId) {
        try {
            log.info("[원본 상품 정보 조회] request: {}", originProductId);
            return BaseResponse.onSuccess(originProductService.getOriginProductInfo(originProductId));
        } catch (Exception e) {
            log.error("[원본 상품 정보 조회] request: {}, error: {}", originProductId, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //원본 상품 목록 조회
    @Operation(summary = "원본 상품 목록 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/{originProductId}/list")
    public BaseResponse<List<OriginProductDTO>> getOriginProducts(
        @RequestParam UUID originProductId) {
        try {
            log.info("[원본 상품 목록 조회] request: {}", originProductId);
            return BaseResponse.onSuccess(originProductService.getOriginProducts());
        } catch (Exception e) {
            log.error("[원본 상품 목록 조회] error: {}", e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //카테고리별 원본 상품 목록 조회
    @Operation(summary = "카테고리별 원본 상품 목록 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/category/{categoryId}/list")
    public BaseResponse<List<OriginProductDTO>> getOriginProductsWithCategory(
        @RequestParam UUID categoryId) {
        try {
            log.info("[카테고리별 원본 상품 목록 조회] request: {}", categoryId);
            return BaseResponse.onSuccess(originProductService.getOriginProductsWithCategory(categoryId));
        } catch (Exception e) {
            log.error("[카테고리별 원본 상품 목록 조회] request: {}, error: {}", categoryId, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //원본 상품 검색
    @Operation(summary = "원본 상품 검색")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/search/{query}") //추후 수정 필요 query로 검색할건지
    public BaseResponse<List<OriginProductDTO>> searchOriginProducts(
        @RequestParam String query) {
        try {
            log.info("[원본 상품 검색] request: {}", query);
            return BaseResponse.onSuccess(originProductService.searchOriginProducts(query));
        } catch (Exception e) {
            log.error("[원본 상품 검색] request: {}, error: {}", query, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //원본 상품 삭제
    @Operation(summary = "원본 상품 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @DeleteMapping("/{originProductId}")
    public BaseResponse<Void> deleteOriginProduct(
        @RequestParam UUID originProductId) {
        try {
            log.info("[원본 상품 삭제] request: {}", originProductId);
            originProductService.deleteOriginProduct(new OriginProductRequest.DeleteOriginProduct(originProductId));
            return BaseResponse.onSuccess(null);
        } catch (Exception e) {
            log.error("[원본 상품 삭제] request: {}, error: {}", originProductId, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
}
