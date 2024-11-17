package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.FavoriteDTO;
import com.checkhouse.core.dto.request.FavoriteRequest;
import com.checkhouse.core.entity.*;
import com.checkhouse.core.repository.mysql.FavoriteOriginRepository;
import com.checkhouse.core.repository.mysql.FavoriteUsedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteOriginRepository favoriteOriginRepository;
    private final FavoriteUsedRepository favoriteUsedRepository;

    private final UserService userService;
    private final OriginProductService originProductService;
    private final UsedProductService usedProductService;

    FavoriteDTO addFavoriteOrigin(FavoriteRequest.AddToFavoriteRequest request) {
        OriginProduct originProduct = originProductService.findOriginProduct(request.originProductId());
        User user = userService.findUser(request.userId());

        if(!favoriteOriginRepository.existsByOriginProductOriginProductIdAndUserUserId(request.originProductId(), request.userId())) {
            FavoriteOrigin savedFavorite = favoriteOriginRepository.save(
                    FavoriteOrigin.builder()
                            .originProduct(originProduct)
                            .user(user)
                            .build()
            );

            return new FavoriteDTO(
                    savedFavorite.getFavoriteOriginId(),
                    savedFavorite.getOriginProduct().getOriginProductId(),
                    savedFavorite.getUser().getUserId(),
                    "origin"
            );
        } else {
            throw new GeneralException(ErrorStatus._FAVORITE_NOT_FOUND);
        }
    }
    void removeFavoriteOrigin(FavoriteRequest.RemoveFromFavoriteRequest request) {
        // 검증
        originProductService.findOriginProduct(request.originProductId());
        userService.findUser(request.userId());

        if(favoriteOriginRepository.existsByOriginProductOriginProductIdAndUserUserId(request.originProductId(), request.userId())) {
            favoriteOriginRepository.deleteByOriginProductOriginProductIdAndUserUserId(request.originProductId(), request.userId());
        } else {
            throw new GeneralException(ErrorStatus._FAVORITE_NOT_FOUND);
        }
    }

    List<FavoriteDTO> getUserFavoriteOrigins(FavoriteRequest.GetUserFavoriteOrigins request) {
        User user = userService.findUser(request.userId());

        return favoriteOriginRepository.findAllByUserId(user.getUserId())
                .stream().map((f) -> new FavoriteDTO(
                        f.getFavoriteOriginId(),
                        f.getOriginProduct().getOriginProductId(),
                        f.getUser().getUserId(),
                        "origin"
                )).toList();
    }

    FavoriteDTO addFavoriteUsed(FavoriteRequest.AddUsedProductLikeRequest request) {
        UsedProduct usedProduct = usedProductService.findUsedProduct(request.usedProductId());
        User user = userService.findUser(request.userId());

        if(!favoriteUsedRepository.existsByUsedProductUsedProductIdAndUserUserId(request.usedProductId(), request.userId())) {
            FavoriteUsed savedFavorite = favoriteUsedRepository.save(
                    FavoriteUsed.builder()
                            .usedProduct(usedProduct)
                            .user(user)
                            .build()
            );

            return new FavoriteDTO(
                    savedFavorite.getFavoriteUsedId(),
                    savedFavorite.getUsedProduct().getUsedProductId(),
                    savedFavorite.getUser().getUserId(),
                    "used"
            );
        } else {
            throw new GeneralException(ErrorStatus._FAVORITE_NOT_FOUND);
        }
    }
    void removeFavoriteUsed(FavoriteRequest.RemoveUsedProductLikeRequest request) {
        // 검증
        usedProductService.findUsedProduct(request.usedProductId());
        userService.findUser(request.userId());

        if(favoriteUsedRepository.existsByUsedProductUsedProductIdAndUserUserId(request.usedProductId(), request.userId())) {
            favoriteUsedRepository.deleteByUsedProductUsedProductIdAndUserUserId(request.usedProductId(), request.userId());

        } else {
            throw new GeneralException(ErrorStatus._FAVORITE_NOT_FOUND);
        }
    }

    List<FavoriteDTO> getUserFavoriteUsed(FavoriteRequest.GetUserFavoriteUsed request) {
        User user = userService.findUser(request.userId());

        return favoriteUsedRepository.findAllByUserId(user.getUserId())
                .stream().map((f) -> new FavoriteDTO(
                        f.getFavoriteUsedId(),
                        f.getUsedProduct().getUsedProductId(),
                        f.getUser().getUserId(),
                        "used"
                )).toList();
    }
}
