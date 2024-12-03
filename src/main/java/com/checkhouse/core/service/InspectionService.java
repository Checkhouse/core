package com.checkhouse.core.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.checkhouse.core.dto.InspectionDTO;
import com.checkhouse.core.dto.request.InspectionRequest;
import com.checkhouse.core.entity.Inspection;
import com.checkhouse.core.entity.UsedProduct;
import com.checkhouse.core.repository.mysql.InspectionRepository;
import com.checkhouse.core.repository.mysql.UsedProductRepository;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.repository.mysql.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class InspectionService {
    private final InspectionRepository inspectionRepository;
    private final UsedProductRepository usedProductRepository;
    private final UserRepository userRepository;
    /**
     * 검수 물품 등록
     */
    public InspectionDTO addInspection(InspectionRequest.AddInspectionRequest req) {
        // 판매 등록이 안된 상품은 검수 등록 불가
        if (req.usedProductId() == null) {
            throw new GeneralException(ErrorStatus._USED_PRODUCT_ID_NOT_FOUND);
        }
        
        // 판매 등록이 안된 상품은 검수 등록 불가
        UsedProduct usedProduct = usedProductRepository.findById(req.usedProductId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._USED_PRODUCT_ID_NOT_FOUND));
            
        // 이미 완료된 검수가 있는 상품은 검수 등록 불가
        inspectionRepository.findByUsedProductAndIsDone(usedProduct, true)
            .ifPresent(inspection -> {
                throw new GeneralException(ErrorStatus._INSPECTION_ALREADY_DONE);
            });
        // 이미 검수가 존재하는 상품은 검수 등록 불가
        inspectionRepository.findByUsedProduct(usedProduct)
            .ifPresent(inspection -> {
                throw new GeneralException(ErrorStatus._INSPECTION_ALREADY_EXISTS);
            });

        // 검수 등록 - isDone은 항상 false로 시작
        Inspection inspection = Inspection.builder()
                .usedProduct(usedProduct)
                .description(req.description())
                .isDone(false)  // 항상 false로 설정
                .user(userRepository.findById(req.userId())
                    .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND)))
                .build();
        Inspection savedInspection = inspectionRepository.save(inspection);
        return savedInspection.toDto();
    }
    // 검수 상태 업데이트
    public InspectionDTO updateInspectionState(
            InspectionRequest.UpdateInspectionStateRequest req
    ) {
        Inspection inspection = inspectionRepository.findById(req.inspectionId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._INSPECTION_ID_NOT_FOUND));
        if (inspection.isDone()) {
            throw new GeneralException(ErrorStatus._INSPECTION_ALREADY_DONE);
        }
        inspection.updateInspectionState(true);
        return inspectionRepository.save(inspection).toDto();
    }
    
    // 검수 리스트 조회(관리자)
    public List<InspectionDTO> getInspectionList(
            InspectionRequest.GetInspectionListRequest request
    ) {
        return inspectionRepository.findByUsedProduct_UsedProductId(request.usedProductId())
            .stream()
            .map(Inspection::toDto)
            .collect(Collectors.toList());
    }
    // 검수 삭제
    public void deleteInspection(InspectionRequest.DeleteInspectionRequest req) {
        Inspection inspection = inspectionRepository.findById(req.inspectionId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._INSPECTION_ID_NOT_FOUND));
        inspectionRepository.delete(inspection);
    }
    // 검수 설명 수정
    public InspectionDTO updateInspectionDescription(
            InspectionRequest.UpdateInspectionDescriptionRequest req
    ) {
        Inspection inspection = inspectionRepository.findById(req.inspectionId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._INSPECTION_ID_NOT_FOUND));
        if (inspection.isDone()) {
            throw new GeneralException(ErrorStatus._INSPECTION_ALREADY_DONE);
        }
        inspection.updateDescription(req.description());
        return inspectionRepository.save(inspection).toDto();
    }
}
