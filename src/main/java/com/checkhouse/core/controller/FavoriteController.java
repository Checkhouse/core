package com.checkhouse.core.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import com.checkhouse.core.apiPayload.BaseResponse;
import com.checkhouse.core.dto.FavoriteDTO;
import com.checkhouse.core.dto.request.FavoriteRequest;
import com.checkhouse.core.service.FavoriteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Slf4j
@Tag(name = "favorite apis", description = "찜 관련 API - 찜 등록, 찜 삭제, 찜 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/favorite")
public class FavoriteController {
    private final FavoriteService favoriteService;
    //origin favorite 등록
    @Operation(summary = "origin favorite 등록")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "등록 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/origin")
    public BaseResponse<FavoriteDTO> addFavoriteOrigin(
        @Valid @RequestBody FavoriteRequest.AddToFavoriteRequest req) {
        try {
            log.info("[origin favorite 등록] request: {}", req);
            return BaseResponse.onSuccess(favoriteService.addFavoriteOrigin(req));
        } catch (Exception e) {
            log.error("[origin favorite 등록] request: {}, error: {}", req, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //used favorite 등록
    @Operation(summary = "used favorite 등록")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "등록 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/used")
    public BaseResponse<FavoriteDTO> addFavoriteUsed(
        @Valid @RequestBody FavoriteRequest.AddUsedProductLikeRequest req) {
        try {
            log.info("[used favorite 등록] request: {}", req);
            return BaseResponse.onSuccess(favoriteService.addFavoriteUsed(req));
        } catch (Exception e) {
            log.error("[used favorite 등록] request: {}, error: {}", req, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //origin favorite 삭제
    @Operation(summary = "origin favorite 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @DeleteMapping("/origin/{favoriteId}")
    public BaseResponse<Void> removeFavoriteOrigin(
        @Valid @RequestBody FavoriteRequest.RemoveFromFavoriteRequest req) {
        try {
            log.info("[origin favorite 삭제] request: {}", req);
            favoriteService.removeFavoriteOrigin(req);
            return BaseResponse.onSuccess(null);
        } catch (Exception e) {
            log.error("[origin favorite 삭제] request: {}, error: {}", req, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //used favorite 삭제
    @Operation(summary = "used favorite 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @DeleteMapping("/used/{favoriteId}")
    public BaseResponse<Void> removeFavoriteUsed(
        @Valid @RequestBody FavoriteRequest.RemoveUsedProductLikeRequest req) {
        try {
            log.info("[used favorite 삭제] request: {}", req);
            favoriteService.removeFavoriteUsed(req);
            return BaseResponse.onSuccess(null);
        } catch (Exception e) {
            log.error("[used favorite 삭제] request: {}, error: {}", req, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //origin favorite 조회
    @Operation(summary = "origin favorite 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/origin/list")
    public BaseResponse<List<FavoriteDTO>> getUserFavoriteOrigins(
        @Valid @RequestBody FavoriteRequest.GetUserFavoriteOrigins req) {
        try {
            log.info("[origin favorite 조회] request: {}", req);
            return BaseResponse.onSuccess(favoriteService.getUserFavoriteOrigins(req));
        } catch (Exception e) {
            log.error("[origin favorite 조회] request: {}, error: {}", req, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //used favorite 조회
    @Operation(summary = "used favorite 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/used/list")
    public BaseResponse<List<FavoriteDTO>> getUserFavoriteUsed(
        @Valid @RequestBody FavoriteRequest.GetUserFavoriteUsed req) {
        try {
            log.info("[used favorite 조회] request: {}", req);
            return BaseResponse.onSuccess(favoriteService.getUserFavoriteUsed(req));
        } catch (Exception e) {
            log.error("[used favorite 조회] request: {}, error: {}", req, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //origin favorite 개수 조회
    @Operation(summary = "origin favorite 개수 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/origin/count")
    public BaseResponse<Integer> getOriginProductFavoriteCount(
        @RequestParam UUID originProductId) {
        try {
            log.info("[origin favorite 개수 조회] request: {}", originProductId);
            return BaseResponse.onSuccess(favoriteService.getOriginProductFavoriteCount(originProductId));
        } catch (Exception e) {
            log.error("[origin favorite 개수 조회] request: {}, error: {}", originProductId, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //used favorite 개수 조회
    @Operation(summary = "used favorite 개수 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/used/count")
    public BaseResponse<Integer> getUsedProductFavoriteCount(
        @RequestParam UUID usedId) {
        try {
            log.info("[used favorite 개수 조회] request: {}", usedId);
            return BaseResponse.onSuccess(favoriteService.getUsedProductFavoriteCount(usedId));
        } catch (Exception e) {
            log.error("[used favorite 개수 조회] request: {}, error: {}", usedId, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
}
