package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.OriginProductDTO;
import com.checkhouse.core.dto.request.OriginProductRequest;
import com.checkhouse.core.entity.Category;
import com.checkhouse.core.entity.OriginProduct;
import com.checkhouse.core.repository.mysql.OriginProductRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OriginProductServiceTest {

    @Mock
    private OriginProductRepository originProductRepository;


    @InjectMocks
    private OriginProductService originProductService;

    private Category mockedCategory;
    private OriginProduct mockedOriginProduct;

    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {
        mockedCategory = Category.builder()
                .name("category1")
                .build();

        mockedOriginProduct = OriginProduct.builder()
                .id(UUID.randomUUID())
                .name("origin product name")
                .company("origin product company")
                .category(mockedCategory)
                .build();
    }

    @DisplayName("원본 상품 등록")
    @Test
    void SUCCESS_addOriginProduct() {

        OriginProductRequest.AddOriginProductRequest request = new OriginProductRequest.AddOriginProductRequest(
                "origin product name",
                "origin product company",
                mockedCategory.getCategoryId(),
                List.of()
        );

        when(originProductRepository.findByName(any())).thenReturn(Optional.empty());
        when(originProductRepository.save(any())).thenReturn(mockedOriginProduct);

        OriginProductDTO result = originProductService.addOriginProduct(request);

        assertEquals(result.name(), mockedOriginProduct.getName());

    }

    @DisplayName("원본 상품 수정: 상품 정보만")
    @Test
    void SUCCESS_updateOriginProductInfo() {
        // given
        UUID productId = mockedOriginProduct.getOriginProductId();
        OriginProductRequest.UpdateOriginProductInfo request = new OriginProductRequest.UpdateOriginProductInfo(
                mockedOriginProduct.getOriginProductId(),
                "updated product name",
                "updated product company"
        );

        when(originProductRepository.findById(productId)).thenReturn(Optional.of(mockedOriginProduct));

        // when
        mockedOriginProduct.updateOriginProductInfo(request.name(), request.company());
        when(originProductRepository.save(any())).thenReturn(mockedOriginProduct);

        OriginProductDTO result = originProductService.updateOriginProductInfo(request);

        // then
        assertEquals(request.name(), result.name());
        assertEquals(request.company(), result.company());
        verify(originProductRepository, times(1)).findById(productId);
    }

    @DisplayName("원본 상품 수정: 카테고리만")
    @Test
    void SUCCESS_updateOriginProductCategory() {
        // give
        UUID productId = mockedOriginProduct.getOriginProductId();
        UUID newCategoryId = UUID.randomUUID();
        Category newCategory = Category.builder()
                .id(newCategoryId)
                .name("new category")
                .build();

        OriginProductRequest.UpdateOriginProductCategory request = new OriginProductRequest.UpdateOriginProductCategory(
                mockedOriginProduct.getOriginProductId(),
                newCategoryId
        );

        mockedOriginProduct.updateOriginProductCategory(newCategory);
        when(originProductRepository.findById( any() )).thenReturn(Optional.of(mockedOriginProduct));
        when(originProductRepository.save(any())).thenReturn(mockedOriginProduct);

        // when

        OriginProductDTO result = originProductService.updateOriginProductCategory(request);

        // then
        assertEquals(newCategory.getName(), result.category().getName());
        verify(originProductRepository, times(1)).findById(productId);
    }

    @DisplayName("원본 상품 삭제")
    @Test
    void SUCCESS_deleteOriginProduct() {
        // given
        UUID productId = mockedOriginProduct.getOriginProductId();

        when(originProductRepository.findById(productId)).thenReturn(Optional.of(mockedOriginProduct));
        doNothing().when(originProductRepository).deleteById(productId);

        // when
        originProductService.deleteOriginProduct(productId);

        // then
        verify(originProductRepository, times(1)).findById(productId);
        verify(originProductRepository, times(1)).deleteById(productId);
    }

    @DisplayName("원본 상품 조회")
    @Test
    void SUCCESS_getOriginProducts() {
        // given
        when(originProductRepository.findAll()).thenReturn(List.of(mockedOriginProduct));

        // when
        List<OriginProductDTO> result = originProductService.getOriginProducts();

        // then
        assertEquals(1, result.size());
        verify(originProductRepository, times(1)).findAll();
    }

    @DisplayName("카테고리별 원본 상품 리스트 조회")
    @Test
    void SUCCESS_getOriginProductsByCategory() {
        // given
        UUID categoryId = mockedCategory.getCategoryId();
        when(originProductRepository.findByCategoryId(categoryId)).thenReturn(List.of(mockedOriginProduct));

        // when
        List<OriginProductDTO> result = originProductService.getOriginProductsWithCategory(categoryId);

        // then
        assertEquals(1, result.size());
        verify(originProductRepository, times(1)).findByCategoryId(categoryId);
    }

    @DisplayName("원본 상품 정보 조회")
    @Test
    void SUCCESS_getOriginProductInfo() {
        // given
        UUID productId = mockedOriginProduct.getOriginProductId();
        when(originProductRepository.findById(productId)).thenReturn(Optional.of(mockedOriginProduct));

        // when
        OriginProductDTO result = originProductService.getOriginProductInfo(productId);

        // then
        assertEquals(mockedOriginProduct.getName(), result.name());
        verify(originProductRepository, times(1)).findById(productId);
    }

    // todo es에서 검색
    @DisplayName("원본 상품 검색")
    @Test
    void SUCCESS_searchOriginProduct() {
        // given
        String keyword = "origin";
        // when
        List<OriginProductDTO> result = originProductService.searchOriginProducts(keyword);

        System.out.println("do noting");

    }

    @DisplayName("중복된 이름의 원본 상품일 경우 저장 실패")
    @Test
    void FAIL_addOriginProduct_already_exist() {
        // given
        OriginProductRequest.AddOriginProductRequest request = new OriginProductRequest.AddOriginProductRequest(
                "origin product name",
                "origin product company",
                mockedCategory.getCategoryId(),
                List.of()
        );

        when(originProductRepository.findByName(any())).thenReturn(Optional.of(mockedOriginProduct));

        // then
        assertThrows(GeneralException.class, () -> {
            originProductService.addOriginProduct(request);
        });
        verify(originProductRepository, times(1)).findByName(any());
    }

    @DisplayName("존재하지 않는 원본 상품에 대한 정보 수정 실패")
    @Test
    void FAIL_updateOriginProductInfo_not_found() {
        // given
        UUID productId = UUID.randomUUID();
        OriginProductRequest.UpdateOriginProductInfo request = new OriginProductRequest.UpdateOriginProductInfo(
                productId,
                "updated product name",
                "updated product company"
        );

        when(originProductRepository.findById(productId)).thenReturn(Optional.empty());

        // then
        assertThrows(GeneralException.class, () -> {
            originProductService.updateOriginProductInfo(request);
        });
        verify(originProductRepository, times(1)).findById(productId);
    }

    @DisplayName("존재하지 않는 원본 상품에 대한 정보 조회 실패")
    @Test
    void FAIL_getOriginProductInfo_not_found() {
        // given
        UUID productId = UUID.randomUUID();
        when(originProductRepository.findById(productId)).thenReturn(Optional.empty());

        // then
        assertThrows(GeneralException.class, () -> {
            originProductService.getOriginProductInfo(productId);
        });
        verify(originProductRepository, times(1)).findById(productId);
    }

    @DisplayName("존재하지 않은 카테고리로 원본 상품 조회 실패")
    @Test
    void FAIL_getOriginProductsByCategory_not_found() {
        // given

    }

    @DisplayName("존재하지 않는 원본 상품 삭제 실패")
    @Test
    void FAIL_deleteOriginProduct_not_found() {
        // given
        UUID productId = UUID.randomUUID();
        when(originProductRepository.findById(productId)).thenReturn(Optional.empty());

        // then
        assertThrows(GeneralException.class, () -> {
            originProductService.deleteOriginProduct(productId);
        });
        verify(originProductRepository, times(1)).findById(productId);
    }
}

