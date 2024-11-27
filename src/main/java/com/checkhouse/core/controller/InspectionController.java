package com.checkhouse.core.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        InspectionDTO inspection = inspectionService.addInspection(req);
        return BaseResponse.onSuccess(inspection);
    }
    //검수 상태 업데이트
    @Operation(summary = "검수 상태 업데이트")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "업데이트 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PatchMapping("/state/{inspectionId}")
    public BaseResponse<InspectionDTO> updateInspection(
        @PathVariable UUID inspectionId,
        @Valid @RequestBody InspectionRequest.UpdateInspectionRequest req) {
        InspectionDTO inspection = inspectionService.updateInspection(inspectionId, req);
        return BaseResponse.onSuccess(inspection);
    }
    //검수 삭제
    @Operation(summary = "검수 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @DeleteMapping("/{inspectionId}")
    public BaseResponse<Void> deleteInspection(
        @PathVariable UUID inspectionId,
        @Valid @RequestBody InspectionRequest.DeleteInspectionRequest req) {
        inspectionService.deleteInspection(req);
        return BaseResponse.onSuccess(null);
    }
    //검수 설명 수정
    @Operation(summary = "검수 설명 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PatchMapping("/description/{inspectionId}")
    public BaseResponse<InspectionDTO> updateInspectionDescription(
        @PathVariable UUID inspectionId,
        @Valid @RequestBody InspectionRequest.UpdateInspectionDescriptionRequest req) {
        InspectionDTO inspection = inspectionService.updateInspectionDescription(inspectionId, req);
        return BaseResponse.onSuccess(inspection);
    }
    //검수 리스트 조회
    @Operation(summary = "검수 리스트 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/list")
    public BaseResponse<List<InspectionDTO>> getInspectionList(
        @PathVariable UUID usedProductId) {
        List<InspectionDTO> inspectionList = inspectionService.getInspectionList(usedProductId);
        return BaseResponse.onSuccess(inspectionList);
    }
    //todo: 검수 사진 등록
}
