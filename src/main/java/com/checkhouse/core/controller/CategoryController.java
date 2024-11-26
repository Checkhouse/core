package com.checkhouse.core.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.checkhouse.core.apiPayload.BaseResponse;
import com.checkhouse.core.dto.CategoryDTO;
import com.checkhouse.core.dto.request.CategoryRequest;
import com.checkhouse.core.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "category apis", description = "카테고리 관련 API - 카테고리 조회")
@RestController
@RequestMapping("api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    //카테고리 등록
    @Operation(summary = "카테고리 등록")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "등록 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping
    public BaseResponse<CategoryDTO> addCategory(
       @Valid @RequestBody CategoryRequest.AddCategoryRequest req) {
        try {
            log.info("[카테고리 등록] request: {}", req);
            return BaseResponse.onSuccess(categoryService.addCategory(req));
        } catch (Exception e) {
            log.error("[카테고리 등록] request: {}, error: {}", req, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //카테고리 상세보기
    @Operation(summary = "카테고리 상세보기")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/{categoryId}")
    public BaseResponse<CategoryDTO> getCategory(
        @Valid @RequestBody CategoryRequest.GetCategoryByIdRequest req) {
        try {
            log.info("[카테고리 상세보기] request: {}", req);
            return BaseResponse.onSuccess(categoryService.getCategory(req));
        } catch (Exception e) {
            log.error("[카테고리 상세보기] request: {}, error: {}", req, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //카테고리 수정
    @Operation(summary = "카테고리 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PutMapping("/{categoryId}")
    public BaseResponse<CategoryDTO> updateCategory(
        @Valid @RequestBody CategoryRequest.UpdateCategoryByIdRequest req) {
        try {
            log.info("[카테고리 수정] request: {}", req);
            return BaseResponse.onSuccess(categoryService.updateCategoryById(req));
        } catch (Exception e) {
            log.error("[카테고리 수정] request: {}, error: {}", req, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //카테고리 삭제
    @Operation(summary = "카테고리 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @DeleteMapping("/{categoryId}")
    public BaseResponse<Void> deleteCategory(
        @Valid @RequestBody CategoryRequest.DeleteCategoryRequest req) {
        try {
            log.info("[카테고리 삭제] request: {}", req);
            categoryService.deleteCategory(req);
            return BaseResponse.onSuccess(null);
        } catch (Exception e) {
            log.error("[카테고리 삭제] request: {}, error: {}", req, e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    //카테고리 조회
    @Operation(summary = "카테고리 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/list")
    public BaseResponse<List<CategoryDTO>> getCategoryList() {
        try {
            log.info("[카테고리 조회]");
            return BaseResponse.onSuccess(categoryService.getCategories());
        } catch (Exception e) {
            log.error("[카테고리 조회] error: {}", e.getMessage());
            return BaseResponse.onFailure(e.getMessage(), e.getMessage(), null);
        }
    }
    
}
