package com.checkhouse.core.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.checkhouse.core.dto.InspectionDTO;
import com.checkhouse.core.entity.Inspection;
import com.checkhouse.core.repository.mysql.InspectionRepository;
import com.checkhouse.core.request.InspectionRequest;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.apiPayload.code.status.ErrorStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class InspectionService {
    private final InspectionRepository inspectionRepository;
    // private final UsedProductRepository usedProductRepository;
    /**
     * 검수 물품 등록
     */
    InspectionDTO createInspection(InspectionRequest.CreateInspectionRequest req) {
        // 판매 등록이 안된 상품은 검수 등록 불가
        // UsedProduct usedProduct = usedProductRepository.findById(req.usedProductId())
        //         .orElseThrow(() -> new GeneralException(ErrorStatus._USED_PRODUCT_ID_NOT_FOUND));

        Inspection inspection = Inspection.builder()
                //.usedProduct(usedProduct)
                .description(req.description())
                .isDone(req.isDone())
                .build();
        return inspection.toDTO();
        // 검수 등록
        //inspectionRepository.save(inspection);    
    }
    // 검수 상태 업데이트
    InspectionDTO updateInspection(UUID inspectionId, InspectionRequest.UpdateInspectionRequest req) {
        // 검수 상태 변경
        Inspection inspection = inspectionRepository.findById(inspectionId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._INSPECTION_ID_NOT_FOUND));
        inspectionRepository.update(inspectionId, req.isDone());
        return inspection.toDTO();
    }
    // 검수 사진 등록(상태 업데이트)
    InspectionDTO updateInspectionImage(UUID inspectionId, InspectionRequest.UpdateInspectionImageRequest req) {
        return null;
    }
    // 검수 리스트 조회(관리자)
    List<InspectionDTO> getInspectionList(UUID usedProductId) {
        return inspectionRepository.findByUsedProductId(usedProductId)
            .stream()
            .map(Inspection::toDTO)
            .collect(Collectors.toList());
    }
}
