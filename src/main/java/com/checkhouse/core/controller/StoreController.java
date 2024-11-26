package com.checkhouse.core.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
@RequestMapping("/store")
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
        try {
            log.info("[스토어 추가] request: {}", req);
            return BaseResponse.onSuccess(storeService.addStore(req));
        } catch (Exception e) {
            log.error("[스토어 추가] request: {}, error: {}", req, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //스토어 이름으로 조회
    @Operation(summary = "스토어 이름으로 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/name")
    public BaseResponse<StoreDTO> getStoreByName(String name) {
        try {
            log.info("[스토어 이름으로 조회] name: {}", name);
            return BaseResponse.onSuccess(storeService.getStoreByName(name));
        } catch (Exception e) {
            log.error("[스토어 이름으로 조회] name: {}, error: {}", name, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //스토어 조회
    @Operation(summary = "스토어 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/{storeId}")
    public BaseResponse<StoreDTO> getStore(
        @Valid @RequestBody StoreRequest.GetStoreRequest req) {
        try {
            log.info("[스토어 조회] request: {}", req);
            return BaseResponse.onSuccess(storeService.getStore(req));
        } catch (Exception e) {
            log.error("[스토어 조회] request: {}, error: {}", req, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //스토어 리스트 조회
    @Operation(summary = "스토어 리스트 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/list")
    public BaseResponse<List<StoreDTO>> getStores() {
        try {
            log.info("[스토어 리스트 조회]");
            return BaseResponse.onSuccess(storeService.getStores());
        } catch (Exception e) {
            log.error("[스토어 리스트 조회] error: {}", e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //스토어 정보 수정
    @Operation(summary = "스토어 정보 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PutMapping("/{storeId}")
    public BaseResponse<StoreDTO> updateStore(
        @Valid @RequestBody StoreRequest.UpdateStoreRequest req) {
        try {
            log.info("[스토어 정보 수정] request: {}", req);
            return BaseResponse.onSuccess(storeService.updateStore(req));
        } catch (Exception e) {
            log.error("[스토어 정보 수정] request: {}, error: {}", req, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //스토어 코드 수정
    @Operation(summary = "스토어 코드 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PutMapping("/code/{storeId}")
    public BaseResponse<StoreDTO> updateStoreCode(
        @Valid @RequestBody StoreRequest.UpdateStoreCodeRequest req) {
        try {
            log.info("[스토어 코드 수정] request: {}", req);
            return BaseResponse.onSuccess(storeService.updateStoreCode(req));
        } catch (Exception e) {
            log.error("[스토어 코드 수정] request: {}, error: {}", req, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //스토어 코드 확인
    @Operation(summary = "스토어 코드 확인")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "확인 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/code/{storeId}")
    public BaseResponse<Boolean> verifyCode(
        @Valid @RequestBody StoreRequest.VerifyCodeRequest req) {
        try {
            log.info("[스토어 코드 확인] request: {}", req);
            return BaseResponse.onSuccess(storeService.verifyCode(req));
        } catch (Exception e) {
            log.error("[스토어 코드 확인] request: {}, error: {}", req, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //스토어 삭제
    @Operation(summary = "스토어 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @DeleteMapping("/{storeId}")
    public BaseResponse<Void> deleteStore(
        @Valid @RequestBody StoreRequest.DeleteStoreRequest req) {
        try {
            log.info("[스토어 삭제] request: {}", req);
            storeService.deleteStore(req);
            return BaseResponse.onSuccess(null);
        } catch (Exception e) {
            log.error("[스토어 삭제] request: {}, error: {}", req, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
}
