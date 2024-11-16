package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.UsedProductDTO;
import com.checkhouse.core.dto.request.UsedProductRequest;
import com.checkhouse.core.entity.Category;
import com.checkhouse.core.entity.OriginProduct;
import com.checkhouse.core.entity.UsedProduct;
import com.checkhouse.core.entity.User;
import com.checkhouse.core.entity.enums.Role;
import com.checkhouse.core.entity.enums.UsedProductState;
import com.checkhouse.core.repository.mysql.OriginProductRepository;
import com.checkhouse.core.repository.mysql.UsedProductRepository;
import com.checkhouse.core.repository.mysql.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class UsedProductServiceTest {
    @Mock
    private UsedProductRepository usedProductRepository;
    @Mock
    private UserRepository userRepository;

    @Mock
    private OriginProductRepository originProductRepository;

    @Mock
    private UserService userService;

    @Mock
    private OriginProductService originProductService;
    @InjectMocks
    private UsedProductService usedProductService;



    private User mockedUser;
    private OriginProduct mockedOriginProduct;
    private UsedProduct mockedUsedProduct;


    @BeforeAll
    public static void onlyOnce() {}

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

    @DisplayName("중고 상품 등록")
    @Test
    void SUCCESS_addUsedProduct() {
        UsedProductRequest.AddUsedProductRequest request = new UsedProductRequest.AddUsedProductRequest(
                "Used Product Name",
                "shit",
                189000,
                true,
                mockedUser.getUserId(),
                mockedOriginProduct.getOriginProductId()
        );

        when(originProductService.findOriginProduct(request.originProductId())).thenReturn(mockedOriginProduct);
        when(userService.findUser(request.userId())).thenReturn(mockedUser);
        when(usedProductRepository.save( any()) ).thenReturn(mockedUsedProduct);

        UsedProductDTO result = usedProductService.addUsedProduct(request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockedUsedProduct.getTitle(), result.title());

        verify(usedProductRepository, times(1)).save(any(UsedProduct.class));
    }

    @DisplayName("중고 상품 네고 상태 변경")
    @Test
    void SUCCESS_updateUsedProductNegoState() {
        UsedProductRequest.UpdateUsedProductNegoState request = new UsedProductRequest.UpdateUsedProductNegoState(
                mockedUsedProduct.getUsedProductId(),
                true
        );

        mockedUsedProduct.updateUsedProductNegoAllow(true);

        when(usedProductRepository.findById(mockedUsedProduct.getUsedProductId())).thenReturn(Optional.of(mockedUsedProduct));
        when(usedProductRepository.save(any(UsedProduct.class))).thenReturn(mockedUsedProduct);

        UsedProductDTO result = usedProductService.updateUsedProductNegoState(request);

        Assertions.assertTrue(result.negoState());

        verify(usedProductRepository, times(1)).findById(mockedUsedProduct.getUsedProductId());
        verify(usedProductRepository, times(1)).save(any(UsedProduct.class));
    }

    @DisplayName("중고 상품 상태 변경: 판매 중으로 변경")
    @Test
    void SUCCESS_updateUsedProductStatus() {
        UsedProductRequest.UpdateUsedProductState request = new UsedProductRequest.UpdateUsedProductState(
                mockedUsedProduct.getUsedProductId(),
                UsedProductState.ON_SALE
        );

        mockedUsedProduct.updateUsedProductState(
                UsedProductState.ON_SALE
        );

        when(usedProductRepository.findById( any() )).thenReturn(Optional.of(mockedUsedProduct));
        when(usedProductRepository.save(any(UsedProduct.class))).thenReturn(mockedUsedProduct);

        UsedProductDTO result = usedProductService.updateUsedProductStatus(request);

        Assertions.assertEquals(UsedProductState.ON_SALE, result.status());

        verify(usedProductRepository, times(1)).findById(mockedUsedProduct.getUsedProductId());
        verify(usedProductRepository, times(1)).save(any(UsedProduct.class));
    }

    @DisplayName("중고 상품 정보 수정")
    @Test
    void SUCCESS_updateUsedProductInfo() {
        // given
        UsedProductRequest.UpdateUsedProductInfo request = new UsedProductRequest.UpdateUsedProductInfo(
                mockedUsedProduct.getUsedProductId(),
                mockedUsedProduct.getTitle(),
                mockedUsedProduct.getDescription(),
                111111111
        );
        UUID productId = mockedUsedProduct.getUsedProductId();

        mockedUsedProduct.updateUsedProductInfo(
                request.title(),
                request.description(),
                request.price()
        );

        when(usedProductRepository.findById(productId)).thenReturn(Optional.of(mockedUsedProduct));
        when(usedProductRepository.save(any(UsedProduct.class))).thenReturn(mockedUsedProduct);

        UsedProductDTO result = usedProductService.updateUsedProductInfo(request);

        Assertions.assertEquals(request.title(), result.title());
        Assertions.assertEquals(request.price(), result.price());

        verify(usedProductRepository, times(1)).findById(productId);
        verify(usedProductRepository, times(1)).save(any(UsedProduct.class));
    }

    @DisplayName("중고 상품 정보 조회")
    @Test
    void SUCCESS_getUsedProductDetails() {
        UUID productId = mockedUsedProduct.getUsedProductId();

        when(usedProductRepository.findById(productId)).thenReturn(Optional.of(mockedUsedProduct));

        UsedProductDTO result = usedProductService.getUsedProductDetails(productId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockedUsedProduct.getTitle(), result.title());
        verify(usedProductRepository, times(1)).findById(productId);
    }

    @DisplayName("중고 상품 등록 취소")
    @Test
    void SUCCESS_cancelAddUsedProduct() {
        UUID productId = mockedUsedProduct.getUsedProductId();

        when(usedProductRepository.findById(productId)).thenReturn(Optional.of(mockedUsedProduct));
        doNothing().when(usedProductRepository).delete(mockedUsedProduct);

        usedProductService.cancelAddUsedProduct(productId);

        verify(usedProductRepository, times(1)).findById(productId);
        verify(usedProductRepository, times(1)).delete(mockedUsedProduct);
    }

    @DisplayName("특정 상태 중고 상품 리스트 조회")
    @Test
    void SUCCESS_getUsedProductsByStatus() {
        UsedProductState status = UsedProductState.PRE_SALE;

        when(usedProductRepository.findAllByState(status.name())).thenReturn(List.of(mockedUsedProduct));

        List<UsedProductDTO> result = usedProductService.getUsedProductsByStatus(status.name());

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(mockedUsedProduct.getTitle(), result.getFirst().title());

        verify(usedProductRepository, times(1)).findAllByState(status.name());
    }

    @DisplayName("특정 사용자가 등록한 중고 상품 리스트 조회")
    @Test
    void SUCCESS_getUsedProductsByUser() {
        UUID userId = mockedUsedProduct.getUser().getUserId();

        when(usedProductRepository.findAllByUserId(userId)).thenReturn(List.of(mockedUsedProduct));
        when(userService.findUser( userId )).thenReturn(mockedUser);

        List<UsedProductDTO> result = usedProductService.getUsedProductsByUser(userId);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(mockedUsedProduct.getTitle(), result.getFirst().title());

        verify(usedProductRepository, times(1)).findAllByUserId(userId);
    }
    @DisplayName("존재하지 않는 사용자에 대한 즁고 상품 등록은 실패")
    @Test
    void FAIL_addUsedProduct_not_exist() {
        UsedProductRequest.AddUsedProductRequest request = new UsedProductRequest.AddUsedProductRequest(
                "Used Product Name",
                "shit",
                189000,
                true,
                UUID.randomUUID(),
                mockedOriginProduct.getOriginProductId()
        );

        when(userService.findUser(request.userId())).thenThrow(GeneralException.class);

        Assertions.assertThrows(GeneralException.class,
                () -> {
                    usedProductService.addUsedProduct(request);
                }
        );
    }

    @DisplayName("존재하지 않는 중고 상품에 대한 네고 상태 변경은 실패")
    @Test
    void FAIL_updateUsedProductNegoState_not_exist() {
        UsedProductRequest.UpdateUsedProductNegoState request = new UsedProductRequest.UpdateUsedProductNegoState(
                mockedUsedProduct.getUsedProductId(),
                true
        );
        when(usedProductRepository.findById(request.usedProductId())).thenReturn(Optional.empty());

        Assertions.assertThrows(GeneralException.class, () -> {
            usedProductService.updateUsedProductNegoState(request);
        });
        verify(usedProductRepository, times(1)).findById(request.usedProductId());
    }

    @DisplayName("중고 상품이 존재하지 않을 때 상태 변경 실패")
    @Test
    void FAIL_updateUsedProductStatus_not_found() {
        UsedProductRequest.UpdateUsedProductState request = new UsedProductRequest.UpdateUsedProductState(
                mockedUsedProduct.getUsedProductId(),
                UsedProductState.ON_SALE
        );

        when(usedProductRepository.findById(mockedUsedProduct.getUsedProductId())).thenReturn(Optional.empty());

        Assertions.assertThrows(GeneralException.class, () -> {
            usedProductService.updateUsedProductStatus(request);
        });
        verify(usedProductRepository, times(1)).findById(mockedUsedProduct.getUsedProductId());
    }

    @DisplayName("중고 상품이 존재하지 않을 때 정보 수정 실패")
    @Test
    void FAIL_updateUsedProduct_not_found() {
        UsedProductRequest.UpdateUsedProductInfo request = new UsedProductRequest.UpdateUsedProductInfo(
                mockedUsedProduct.getUsedProductId(),
                mockedUsedProduct.getTitle(),
                mockedUsedProduct.getDescription(),
                111111111
        );

        when(usedProductRepository.findById(mockedUsedProduct.getUsedProductId())).thenReturn(Optional.empty());

        Assertions.assertThrows(GeneralException.class, () -> {
            usedProductService.updateUsedProductInfo(request);
        });
        verify(usedProductRepository, times(1)).findById(mockedUsedProduct.getUsedProductId());
    }

    @DisplayName("중고 상품이 존재하지 않을 때 조회 실패")
    @Test
    void FAIL_getUsedProductDetails_not_found() {
        UUID productId = mockedUsedProduct.getUsedProductId();

        when(usedProductRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(GeneralException.class, () -> {
            usedProductService.getUsedProductDetails(productId);
        });
        verify(usedProductRepository, times(1)).findById(productId);
    }

    @DisplayName("중고 상품이 존재하지 않을 때 취소 실패")
    @Test
    void FAIL_cancelUsedProductRegistration_not_found() {
        UUID productId = UUID.randomUUID();
        when(usedProductRepository.findById(productId)).thenReturn(Optional.empty());

        Assertions.assertThrows(GeneralException.class, () -> {
            usedProductService.cancelAddUsedProduct(productId);
        });
        verify(usedProductRepository, times(1)).findById(productId);
    }

    @DisplayName("사용자 특정 실패 시 리스트 조회 실패")
    @Test
    void FAIL_getUsedProductsByUser_user_not_found() {
        UUID userId = UUID.randomUUID();

        when(userService.findUser( userId )).thenThrow(GeneralException.class);

        Assertions.assertThrows(GeneralException.class, () -> {
            usedProductService.getUsedProductsByUser(userId);
        });
    }
}
