package com.checkhouse.core.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.checkhouse.core.apiPayload.BaseResponse;
import com.checkhouse.core.dto.UsedProductDTO;
import com.checkhouse.core.dto.request.UsedProductRequest;
import com.checkhouse.core.service.UsedProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@Tag(name = "used-product apis")
@RestController
@RequiredArgsConstructor
@RequestMapping("used-product")
public class UsedProductController {
    private final UsedProductService usedProductService;
    //중고 상품 등록
    @PostMapping
    public BaseResponse<UsedProductDTO> addUsedProduct(@RequestBody UsedProductRequest.AddUsedProductRequest req) {
        return BaseResponse.onSuccess(usedProductService.addUsedProduct(req));
    }
    //중고 상품 정보 수정
    @PutMapping
    public BaseResponse<UsedProductDTO> updateUsedProduct(@RequestBody UsedProductRequest.UpdateUsedProductInfo req) {
        return BaseResponse.onSuccess(usedProductService.updateUsedProductInfo(req));
    }
    //중고 상품 네고 상태 수정
    @PutMapping("/nego")
    public BaseResponse<UsedProductDTO> updateUsedProductNegoState(@RequestBody UsedProductRequest.UpdateUsedProductNegoState req) {
        return BaseResponse.onSuccess(usedProductService.updateUsedProductNegoState(req));
    }
    //중고 상품 상태 수정
    @PutMapping("/state")
    public BaseResponse<UsedProductDTO> updateUsedProductStatus(@RequestBody UsedProductRequest.UpdateUsedProductState req) {
        return BaseResponse.onSuccess(usedProductService.updateUsedProductStatus(req));
    }
    //중고 상품 취소
    @DeleteMapping
    public BaseResponse<Void> cancelUsedProduct(@RequestParam UUID usedProductId) {
        usedProductService.cancelAddUsedProduct(usedProductId);
        return BaseResponse.onSuccess(null);
    }
    //중고 상품 상세 조회
    @GetMapping
    public BaseResponse<UsedProductDTO> getUsedProductDetails(@RequestParam UUID usedProductId) {
        return BaseResponse.onSuccess(usedProductService.getUsedProductDetails(usedProductId));
    }
    //중고 상품 상태별 목록 조회
    @GetMapping("/list")
    public BaseResponse<List<UsedProductDTO>> getUsedProductsByStatus(@RequestParam String status) {
        return BaseResponse.onSuccess(usedProductService.getUsedProductsByStatus("ACTIVE"));
    }
    //중고 상품 유저별 목록 조회
    @GetMapping("/user")
    public BaseResponse<List<UsedProductDTO>> getUsedProductsByUser(@RequestParam UUID userId) {
        return BaseResponse.onSuccess(usedProductService.getUsedProductsByUser(userId));
    }
    //중고 상품 유저별 중고상품 유저id 확인
    @GetMapping("/user/id")
    public BaseResponse<UUID> getUserIdByUsedProduct(@RequestParam UUID usedProductId) {
        return BaseResponse.onSuccess(usedProductService.findUsedProduct(usedProductId).getUser().getUserId());
    }
}
