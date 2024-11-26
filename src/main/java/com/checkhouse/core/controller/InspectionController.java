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
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@Tag(name = "inspection apis")
@RestController
@RequestMapping("inspection")
@RequiredArgsConstructor
public class InspectionController {
    private final InspectionService inspectionService;

    //검수 등록
    @PostMapping
    public BaseResponse<InspectionDTO> addInspection(@RequestBody InspectionRequest.AddInspectionRequest req) {
        return BaseResponse.onSuccess(inspectionService.addInspection(req));
    }
    //검수 상태 업데이트
    @PatchMapping
    public BaseResponse<InspectionDTO> updateInspection(@RequestBody InspectionRequest.UpdateInspectionRequest req) {
        return BaseResponse.onSuccess(inspectionService.updateInspection(req.inspectionId(), req));
    }
    //검수 삭제
    @DeleteMapping
    public BaseResponse<Void> deleteInspection(@RequestBody InspectionRequest.DeleteInspectionRequest req) {
        inspectionService.deleteInspection(req);
        return BaseResponse.onSuccess(null);
    }
    //검수 설명 수정
    @PatchMapping
    public BaseResponse<InspectionDTO> updateInspectionDescription(@RequestBody InspectionRequest.UpdateInspectionDescriptionRequest req) {
        return BaseResponse.onSuccess(inspectionService.updateInspectionDescription(req.inspectionId(), req));
    }
    //검수 리스트 조회
    @GetMapping
    public BaseResponse<List<InspectionDTO>> getInspectionList(@RequestParam UUID usedProductId) {
        return BaseResponse.onSuccess(inspectionService.getInspectionList(usedProductId));
    }
    //todo: 검수 사진 등록
}
