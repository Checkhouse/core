package com.checkhouse.core.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
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
        @RequestBody DeliveryRequest.AddDeliveryRequest req) {
        DeliveryDTO delivery = deliveryService.addDelivery(req);
        return BaseResponse.onSuccess(delivery);
    }
    //배송 상태 업데이트
    @Operation(summary = "배송 상태 업데이트")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "업데이트 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PatchMapping("/{deliveryId}")
    public BaseResponse<DeliveryDTO> updateDeliveryState(
        @RequestBody DeliveryRequest.UpdateDeliveryStateRequest req) {
        DeliveryDTO delivery = deliveryService.updateDeliveryState(req);
        return BaseResponse.onSuccess(delivery);
    }
    //배송 리스트 조회
    @Operation(summary = "배송 리스트 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/list")
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
    @PostMapping("/tracking-code/{deliveryId}")
    public BaseResponse<DeliveryDTO> registerTrackingCode(
        @RequestBody DeliveryRequest.RegisterTrackingCodeRequest req) {
        DeliveryDTO delivery = deliveryService.registerTrackingCode(req);
        return BaseResponse.onSuccess(delivery);
    }
    //배송 삭제
    @Operation(summary = "배송 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @DeleteMapping("/{deliveryId}")
    public BaseResponse<Void> deleteDelivery(
        @RequestBody DeliveryRequest.DeleteDeliveryRequest req) {
        log.info("[배송 삭제] request: {}", req);
        deliveryService.deleteDelivery(req);
        return BaseResponse.onSuccess(null);
    }
}
