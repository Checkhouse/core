package com.checkhouse.core.controller;

import java.util.List;
import java.util.UUID;

import com.checkhouse.core.dto.*;
import com.checkhouse.core.dto.request.CollectRequest;
import com.checkhouse.core.dto.request.UsedProductRequest;
import com.checkhouse.core.entity.Collect;
import com.checkhouse.core.entity.Inspection;
import com.checkhouse.core.entity.enums.UsedProductState;
import com.checkhouse.core.service.CollectService;
import com.checkhouse.core.service.UsedProductService;
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
import com.checkhouse.core.dto.request.ImageRequest;
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
    private final UsedProductService usedProductService;
    private final CollectService collectService;

    // QR 스캔 후 검수 시작
    @Operation(summary = "검수 등록 (QR 스캔 후)")
    @PostMapping("/start")
    public BaseResponse<InspectionRequest.StartInspectionResponse> startInspection(
        @Valid @RequestBody InspectionRequest.AddInspectionRequest req
    ) {
        InspectionDTO inspectionDTO = inspectionService.addInspection(req);
        List<UsedImageDTO> imagelist = imageService.getUsedImagesByUsedId(
                ImageRequest.GetUsedImagesByUsedIdRequest.builder()
                        .usedProductId(req.usedProductId())
                        .build()
        );

        // Collect 상태 업데이트
        CollectDTO collectDTO = collectService.findByUsedProduct(
            CollectRequest.GetCollectByUsedProductRequest.builder()
                .usedProductId(req.usedProductId())
                .build()
        );
        collectService.updateCollectCompleted(
            CollectRequest.UpdateCollectCompleted.builder()
                .collectId(collectDTO.collectId())
                .build()
        );

        return BaseResponse.onSuccess(InspectionRequest.StartInspectionResponse.builder()
                .inspectionId(inspectionDTO.inspectionId())
                .usedProductName(inspectionDTO.usedProductDTO().title())
                .usedProductDescription(inspectionDTO.usedProductDTO().description())
                .usedImages(imagelist)
                .build());
    }

    // 검수 완료 후 사진 등록, 상태 업데이트, 노트 업데이트
    @Operation(summary = "검수 완료 후 사진 등록, 상태 업데이트, 노트 업데이트")
    @PostMapping("/finish")
    public BaseResponse<InspectionDTO> updateInspection(
        @Valid @RequestBody InspectionRequest.UpdateInspectionRequest req
    ) {
        // 검수 이미지들 등록
        for (int i = 0; i < req.imageURL().size(); i++) {
            imageService.addInspectionImage(
                ImageRequest.AddInspectionImageRequest.builder()
                    .inspectionId(req.inspectionId())
                    .imageURL(req.imageURL().get(i))
                    .usedImageId(req.usedImageId().get(i))
                    .build()
            );
        }
        
        // 검수 노트 업데이트
        inspectionService.updateInspectionDescription(
            InspectionRequest.UpdateInspectionDescriptionRequest.builder()
                .inspectionId(req.inspectionId())
                .description(req.description())
                .build()
        );
        // 검수 상태 업데이트
        InspectionDTO inspection = inspectionService.updateInspectionState(
                InspectionRequest.UpdateInspectionStateRequest.builder()
                        .inspectionId(req.inspectionId())
                        .build());

        usedProductService.updateUsedProductStatus(
                UsedProductRequest.UpdateUsedProductState.builder()
                        .usedProductId(inspection.usedProductDTO().usedProductId())
                        .status(UsedProductState.ON_SALE)
                        .build());

        //TODO: ES 검색 상태 변경

        return BaseResponse.onSuccess(inspection);
    }

    // 검수 리스트 조회
    @Operation(summary = "검수 리스트 조회")
    @GetMapping("/list")
    public BaseResponse<List<InspectionDTO>> getInspectionList(
        @RequestParam UUID usedProductId
    ) {
        return BaseResponse.onSuccess(inspectionService.getInspectionList(
            InspectionRequest.GetInspectionListRequest.builder()
                .usedProductId(usedProductId)
                .build()
        ));
    }

    // 검수 삭제
    @Operation(summary = "검수 삭제")
    @DeleteMapping("/{inspectionId}")
    public BaseResponse<Void> deleteInspection(
        @PathVariable UUID inspectionId
    ) {
        inspectionService.deleteInspection(
            InspectionRequest.DeleteInspectionRequest.builder()
                .inspectionId(inspectionId)
                .build()
        );
        return BaseResponse.onSuccess(null);
    }
}
