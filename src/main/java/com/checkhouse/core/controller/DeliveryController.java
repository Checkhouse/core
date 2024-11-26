package com.checkhouse.core.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.checkhouse.core.apiPayload.BaseResponse;
import com.checkhouse.core.dto.DeliveryDTO;
import com.checkhouse.core.dto.request.DeliveryRequest;
import com.checkhouse.core.service.DeliveryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "delivery apis", description = "배송 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/delivery")
public class DeliveryController {
    private final DeliveryService deliveryService;
    //배송 등록
    @Operation(summary = "배송 등록")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "등록 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping
    public BaseResponse<DeliveryDTO> addDelivery(
        @Valid @RequestBody DeliveryRequest.AddDeliveryRequest req) {
        try {
            log.info("[배송 등록] request: {}", req);
            return BaseResponse.onSuccess(deliveryService.addDelivery(req));
        } catch (Exception e) {
            log.error("[배송 등록] request: {}, error: {}", req, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //배송 상태 업데이트
    @Operation(summary = "배송 상태 업데이트")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "업데이트 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PatchMapping
    public BaseResponse<DeliveryDTO> updateDeliveryState(@RequestBody DeliveryRequest.UpdateDeliveryStateRequest req) {
        try {   
            log.info("[배송 상태 업데이트] request: {}", req);
            return BaseResponse.onSuccess(deliveryService.updateDeliveryState(req));
        } catch (Exception e) {
            log.error("[배송 상태 업데이트] request: {}, error: {}", req, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //배송 리스트 조회
    @Operation(summary = "배송 리스트 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping
    public BaseResponse<List<DeliveryDTO>> getDeliveryList() {
        log.info("[배송 리스트 조회]");
        return BaseResponse.onSuccess(deliveryService.getDeliveryList());
    }
    //송장 번호 등록
    @Operation(summary = "송장 번호 등록")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "등록 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/tracking-code")
    public BaseResponse<DeliveryDTO> registerTrackingCode(
        @Valid @RequestBody DeliveryRequest.RegisterTrackingCodeRequest req) {
        try {
            log.info("[송장 번호 등록] request: {}", req);
            return BaseResponse.onSuccess(deliveryService.registerTrackingCode(req));
        } catch (Exception e) {
            log.error("[송장 번호 등록] request: {}, error: {}", req, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //배송 삭제
    @Operation(summary = "배송 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @DeleteMapping
    public BaseResponse<Void> deleteDelivery(
        @Valid @RequestBody DeliveryRequest.DeleteDeliveryRequest req) {
        try {
            log.info("[배송 삭제] request: {}", req);
            deliveryService.deleteDelivery(req);
            return BaseResponse.onSuccess(null);
        } catch (Exception e) {
            log.error("[배송 삭제] request: {}, error: {}", req, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
}
