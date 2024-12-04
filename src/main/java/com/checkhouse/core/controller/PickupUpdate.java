package com.checkhouse.core.controller;

import com.checkhouse.core.apiPayload.BaseResponse;
import com.checkhouse.core.dto.PickupDTO;
import com.checkhouse.core.dto.request.PickupRequest;
import com.checkhouse.core.dto.request.StoreRequest;
import com.checkhouse.core.service.PickupService;
import com.checkhouse.core.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "pickup update apis", description = "픽업 상태 업데이트 기능만 (관리자 로그인 없이)")
@RestController
@RequestMapping("api/v1/pickup-update")
@RequiredArgsConstructor
public class PickupUpdate {
    private final PickupService pickupService;
    private final StoreService storeService;

    @PatchMapping
    @Operation(summary = "픽업 상태 변경")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "변경 성공")
    })
    public BaseResponse<PickupDTO> verifyPickup(
            @Valid @RequestBody PickupRequest.UpdatePickUpRequest req
    ) {
        // 관리자 코드 확인
        storeService.verifyCode(new StoreRequest.VerifyCodeRequest(
                req.storeId(),
                req.code()
        ));
        log.info("[픽업 상태 변경] request: {}", req);
        PickupDTO pickup = pickupService.updatePickup(req);
        return BaseResponse.onSuccess(pickup);
    }
}
