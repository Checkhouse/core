package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.FavoriteDTO;
import com.checkhouse.core.dto.request.FavoriteRequest;
import com.checkhouse.core.entity.*;
import com.checkhouse.core.entity.enums.Role;
import com.checkhouse.core.entity.enums.UsedProductState;
import com.checkhouse.core.repository.mysql.FavoriteOriginRepository;
import com.checkhouse.core.repository.mysql.FavoriteUsedRepository;
import com.checkhouse.core.repository.mysql.UsedProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    @Mock
    private FavoriteOriginRepository favoriteOriginRepository;

    @Mock
    private FavoriteUsedRepository favoriteUsedRepository;

    @Mock
    private UserService userService;

    @Mock
    private OriginProductService originProductService;

    @Mock
    private UsedProductService usedProductService;

    @Mock
    private UsedProductRepository usedProductRepository;
    @InjectMocks
    private FavoriteService favoriteService;
    private User mockedUser;
    private OriginProduct mockedOriginProduct;
    private UsedProduct mockedUsedProduct;

    @BeforeEach
    void setup() {
        mockedUser = User.builder()
                .userId(UUID.randomUUID())
                .username("test user")
                .email("test@test.com")
                .nickname("test nickname")
                .password(null)
                .role(Role.ROLE_USER)
                .provider("naver")
                .providerId("random id naver")
                .build();

        mockedOriginProduct = OriginProduct.builder()
                .id(UUID.randomUUID())
                .name("origin product name")
                .company("origin product company")
                .category(
                        Category.builder()
                                .name("category1")
                                .build()
                )
                .build();

        mockedUsedProduct = UsedProduct.builder()
                .usedProductId(UUID.randomUUID())
                .originProduct(mockedOriginProduct)
                .user(mockedUser)
                .state(UsedProductState.PRE_SALE)
                .title("Used Product Name")
                .description("shit")
                .price(189000)
                .isNegoAllow(false)
                .build();
    }

    @DisplayName("좋아요 목록에 등록")
    @Test
    void SUCCESS_addToFavoriteList() {
        FavoriteRequest.AddToFavoriteRequest request = new FavoriteRequest.AddToFavoriteRequest(
                mockedUser.getUserId(),
                mockedOriginProduct.getOriginProductId()
        );
        // Arrange
        when(userService.findUser(any())).thenReturn(mockedUser);
        when(originProductService.findOriginProduct(any())).thenReturn(mockedOriginProduct);
        when(favoriteOriginRepository.save(any())).thenReturn(
                FavoriteOrigin.builder()
                        .user(mockedUser)
                        .originProduct(mockedOriginProduct)
                        .build()
        );

        // Act
        FavoriteDTO result = favoriteService.addFavoriteOrigin(request);

        Assertions.assertEquals(result.type(), "origin");
        // Assert
        verify(userService, times(1)).findUser(any());
        verify(originProductService, times(1)).findOriginProduct(any());
        verify(favoriteOriginRepository, times(1)).save(any(FavoriteOrigin.class));
    }

    @DisplayName("좋아요 목록에서 제거")
    @Test
    void SUCCESS_removeFromFavoriteList() {
        FavoriteRequest.RemoveFromFavoriteRequest request = new FavoriteRequest.RemoveFromFavoriteRequest(
                mockedUser.getUserId(),
                mockedOriginProduct.getOriginProductId()
        );
        // Arrange
        when(userService.findUser(request.userId())).thenReturn(mockedUser);
        when(favoriteOriginRepository.existsByOriginProductOriginProductIdAndUserUserId(any(), any())).thenReturn(true);
        doNothing().when(favoriteOriginRepository).deleteByOriginProductOriginProductIdAndUserUserId(any(), any());

        // Act
        favoriteService.removeFavoriteOrigin(request);

        // Assert
        verify(favoriteOriginRepository, times(1)).deleteByOriginProductOriginProductIdAndUserUserId(any(), any());
    }

    @DisplayName("사용자의 원본 좋아요 리스트 조회")
    @Test
    void SUCCESS_getUserFavoriteOriginList() {
        FavoriteRequest.GetUserFavoriteOrigins request = new FavoriteRequest.GetUserFavoriteOrigins(
                mockedUser.getUserId()
        );
        // Arrange
        when(userService.findUser( request.userId())).thenReturn(mockedUser);
        when(favoriteOriginRepository.findAllByUserId(request.userId())).thenReturn(List.of(
                FavoriteOrigin.builder()
                        .user(mockedUser)
                        .originProduct(mockedOriginProduct)
                        .build()
        ));

        // Act
        List<FavoriteDTO> result = favoriteService.getUserFavoriteOrigins(request);

        // Assert
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("origin", result.getFirst().type());

        verify(favoriteOriginRepository, times(1)).findAllByUserId(request.userId());
    }

    @DisplayName("이미 좋아요에 등록한 원본 물품의 경우 좋아요 등록 실패")
    @Test
    void FAIL_addToFavoriteList() {
        FavoriteRequest.AddToFavoriteRequest request = new FavoriteRequest.AddToFavoriteRequest(
                mockedUser.getUserId(),
                mockedOriginProduct.getOriginProductId()
        );
        // Arrange
        when(userService.findUser(any())).thenReturn(mockedUser);
        when(originProductService.findOriginProduct(any())).thenReturn(mockedOriginProduct);
        when(favoriteOriginRepository.existsByOriginProductOriginProductIdAndUserUserId(any(), any())).thenReturn(true);

        // Act & Assert
        assertThrows(GeneralException.class, () -> {
            favoriteService.addFavoriteOrigin(request);
        });

        verify(userService, times(1)).findUser(any());
        verify(originProductService, times(1)).findOriginProduct(any());
        verify(favoriteOriginRepository, times(1)).existsByOriginProductOriginProductIdAndUserUserId(any(), any());
    }

    @DisplayName("이미 좋아요 삭제한 원본 물품의 경우 좋아요 삭제 실패")
    @Test
    void FAIL_removeFromFavoriteList() {
        FavoriteRequest.RemoveFromFavoriteRequest request = new FavoriteRequest.RemoveFromFavoriteRequest(
                mockedUser.getUserId(),
                mockedOriginProduct.getOriginProductId()
        );

        when(favoriteOriginRepository.existsByOriginProductOriginProductIdAndUserUserId(any(), any())).thenReturn(false);

        // Act & Assert
        assertThrows(GeneralException.class, () -> {
            favoriteService.removeFavoriteOrigin(request);
        });
        verify(favoriteOriginRepository, times(1)).existsByOriginProductOriginProductIdAndUserUserId(any(), any());
    }

    // ======================================== 중고 물품 ========================================
    @DisplayName("중고 상품 좋아요 추가")
    @Test
    void SUCCESS_addUsedProductLike() {
        // Arrange

        FavoriteRequest.AddUsedProductLikeRequest request = new FavoriteRequest.AddUsedProductLikeRequest(
                mockedUser.getUserId(),
                mockedOriginProduct.getOriginProductId()
        );

        when(userService.findUser(request.userId())).thenReturn(mockedUser);
        when(usedProductService.findUsedProduct(request.usedProductId())).thenReturn(mockedUsedProduct);
        when(favoriteUsedRepository.existsByUsedProductUsedProductIdAndUserUserId(any(), any())).thenReturn(false);
        when(favoriteUsedRepository.save(any())).thenReturn(
                FavoriteUsed.builder()
                        .user(mockedUser)
                        .usedProduct(mockedUsedProduct)
                        .build()
        );

        // Act
        FavoriteDTO result = favoriteService.addFavoriteUsed(request);

        // Assert
        verify(userService, times(1)).findUser(any());
        verify(favoriteUsedRepository, times(1)).existsByUsedProductUsedProductIdAndUserUserId(any(), any());
        verify(favoriteUsedRepository, times(1)).save(any(FavoriteUsed.class));
    }

    @DisplayName("중고 상품 좋아요 삭제")
    @Test
    void SUCCESS_removeUsedProductLike() {

        FavoriteRequest.RemoveUsedProductLikeRequest request = new FavoriteRequest.RemoveUsedProductLikeRequest(
                mockedUser.getUserId(),
                mockedOriginProduct.getOriginProductId()
        );
        when(userService.findUser( request.userId())).thenReturn(mockedUser);
        when(usedProductService.findUsedProduct( request.usedProductId() )).thenReturn(mockedUsedProduct);
        when(favoriteUsedRepository.existsByUsedProductUsedProductIdAndUserUserId( any(), any() )).thenReturn(true);

        doNothing().when(favoriteUsedRepository).deleteByUsedProductUsedProductIdAndUserUserId(any(), any());

        // Act
        favoriteService.removeFavoriteUsed(request);

        // Assert
        verify(favoriteUsedRepository, times(1)).existsByUsedProductUsedProductIdAndUserUserId(any(), any());
        verify(favoriteUsedRepository, times(1)).deleteByUsedProductUsedProductIdAndUserUserId(any(), any());
    }

    @DisplayName("중고 상품 좋아요 리스트")
    @Test
    void SUCCESS_getUsedProductLikeList() {
        FavoriteRequest.GetUserFavoriteUsed request = new FavoriteRequest.GetUserFavoriteUsed(
                mockedUser.getUserId()
        );
        // Arrange
        when(userService.findUser(request.userId())).thenReturn(mockedUser);
        when(favoriteUsedRepository.findAllByUserId( mockedUser.getUserId())).thenReturn(List.of(
                FavoriteUsed.builder()
                        .usedProduct(mockedUsedProduct)
                        .user(mockedUser)
                        .build()
        ));

        // Act
        List<FavoriteDTO> result = favoriteService.getUserFavoriteUsed(request);

        // Assert
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("used", result.getFirst().type());
        verify(favoriteUsedRepository, times(1)).findAllByUserId( mockedUser.getUserId());
    }

    @DisplayName("이미 좋아요를 추가한 경우 실패")
    @Test
    void FAIL_addUsedProductLike_conflict() {
        FavoriteRequest.AddUsedProductLikeRequest request = new FavoriteRequest.AddUsedProductLikeRequest(
                mockedUser.getUserId(),
                mockedOriginProduct.getOriginProductId()
        );
        // Arrange
        when(userService.findUser(any())).thenReturn(mockedUser);
        when(usedProductService.findUsedProduct(any())).thenReturn(mockedUsedProduct);
        when(favoriteUsedRepository.existsByUsedProductUsedProductIdAndUserUserId(any(), any())).thenReturn(true);

        // Act & Assert
        assertThrows(GeneralException.class, () -> {
            favoriteService.addFavoriteUsed(request);
        });
        verify(userService, times(1)).findUser(any());
        verify(favoriteUsedRepository, times(1)).existsByUsedProductUsedProductIdAndUserUserId(any(), any());
    }

    @DisplayName("이미 중고 상품 좋아요 삭제를 한 경우 삭제 실패")
    @Test
    void FAIL_removeUsedProductLike() {

        FavoriteRequest.RemoveUsedProductLikeRequest request = new FavoriteRequest.RemoveUsedProductLikeRequest(
                mockedUser.getUserId(),
                mockedOriginProduct.getOriginProductId()
        );
        // Arrange
        when(favoriteUsedRepository.existsByUsedProductUsedProductIdAndUserUserId(any(), any())).thenReturn(false);

        // Act & Assert
        assertThrows(GeneralException.class, () -> {
            favoriteService.removeFavoriteUsed(request);
        });
        verify(favoriteUsedRepository, times(1)).existsByUsedProductUsedProductIdAndUserUserId(any(), any());
    }
}
