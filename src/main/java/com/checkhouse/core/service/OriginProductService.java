package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.OriginProductDTO;
import com.checkhouse.core.dto.request.OriginProductRequest;
import com.checkhouse.core.entity.Category;
import com.checkhouse.core.entity.OriginProduct;
import com.checkhouse.core.repository.mysql.CategoryRepository;
import com.checkhouse.core.repository.mysql.OriginProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OriginProductService {
    private OriginProductRepository originProductRepository;
    private CategoryRepository categoryRepository;

    public OriginProductDTO addOriginProduct(
            OriginProductRequest.AddOriginProductRequest request
    ) {
        // 중복검사
        originProductRepository.findByName(request.name()).ifPresent(
                (a) -> {
                    throw new GeneralException(ErrorStatus._ORIGIN_PRODUCT_ALREADY_EXISTS);
                }
        );

        Category category = categoryRepository.findById(request.categoryId()).orElseThrow(
                () -> new GeneralException(ErrorStatus._CATEGORY_ID_NOT_FOUND)
        );


        OriginProduct savedOriginProduct = originProductRepository.save(
                OriginProduct.builder()
                        .name(request.name())
                        .company(request.company())
                        .category(category)
                        .build()
        );

        return savedOriginProduct.toDto();
    }

    public OriginProductDTO updateOriginProductInfo(
            OriginProductRequest.UpdateOriginProductInfo request
    ) {
        OriginProduct originProduct = originProductRepository.findById(request.originProductId()).orElseThrow(
                () ->  new GeneralException(ErrorStatus._ORIGIN_PRODUCT_ALREADY_EXISTS)
        );

        // update info
        originProduct.updateOriginProductInfo(request.name(), request.company());

        OriginProduct updatedOriginProduct = originProductRepository.save(originProduct);

        return updatedOriginProduct.toDto();
    }
    public OriginProductDTO updateOriginProductCategory(
            OriginProductRequest.UpdateOriginProductCategory request
    ) {
        OriginProduct originProduct = originProductRepository.findById(request.originProductId()).orElseThrow(
                () ->  new GeneralException(ErrorStatus._ORIGIN_PRODUCT_ALREADY_EXISTS)
        );

        // todo category 유효성 검사

        // todo update category

        OriginProduct updatedOriginProduct = originProductRepository.save(originProduct);

        return updatedOriginProduct.toDto();
    }

    public List<OriginProductDTO> getOriginProducts() {
        return originProductRepository.findAll()
                .stream().map(OriginProduct::toDto).toList();
    }
    public OriginProductDTO getOriginProductInfo( UUID originProductId ) {
         OriginProduct originProduct = originProductRepository.findById(originProductId).orElseThrow(
                () -> new GeneralException(ErrorStatus._ORIGIN_PRODUCT_NOT_FOUND)
        );

         return originProduct.toDto();
    }

    public List<OriginProductDTO> getOriginProductsWithCategory( UUID categoryId ) {
        categoryRepository.findById(categoryId).orElseThrow(
                () -> new GeneralException(ErrorStatus._CATEGORY_ID_NOT_FOUND)
        );
        return originProductRepository.findByCategoryId(categoryId)
                .stream().map(OriginProduct::toDto).toList();
    }

    // todo es 검색
    public List<OriginProductDTO> searchOriginProducts(String query) {

        return List.of();

    }
    public void deleteOriginProduct( OriginProductRequest.DeleteOriginProduct request ) {
        originProductRepository.findById(request.originProductId()).ifPresentOrElse(
                (og) -> originProductRepository.deleteById(request.originProductId()),
                () -> {
                    throw new GeneralException(ErrorStatus._ORIGIN_PRODUCT_NOT_FOUND);
                }

        );
    }
    public OriginProduct findOriginProduct(UUID originProductId) {
        return originProductRepository.findById(originProductId).orElseThrow(
                () -> new GeneralException(ErrorStatus._ORIGIN_PRODUCT_NOT_FOUND)
        );
    }

}
