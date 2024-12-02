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
import com.checkhouse.core.service.ImageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Slf4j
@Tag(name = "inspection apis", description = "검수 관련 API")
@RestController
@RequestMapping("api/v1/inspection")
@RequiredArgsConstructor
public class InspectionController {
    private final InspectionService inspectionService;
    private final ImageService imageService;

    // QR 스캔 후 검수 시작
    @Operation(summary = "검수 등록 (QR 스캔 후)")
    @PostMapping("/start")
    public BaseResponse<InspectionDTO> startInspection(
        @Valid @RequestBody InspectionRequest.AddInspectionRequest req
    ) {
        return BaseResponse.onSuccess(inspectionService.addInspection(req));
    }

    // todo: 검수 이미지 등록
    // @Operation(summary = "검수 이미지 등록")
    // @PostMapping("/{inspectionId}/images")
    // public BaseResponse<InspectionDTO> addInspectionImages(
    //     @PathVariable UUID inspectionId,
    //     @Valid @RequestBody InspectionRequest.AddInspectionImagesRequest req
    // ) {
    //     return BaseResponse.onSuccess(inspectionService.addInspectionImages(inspectionId, req));
    // }

    // 검수 노트(설명) 작성
    @Operation(summary = "검수완료 후 노트 작성")
    @PatchMapping("/{inspectionId}/description")
    public BaseResponse<InspectionDTO> updateInspectionDescription(
        @PathVariable UUID inspectionId,
        @Valid @RequestBody InspectionRequest.UpdateInspectionDescriptionRequest req
    ) {
        return BaseResponse.onSuccess(inspectionService.updateInspectionDescription(
            InspectionRequest.UpdateInspectionDescriptionRequest.builder()
                .inspectionId(inspectionId)
                .description(req.description())
                .build()
        ));
    }
    // 검수 완료 후 상태 업데이트
    @Operation(summary = "검수 완료 후 상태 업데이트")
    @PatchMapping("/{inspectionId}/complete")
    public BaseResponse<InspectionDTO> updateInspectionState(
        @PathVariable UUID inspectionId,
        @Valid @RequestBody InspectionRequest.UpdateInspectionRequest req
    ) {
        return BaseResponse.onSuccess(inspectionService.updateInspection(
            InspectionRequest.UpdateInspectionRequest.builder()
                .inspectionId(inspectionId)
                .isDone(req.isDone())
                .build()
        ));
    }

    // 검수 리스트 조회
    @Operation(summary = "검수 리스트 조회")
    @GetMapping("/list")
    public BaseResponse<List<InspectionDTO>> getInspectionList(
        @RequestParam UUID usedProductId
    ) {
        InspectionRequest.GetInspectionListRequest req = new InspectionRequest.GetInspectionListRequest(usedProductId);
        return BaseResponse.onSuccess(inspectionService.getInspectionList(req));
    }
}
