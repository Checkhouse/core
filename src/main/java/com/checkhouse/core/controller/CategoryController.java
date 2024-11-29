package com.checkhouse.core.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        log.info("[카테고리 등록] request: {}", req);
        return BaseResponse.onSuccess(categoryService.addCategory(req));
    }
    //카테고리 수정
    @Operation(summary = "카테고리 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PatchMapping("/{categoryId}")
    public BaseResponse<CategoryDTO> updateCategory(
        @Valid @RequestBody CategoryRequest.UpdateCategoryByIdRequest req) {
        log.info("[카테고리 수정] request: {}", req);
        return BaseResponse.onSuccess(categoryService.updateCategoryById(req));
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
        log.info("[카테고리 삭제] request: {}", req);
        categoryService.deleteCategory(req);
        return BaseResponse.onSuccess(null);
    }
    //카테고리 조회
    @Operation(summary = "카테고리 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/list")
    public BaseResponse<List<CategoryDTO>> getCategoryList() {
        log.info("[카테고리 조회]");
        return BaseResponse.onSuccess(categoryService.getCategories());
    }
    
}
