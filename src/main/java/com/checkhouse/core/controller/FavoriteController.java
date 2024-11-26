package com.checkhouse.core.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import com.checkhouse.core.apiPayload.BaseResponse;
import com.checkhouse.core.dto.FavoriteDTO;
import com.checkhouse.core.dto.request.FavoriteRequest;
import com.checkhouse.core.service.FavoriteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@Tag(name = "favorite apis")
@RestController
@RequiredArgsConstructor
@RequestMapping("favorite")
public class FavoriteController {
    private final FavoriteService favoriteService;
    //origin favorite 등록
    @PostMapping("/origin")
    public BaseResponse<FavoriteDTO> addFavoriteOrigin(@RequestBody FavoriteRequest.AddToFavoriteRequest req) {
        return BaseResponse.onSuccess(favoriteService.addFavoriteOrigin(req));
    }
    //used favorite 등록
    @PostMapping("/used")
    public BaseResponse<FavoriteDTO> addFavoriteUsed(@RequestBody FavoriteRequest.AddUsedProductLikeRequest req) {
        return BaseResponse.onSuccess(favoriteService.addFavoriteUsed(req));
    }
    //origin favorite 삭제
    @DeleteMapping("/origin")
    public BaseResponse<Void> removeFavoriteOrigin(@RequestBody FavoriteRequest.RemoveFromFavoriteRequest req) {
        favoriteService.removeFavoriteOrigin(req);
        return BaseResponse.onSuccess(null);
    }
    //used favorite 삭제
    @DeleteMapping("/used")
    public BaseResponse<Void> removeFavoriteUsed(@RequestBody FavoriteRequest.RemoveUsedProductLikeRequest req) {
        favoriteService.removeFavoriteUsed(req);
        return BaseResponse.onSuccess(null);
    }
    //origin favorite 조회
    @GetMapping("/origin")
    public BaseResponse<List<FavoriteDTO>> getUserFavoriteOrigins(@RequestBody FavoriteRequest.GetUserFavoriteOrigins req) {
        return BaseResponse.onSuccess(favoriteService.getUserFavoriteOrigins(req));
    }
    //used favorite 조회
    @GetMapping("/used")
    public BaseResponse<List<FavoriteDTO>> getUserFavoriteUsed(@RequestBody FavoriteRequest.GetUserFavoriteUsed req) {
        return BaseResponse.onSuccess(favoriteService.getUserFavoriteUsed(req));
    }
    //origin favorite 개수 조회
    @GetMapping("/origin/count")
    public BaseResponse<Integer> getOriginProductFavoriteCount(@RequestParam UUID originProductId) {
        return BaseResponse.onSuccess(favoriteService.getOriginProductFavoriteCount(originProductId));
    }
    //used favorite 개수 조회
    @GetMapping("/used/count")
    public BaseResponse<Integer> getUsedProductFavoriteCount(@RequestParam UUID usedId) {
        return BaseResponse.onSuccess(favoriteService.getUsedProductFavoriteCount(usedId));
    }
}
