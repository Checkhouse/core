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
import com.checkhouse.core.dto.InspectionDTO;
import com.checkhouse.core.dto.request.InspectionRequest;
import com.checkhouse.core.service.InspectionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Slf4j
@Tag(name = "inspection apis", description = "검수 관련 API - 검수 등록, 검수 상태 업데이트, 검수 삭제, 검수 설명 수정, 검수 리스트 조회, 검수 사진 등록")
@RestController
@RequestMapping("api/v1/inspection")
@RequiredArgsConstructor
public class InspectionController {
    private final InspectionService inspectionService;

    //검수 등록
    @Operation(summary = "검수 등록")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "등록 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping
    public BaseResponse<InspectionDTO> addInspection(
        @Valid @RequestBody InspectionRequest.AddInspectionRequest req) {
        try {
            log.info("[검수 등록] request: {}", req);
            return BaseResponse.onSuccess(inspectionService.addInspection(req));
        } catch (Exception e) {
            log.error("[검수 등록] request: {}, error: {}", req, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //검수 상태 업데이트
    @Operation(summary = "검수 상태 업데이트")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "업데이트 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PatchMapping("/state/{inspectionId}")
    public BaseResponse<InspectionDTO> updateInspection(
        @Valid @RequestBody InspectionRequest.UpdateInspectionRequest req) {
        try {
            log.info("[검수 상태 업데이트] request: {}", req);
            return BaseResponse.onSuccess(inspectionService.updateInspection(req.inspectionId(), req));
        } catch (Exception e) {
            log.error("[검수 상태 업데이트] request: {}, error: {}", req, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //검수 삭제
    @Operation(summary = "검수 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @DeleteMapping("/{inspectionId}")
    public BaseResponse<Void> deleteInspection(
        @Valid @RequestBody InspectionRequest.DeleteInspectionRequest req) {
        try {
            log.info("[검수 삭제] request: {}", req);
            inspectionService.deleteInspection(req);
            return BaseResponse.onSuccess(null);
        } catch (Exception e) {
            log.error("[검수 삭제] request: {}, error: {}", req, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //검수 설명 수정
    @Operation(summary = "검수 설명 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PatchMapping("/description/{inspectionId}")
    public BaseResponse<InspectionDTO> updateInspectionDescription(
        @Valid @RequestBody InspectionRequest.UpdateInspectionDescriptionRequest req) {
        try {
            log.info("[검수 설명 수정] request: {}", req);
            return BaseResponse.onSuccess(inspectionService.updateInspectionDescription(req.inspectionId(), req));
        } catch (Exception e) {
            log.error("[검수 설명 수정] request: {}, error: {}", req, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //검수 리스트 조회
    @Operation(summary = "검수 리스트 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/list/{usedProductId}")
    public BaseResponse<List<InspectionDTO>> getInspectionList(
        @RequestParam UUID usedProductId) {
        try {
            log.info("[검수 리스트 조회] request: {}", usedProductId);
            return BaseResponse.onSuccess(inspectionService.getInspectionList(usedProductId));
        } catch (Exception e) {
            log.error("[검수 리스트 조회] request: {}, error: {}", usedProductId, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //todo: 검수 사진 등록
}
