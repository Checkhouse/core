package com.checkhouse.core.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;

import com.checkhouse.core.apiPayload.BaseResponse;
import com.checkhouse.core.dto.CollectDTO;
import com.checkhouse.core.dto.request.CollectRequest;
import com.checkhouse.core.entity.enums.DeliveryState;
import com.checkhouse.core.service.CollectService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Slf4j
@Tag(name = "collect apis", description = "수거 관련 API - 수거 등록, 수거 상태 업데이트, 수거 리스트 조회, 수거 삭제")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/collect")
public class CollectController {
    private final CollectService collectService;

    //수거 등록
    @Operation(summary = "수거 등록")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "등록 성공")
    })
    @PostMapping
    public BaseResponse<CollectDTO> addCollect(
        @Valid @RequestBody CollectRequest.AddCollectRequest req) {
        log.info("[수거 등록] request: {}", req);
        CollectDTO collect = collectService.addCollect(req);
        return BaseResponse.onSuccess(collect);
    }
    //수거 상태 업데이트
    @Operation(summary = "수거 상태 업데이트")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정 성공")
    })
    @PatchMapping
    public BaseResponse<CollectDTO> updateCollectState(
        @Valid @RequestBody CollectRequest.UpdateCollectRequest req) {
        log.info("[수거 상태 업데이트] request: {}", req);
        CollectDTO collect = collectService.updateCollect(req);
        return BaseResponse.onSuccess(collect);
    }

    //수거 리스트 조회
    @Operation(summary = "수거 리스트 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping
    public BaseResponse<List<CollectDTO>> getCollectList() {
        log.info("[수거 리스트 조회]");
        List<CollectDTO> collectList = collectService.getCollectList();
        return BaseResponse.onSuccess(collectList);
    }
    //수거 삭제
    @Operation(summary = "수거 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공")
    })
    @DeleteMapping
    public BaseResponse<Void> deleteCollect(
        @Valid @RequestBody CollectRequest.DeleteCollectRequest req) {
        log.info("[수거 삭제] request: {}", req);
        collectService.deleteCollect(req);
        return BaseResponse.onSuccess(null);
    }
    // 수거 상태 조회
    @Operation(summary = "수거 상태 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/state")
    public BaseResponse<DeliveryState> getCollectState(
        @RequestParam UUID collectId) {
        CollectRequest.GetCollectStateRequest req = new CollectRequest.GetCollectStateRequest(
            collectId
        );
        log.info("[수거 상태 조회] request: {}", req);
        DeliveryState collectState = collectService.getCollectState(req);
        return BaseResponse.onSuccess(collectState);
    }
}
