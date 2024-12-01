package com.checkhouse.core.controller;

import java.util.List;
import java.util.UUID;

import com.checkhouse.core.dto.OriginProductDTO;
import com.checkhouse.core.dto.request.*;
import com.checkhouse.core.entity.OriginProduct;
import com.checkhouse.core.service.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.checkhouse.core.apiPayload.BaseResponse;
import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.dto.DeliveryDTO;
import com.checkhouse.core.dto.UsedProductDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import com.checkhouse.core.entity.enums.UsedProductState;
import com.checkhouse.core.entity.User;
import com.checkhouse.core.apiPayload.exception.GeneralException;

@Slf4j
@Tag(name = "used-product apis", description = "중고 상품 관련 API - 중고 상품 등록, 수정, 삭제, 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/used-products")
@Validated
public class UsedProductController {
    private final UsedProductService usedProductService;
    private final OriginProductService originProductService;
    private final DeliveryService deliveryService;
    private final InspectionService inspectionService;
    private final CollectService collectService;

    // 중고 상품 등록
    @Operation(summary = "중고 상품 등록")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "등록 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @Transactional
    @PostMapping
    public BaseResponse<UsedProductDTO> addUsedProduct(
        @Valid @RequestBody UsedProductRequest.AddUsedProductRequest req
    ) {
        log.info("[중고 상품 등록] request: {}", req);
        // 1. 중고 상품 등록
        UsedProductDTO savedProduct = usedProductService.addUsedProduct(req);

        // 2. 배송 정보 생성 - 프론트에서 받은 주소 ID 사용
        DeliveryDTO delivery = deliveryService.addDelivery(
            DeliveryRequest.AddDeliveryRequest.builder()
                .addressId(UUID.fromString(req.addressId()))
                .build()
        );

        // 3. 수거 정보 생성
        collectService.addCollect(
            CollectRequest.AddCollectRequest.builder()
                .usedProductId(savedProduct.usedProductId())
                .build()
        );

        return BaseResponse.onSuccess(savedProduct);
    }
    
    // 중고 상품 등록 상태 변경
    @Operation(summary = "중고 상품 등록 상태 변경")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "변경 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PatchMapping("/state")
    public BaseResponse<UsedProductDTO> updateUsedProductState(
        @Valid @RequestBody UsedProductRequest.UpdateUsedProductState req
    ) {
        log.info("[중고 상품 등록 상태 변경] request: {}", req);
        UsedProductDTO updatedProduct = usedProductService.updateUsedProductStatus(req);
        return BaseResponse.onSuccess(updatedProduct);
    }

    // 중고 상품 정보 조회
    @Operation(summary = "중고 상품 정보 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/{usedProductId}")
    public BaseResponse<UsedProductDTO> getUsedProduct(
        @PathVariable UUID usedProductId
    ) {
        UsedProductDTO product = usedProductService.getUsedProductDetails(
            UsedProductRequest.GetUsedProductRequest.builder()
                .usedProductId(usedProductId)
                .build()
        );
        return BaseResponse.onSuccess(product);
    }

    // 중고 상품 네고 상태 변경
    @Operation(summary = "중고 상품 네고 상태 변경")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "변경 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PatchMapping("/nego")
    public BaseResponse<UsedProductDTO> updateUsedProductNegoState(
        @Valid @RequestBody UsedProductRequest.UpdateUsedProductNegoState req
    ) {
        log.info("[중고 상품 네고 상태 변경] request: {}", req);
        UsedProductDTO updatedProduct = usedProductService.updateUsedProductNegoState(req);
        return BaseResponse.onSuccess(updatedProduct);
    }

    // 중고 상품 정보 수정
    @Operation(summary = "중고 상품 정보 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PatchMapping("/{usedProductId}")
    public BaseResponse<UsedProductDTO> updateUsedProductInfo(
        @PathVariable UUID usedProductId,
        @RequestBody UsedProductRequest.UpdateUsedProductInfo req
    ) {
        log.info("[중고 상품 정보 수정] request: {}", req);
        UsedProductDTO updatedProduct = usedProductService.updateUsedProductInfo(req);
        return BaseResponse.onSuccess(updatedProduct);
    }

    // 중고 상품 삭제
    @Operation(summary = "중고 상품 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @DeleteMapping("/{usedProductId}")
    public BaseResponse<Void> deleteUsedProduct(
        @PathVariable UUID usedProductId
    ) {
        usedProductService.cancelAddUsedProduct(
            UsedProductRequest.DeleteUsedProductRequest.builder()
                .usedProductId(usedProductId)
                .build()
        );
        return BaseResponse.onSuccess(null);
    }
    
    // 중고 상품 목록 조회(상태)
    @Operation(summary = "중고 상품 목록 조회(상태)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/list/{status}")
    public BaseResponse<List<UsedProductDTO>> getUsedProductsByStatus(
        @PathVariable UsedProductState status
    ) {
        List<UsedProductDTO> products = usedProductService.getUsedProductsByStatus(
            UsedProductRequest.GetUsedProductByStatusRequest.builder()
                .status(status)
                .build()
        );
        return BaseResponse.onSuccess(products);
    }

    // 중고 상품 목록 조회(유저)
    @Operation(summary = "중고 상품 목록 조회(유저)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/list/user/{userId}")
    public BaseResponse<List<UsedProductDTO>> getUsedProductsByUser(
        @PathVariable UUID userId
    ) {
        List<UsedProductDTO> products = usedProductService.getUsedProductsByUser(
            UsedProductRequest.GetUsedProductByUserRequest.builder()
                .userId(userId)
                .build()
        );
        return BaseResponse.onSuccess(products);
    }

    @Operation(summary = "원본 상품 별 중고 상품 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/origin/{originId}")
    public BaseResponse<List<UsedProductDTO>> getUsedProductWithOriginId(
            @PathVariable UUID originId
    ) {
        log.info("[ 원본 상품 별 중고 상품 조회 ]");
        OriginProduct originProduct = originProductService.findOriginProduct(OriginProductRequest.GetOriginProductInfoRequest.builder().originProductId(originId).build());
        List<UsedProductDTO> products = usedProductService.getUsedProductWithOriginId(originProduct.getOriginProductId());
        return BaseResponse.onSuccess(products);
    }
}
