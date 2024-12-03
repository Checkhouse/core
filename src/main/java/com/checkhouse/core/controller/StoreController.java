package com.checkhouse.core.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;
import com.checkhouse.core.service.StoreService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.checkhouse.core.apiPayload.BaseResponse;
import com.checkhouse.core.dto.StoreDTO;
import com.checkhouse.core.dto.request.StoreRequest;

@Tag(name = "Store", description = "스토어 API")
@RestController
@RequestMapping("/api/v1/store")
@RequiredArgsConstructor
@Slf4j
public class StoreController {
    private final StoreService storeService;

    //스토어 추가
    @Operation(summary = "스토어 추가")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "추가 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping
    public BaseResponse<StoreDTO> addStore(
        @Valid @RequestBody StoreRequest.AddStoreRequest req) {
        log.info("[스토어 추가] request: {}", req);
        return BaseResponse.onSuccess(storeService.addStore(req));
    }
    //스토어 조회
    @Operation(summary = "스토어 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping
    public BaseResponse<StoreDTO> getStore(
        @RequestParam UUID storeId) {
        StoreRequest.GetStoreRequest req = new StoreRequest.GetStoreRequest(storeId);
        log.info("[스토어 조회] request: {}", req);
        return BaseResponse.onSuccess(storeService.getStore(req));
    }
    //스토어 리스트 조회
    @Operation(summary = "스토어 리스트 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/list")
    public BaseResponse<List<StoreDTO>> getStores() {
        log.info("[스토어 리스트 조회]");
        return BaseResponse.onSuccess(storeService.getStores());
    }
    //스토어 정보 수정
    @Operation(summary = "스토어 정보 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PatchMapping
    public BaseResponse<StoreDTO> updateStore(
        @Valid @RequestBody StoreRequest.UpdateStoreRequest req) {
        log.info("[스토어 정보 수정] request: {}", req);
        return BaseResponse.onSuccess(storeService.updateStore(req));
    }
    //스토어 코드 수정
    @Operation(summary = "스토어 코드 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PatchMapping("/code")
    public BaseResponse<StoreDTO> updateStoreCode(
        @Valid @RequestBody StoreRequest.UpdateStoreCodeRequest req) {
        log.info("[스토어 코드 수정] request: {}", req);
        return BaseResponse.onSuccess(storeService.updateStoreCode(req));
    }
    //스토어 삭제
    @Operation(summary = "스토어 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @DeleteMapping
    public BaseResponse<Void> deleteStore(@RequestParam UUID storeId) {
        storeService.deleteStore(
            StoreRequest.DeleteStoreRequest.builder()
                .storeId(storeId)
                .build()
        );
        return BaseResponse.onSuccess(null);
    }
}
