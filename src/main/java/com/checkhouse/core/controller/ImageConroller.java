package com.checkhouse.core.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.checkhouse.core.apiPayload.BaseResponse;
import com.checkhouse.core.dto.ImageDTO;
import com.checkhouse.core.dto.OriginImageDTO;
import com.checkhouse.core.dto.UsedImageDTO;
import com.checkhouse.core.dto.request.ImageRequest;
import com.checkhouse.core.service.ImageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@Tag(name = "이미지", description = "이미지 API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/images")
public class ImageConroller {
    private final ImageService imageService;

    //이미지 추가
    @Operation(summary = "이미지 추가")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "추가 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping
    public BaseResponse<ImageDTO> addImage(
        @Valid @RequestBody ImageRequest.AddImageRequest req) {
        log.info("[이미지 추가] request: {}", req);
        return BaseResponse.onSuccess(imageService.addImage(req));
    }
    //이미지 조회
    @Operation(summary = "이미지 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/{imageId}")
    public BaseResponse<ImageDTO> getImage(
        @Valid @RequestBody ImageRequest.GetImageRequest req) {
        log.info("[이미지 조회] request: {}", req);
        return BaseResponse.onSuccess(imageService.getImage(req));
    }
    //이미지 삭제
    @Operation(summary = "이미지 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @DeleteMapping("/{imageId}")
    public BaseResponse<Void> deleteImage(
        @Valid @RequestBody ImageRequest.DeleteImageRequest req) {
        log.info("[이미지 삭제] request: {}", req);
        imageService.deleteImage(req);
        return BaseResponse.onSuccess(null);
    }
    //원본 이미지 추가
    @Operation(summary = "원본 이미지 추가")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "추가 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/origin")
    public BaseResponse<OriginImageDTO> addOriginImage(
        @Valid @RequestBody ImageRequest.AddOriginImageRequest req) {
        log.info("[원본 이미지 추가] request: {}", req);
        return BaseResponse.onSuccess(imageService.addOriginImage(req));
    }
    //원본 이미지 조회
    @Operation(summary = "원본 이미지 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/origin/{originImageId}")
    public BaseResponse<OriginImageDTO> getOriginImage(
        @Valid @RequestBody ImageRequest.GetOriginImageRequest req) {
        log.info("[원본 이미지 조회] request: {}", req);
        return BaseResponse.onSuccess(imageService.getOriginImage(req));
    }
    //원본 이미지 삭제
    @Operation(summary = "원본 이미지 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @DeleteMapping("/origin/{originImageId}")
    public BaseResponse<Void> deleteOriginImage(
        @Valid @RequestBody ImageRequest.DeleteOriginImageRequest req) {
        log.info("[원본 이미지 삭제] request: {}", req);
        imageService.deleteOriginImage(req);
        return BaseResponse.onSuccess(null);
    }
    //원본 상품 이미지 조회
    @Operation(summary = "원본 상품 이미지 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/origin/{originProductId}")
    public BaseResponse<List<OriginImageDTO>> getOriginImagesByOriginId(
        @Valid @RequestBody ImageRequest.GetOriginImagesByOriginIdRequest req) {
        log.info("[원본 상품 이미지 조회] request: {}", req);
        return BaseResponse.onSuccess(imageService.getOriginImagesByOriginId(req));
    }
    //중고 상품 이미지 추가
    @Operation(summary = "중고 상품 이미지 추가")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "추가 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/used")
    public BaseResponse<UsedImageDTO> addUsedImage(
        @Valid @RequestBody ImageRequest.AddUsedImageRequest req) {
        log.info("[중고 상품 이미지 추가] request: {}", req);
        return BaseResponse.onSuccess(imageService.addUsedImage(req));
    }
    //중고 상품 이미지 조회
    @Operation(summary = "중고 상품 이미지 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/used/{usedImageId}")
    public BaseResponse<UsedImageDTO> getUsedImage(
        @Valid @RequestBody ImageRequest.GetUsedImageRequest req) {
        log.info("[중고 상품 이미지 조회] request: {}", req);
        return BaseResponse.onSuccess(imageService.getUsedImage(req));
    }
    //중고 상품 리스트 조회
    @Operation(summary = "중고 상품 리스트 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/used/{usedProductId}")
    public BaseResponse<List<UsedImageDTO>> getUsedImagesByUsedId(
        @Valid @RequestBody ImageRequest.GetUsedImagesByUsedIdRequest req) {
        log.info("[중고 상품 리스트 조회] request: {}", req);
        return BaseResponse.onSuccess(imageService.getUsedImagesByUsedId(req));
    }
    //중고 상품 이미지 삭제
    @Operation(summary = "중고 상품 이미지 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @DeleteMapping("/used/{usedImageId}")
    public BaseResponse<Void> deleteUsedImage(
        @Valid @RequestBody ImageRequest.DeleteUsedImageRequest req) {
        log.info("[중고 상품 이미지 삭제] request: {}", req);
        imageService.deleteUsedImage(req);
        return BaseResponse.onSuccess(null);
    }
    // todo: 검수 이미지 추가
}
