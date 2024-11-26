package com.checkhouse.core.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.checkhouse.core.apiPayload.BaseResponse;
import com.checkhouse.core.dto.OriginProductDTO;
import com.checkhouse.core.dto.request.OriginProductRequest;
import com.checkhouse.core.service.OriginProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@Tag(name = "origin-product apis")
@RestController
@RequiredArgsConstructor
@RequestMapping("/origin-product")
public class OriginProductController {
    private final OriginProductService originProductService;
    //원본 상품 등록
    @PostMapping
    public BaseResponse<OriginProductDTO> addOriginProduct(@RequestBody OriginProductRequest.AddOriginProductRequest req) {
        return BaseResponse.onSuccess(originProductService.addOriginProduct(req));
    }
    //원본 상품 정보 수정
    @PutMapping
    public BaseResponse<OriginProductDTO> updateOriginProductInfo(@RequestBody OriginProductRequest.UpdateOriginProductInfo req) {
        return BaseResponse.onSuccess(originProductService.updateOriginProductInfo(req));
    }
    //원본 상품 정보 조회
    @GetMapping
    public BaseResponse<OriginProductDTO> getOriginProductInfo(@RequestParam UUID originProductId) {
        return BaseResponse.onSuccess(originProductService.getOriginProductInfo(originProductId));
    }
    //원본 상품 목록 조회
    @GetMapping("/list")
    public BaseResponse<List<OriginProductDTO>> getOriginProducts() {
        return BaseResponse.onSuccess(originProductService.getOriginProducts());
    }
    //카테고리별 원본 상품 목록 조회
    @GetMapping("/category")
    public BaseResponse<List<OriginProductDTO>> getOriginProductsWithCategory(@RequestParam UUID categoryId) {
        return BaseResponse.onSuccess(originProductService.getOriginProductsWithCategory(categoryId));
    }
    //원본 상품 검색
    @GetMapping("/search")
    public BaseResponse<List<OriginProductDTO>> searchOriginProducts(@RequestParam String query) {
        return BaseResponse.onSuccess(originProductService.searchOriginProducts(query));
    }
    //원본 상품 삭제
    @DeleteMapping
    public BaseResponse<Void> deleteOriginProduct(@RequestParam UUID originProductId) {
        OriginProductRequest.DeleteOriginProduct request = new OriginProductRequest.DeleteOriginProduct(originProductId);
        originProductService.deleteOriginProduct(request);
        return BaseResponse.onSuccess(null);
    }
}
