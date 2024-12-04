package com.checkhouse.core.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.checkhouse.core.entity.es.OriginProductDocument;
import com.checkhouse.core.repository.es.OriginProductDocumentRepository;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.checkhouse.core.apiPayload.BaseResponse;
import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.ImageDTO;
import com.checkhouse.core.dto.OriginImageDTO;
import com.checkhouse.core.dto.OriginProductDTO;
import com.checkhouse.core.dto.OriginProductWithImagesDTO;
import com.checkhouse.core.dto.request.ImageRequest;
import com.checkhouse.core.dto.request.OriginProductRequest;
import com.checkhouse.core.entity.enums.DeliveryState;
import com.checkhouse.core.service.CollectService;
import com.checkhouse.core.service.ImageService;
import com.checkhouse.core.service.OriginProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Slf4j
@Tag(name = "origin-product apis", description = "원본 상품 관련 API - 원본 상품 등록, 원본 상품 정보 수정, 원본 상품 정보 조회, 원본 상품 목록 조회, 카테고리별 원본 상품 목록 조회, 원본 상품 검색, 원본 상품 삭제")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/origin-product")
public class OriginProductController {
    private final OriginProductService originProductService;
    private final ImageService imageService;
    private final CollectService collectService;
    private final OriginProductDocumentRepository originProductDocumentRepository;
    //원본 상품 등록
    @Operation(summary = "원본 상품 등록")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "등록 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping
    public BaseResponse<OriginProductDTO> addOriginProduct(
        @Valid @RequestBody OriginProductRequest.AddOriginProductRequest req) {
        // 1. 원본 상품 등록
        OriginProductDTO savedProduct = originProductService.addOriginProduct(req);
        
        // 2. 이미지 등록
        if (req.imageUrls() != null && !req.imageUrls().isEmpty()) {
            req.imageUrls().forEach(imageUrl -> {
                imageService.addOriginImage(
                    ImageRequest.AddOriginImageRequest.builder()
                        .originProductId(savedProduct.originProductId())
                        .imageURL(imageUrl)
                        .build()
                );
            });
        }

        originProductDocumentRepository.save(OriginProductDocument.builder()
                .originProductId(savedProduct.originProductId().toString())
                .title(savedProduct.name())
                .build()
        );

        
        log.info("[원본 상품 등록] request: {}", savedProduct);
        return BaseResponse.onSuccess(savedProduct);
    }
    //원본 상품 정보 수정
    @Operation(summary = "원본 상품 정보 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PatchMapping
    public BaseResponse<OriginProductDTO> updateOriginProductInfo(
        @Valid @RequestBody OriginProductRequest.UpdateOriginProductInfo req,
        @RequestParam(required = false) List<String> imageUrls  // 이미지 URL 리스트를 파라미터로 받음
    ) {
        // 1. 상품 정보 수정
        OriginProductDTO updatedOriginProduct = originProductService.updateOriginProductInfo(req);

        // 2. 이미지 수정 (이미지 URL이 제공된 경우에만)
        if (imageUrls != null && !imageUrls.isEmpty()) {
            // 기존 이미지 삭제
            List<OriginImageDTO> existingImages = imageService.getOriginImagesByOriginId(
                new ImageRequest.GetOriginImagesByOriginIdRequest(req.originProductId())
            );
            existingImages.forEach(image ->
                imageService.deleteOriginImage(new ImageRequest.DeleteOriginImageRequest(image.originImageId()))
            );

            // 새 이미지 등록
            imageUrls.forEach(imageUrl -> {
                imageService.addOriginImage(
                    ImageRequest.AddOriginImageRequest.builder()
                        .originProductId(req.originProductId())
                        .imageURL(imageUrl)
                        .build()
                );
            });
        }

        log.info("[원본 상품 정보 수정] request: {}", updatedOriginProduct);
        return BaseResponse.onSuccess(updatedOriginProduct);
    }
    //원본 상품 정보 조회
    @Operation(summary = "원본 상품 정보 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping
    public BaseResponse<OriginProductWithImagesDTO> getOriginProductInfo(
        @RequestParam UUID originProductId) {
        OriginProductRequest.GetOriginProductInfoRequest req = 
            new OriginProductRequest.GetOriginProductInfoRequest(originProductId);
        
        // 상품 정보와 이미지 정보 조회
        OriginProductDTO product = originProductService.getOriginProductInfo(req);
        List<OriginImageDTO> images = imageService.getOriginImagesByOriginId(
            new ImageRequest.GetOriginImagesByOriginIdRequest(originProductId)
        );
        
        return BaseResponse.onSuccess(OriginProductWithImagesDTO.of(product, images));
    }
    //원본 상품 목록 조회
    @Operation(summary = "원본 상품 목록 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping
    public BaseResponse<List<OriginProductDTO>> getOriginProducts() {
        log.info("[원본 상품 목록 조회]");
        List<OriginProductDTO> originProducts = originProductService.getOriginProducts();
        return BaseResponse.onSuccess(originProducts);
    }
    //카테고리별 원본 상품 목록 조회
    @Operation(summary = "카테고리별 원본 상품 목록 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/category")
    public BaseResponse<List<OriginProductDTO>> getOriginProductsWithCategory(
        @RequestParam UUID categoryId) {
        OriginProductRequest.GetOriginProductWithCategoryRequest req = new OriginProductRequest.GetOriginProductWithCategoryRequest(categoryId);
        log.info("[카테고리별 원본 상품 목록 조회] categoryId: {}", categoryId);
        List<OriginProductDTO> originProducts = originProductService.getOriginProductsWithCategory(req);
        return BaseResponse.onSuccess(originProducts);
    }
    //원본 상품 검색
    @Operation(summary = "원본 상품 검색")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/search")
    public BaseResponse<List<OriginProductDocument>> searchOriginProducts(
        @RequestParam("query") String query
    ) {
        log.info("[원본 상품 검색] request: {}", query);
        return BaseResponse.onSuccess(originProductService.searchOriginProducts(
            OriginProductRequest.SearchOriginProductsRequest.builder()
                .query(query)
                .build()
        ));
    }
    //원본 상품 삭제
    @Operation(summary = "원본 상품 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @DeleteMapping
    public BaseResponse<Void> deleteOriginProduct(
        @Valid @RequestBody OriginProductRequest.DeleteOriginProduct req) {
        log.info("[원본 상품 삭제] request: {}", req);
        originProductService.deleteOriginProduct(req);
        return BaseResponse.onSuccess(null);
    }
}
