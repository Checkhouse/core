package com.checkhouse.core.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;

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
public class ImageController {
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
        ImageDTO image = imageService.addImage(req);
        return BaseResponse.onSuccess(image);
    }
    //이미지 조회
    @Operation(summary = "이미지 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping
    public BaseResponse<ImageDTO> getImage(
        @RequestParam UUID imageId) {
        ImageRequest.GetImageRequest req = new ImageRequest.GetImageRequest(imageId);
        log.info("[이미지 조회] request: {}", req);
        return BaseResponse.onSuccess(imageService.getImage(req));
    }
    //이미지 삭제
    @Operation(summary = "이미지 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @DeleteMapping
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
        OriginImageDTO originImage = imageService.addOriginImage(req);
        return BaseResponse.onSuccess(originImage);
    }
    //원본 이미지 조회
    @Operation(summary = "원본 이미지 조회/이미지 ID로 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/origin")
    public BaseResponse<OriginImageDTO> getOriginImage(
        @RequestParam UUID originImageId) {
        ImageRequest.GetOriginImageRequest req = new ImageRequest.GetOriginImageRequest(originImageId);
        log.info("[원본 이미지 조회] request: {}", req);
        OriginImageDTO originImage = imageService.getOriginImage(req);
        return BaseResponse.onSuccess(originImage);
    }
    //원본 상품 이미지 조회
    @Operation(summary = "원본 상품 이미지 조회/원본 상품 ID로 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/origin/product")
    public BaseResponse<List<OriginImageDTO>> getOriginImagesByOriginId(
        @RequestParam UUID originProductId) {
        ImageRequest.GetOriginImagesByOriginIdRequest req = new ImageRequest.GetOriginImagesByOriginIdRequest(originProductId);
        log.info("[원본 상품 이미지 조회] request: {}", req);
        return BaseResponse.onSuccess(imageService.getOriginImagesByOriginId(req));
    }
    //원본 이미지 삭제
    @Operation(summary = "원본 이미지 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @DeleteMapping("/origin")
    public BaseResponse<Void> deleteOriginImage(
        @Valid @RequestBody ImageRequest.DeleteOriginImageRequest req) {
        log.info("[원본 이미지 삭제] request: {}", req);
        imageService.deleteOriginImage(req);
        return BaseResponse.onSuccess(null);
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
        UsedImageDTO usedImage = imageService.addUsedImage(req);
        return BaseResponse.onSuccess(usedImage);
    }
    //중고 상품 이미지 조회
    @Operation(summary = "중고 상품 이미지 조회/이미지 ID로 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/used")
    public BaseResponse<UsedImageDTO> getUsedImage(
        @RequestParam UUID usedImageId) {
        ImageRequest.GetUsedImageRequest req = new ImageRequest.GetUsedImageRequest(usedImageId);
        log.info("[중고 상품 이미지 조회] request: {}", req);
        UsedImageDTO usedImage = imageService.getUsedImage(req);
        return BaseResponse.onSuccess(usedImage);
    }
    //중고 상품 리스트 조회
    @Operation(summary = "중고 상품 이미지 조회/중고 상품 ID로 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/used/product")
    public BaseResponse<List<UsedImageDTO>> getUsedImagesByUsedId(
        @RequestParam UUID usedProductId) {
        ImageRequest.GetUsedImagesByUsedIdRequest req = new ImageRequest.GetUsedImagesByUsedIdRequest(usedProductId);
        log.info("[중고 상품 리스트 조회] request: {}", req);
        return BaseResponse.onSuccess(imageService.getUsedImagesByUsedId(req));
    }
    //중고 상품 이미지 삭제
    @Operation(summary = "중고 상품 이미지 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @DeleteMapping("/used")
    public BaseResponse<Void> deleteUsedImage(
        @Valid @RequestBody ImageRequest.DeleteUsedImageRequest req) {
        log.info("[중고 상품 이미지 삭제] request: {}", req);
        imageService.deleteUsedImage(req);
        return BaseResponse.onSuccess(null);
    }
    // todo: 검수 이미지 추가
}
