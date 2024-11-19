package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.UsedProductDTO;
import com.checkhouse.core.dto.request.UsedProductRequest;
import com.checkhouse.core.entity.OriginProduct;
import com.checkhouse.core.entity.UsedProduct;
import com.checkhouse.core.entity.User;
import com.checkhouse.core.entity.enums.UsedProductState;
import com.checkhouse.core.repository.mysql.UsedProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsedProductService {

    private final UsedProductRepository usedProductRepository;

    //service
    private final UserService userService;
    private final OriginProductService originProductService;

    public UsedProductDTO addUsedProduct(UsedProductRequest.AddUsedProductRequest request) {

        OriginProduct originProduct = originProductService.findOriginProduct(request.originProductId());
        User user = userService.findUser(request.userId());

        // Logic to add a used product
        UsedProduct usedProduct = UsedProduct.builder()
                .originProduct(originProduct)
                .user(user)
                .state(UsedProductState.PRE_SALE) // default
                .title(request.title())
                .description(request.description())
                .price(request.price())
                .isNegoAllow(request.isNegoAllow())
                .build();

        try {
            return usedProductRepository.save(usedProduct).toDto();
        } catch(RuntimeException e) {
            throw new RuntimeException("중고 상품 저장 에러");
        }
    }

    public UsedProductDTO updateUsedProductNegoState(UsedProductRequest.UpdateUsedProductNegoState request) {
        // Logic to update the negotiation state of a used product
        UsedProduct usedProduct = usedProductRepository.findById(request.usedProductId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._USED_PRODUCT_NOT_FOUND));

        usedProduct.updateUsedProductNegoAllow(request.state());

        return usedProductRepository.save(usedProduct).toDto();
    }

    public UsedProductDTO updateUsedProductStatus(UsedProductRequest.UpdateUsedProductState request) {
        // Logic to update the status of a used product
        UsedProduct usedProduct = usedProductRepository.findById(request.usedProductId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._USED_PRODUCT_NOT_FOUND));

        usedProduct.updateUsedProductState(request.status());

        return usedProductRepository.save(usedProduct).toDto();
    }

    public UsedProductDTO updateUsedProductInfo(UsedProductRequest.UpdateUsedProductInfo request) {
        // Logic to update the product information
        UsedProduct usedProduct = usedProductRepository.findById(request.usedProductId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._USED_PRODUCT_NOT_FOUND));

        usedProduct.updateUsedProductInfo(request.title(), request.description(), request.price());

        return usedProductRepository.save(usedProduct).toDto();
    }

    public UsedProductDTO getUsedProductDetails(UUID productId) {
        // Logic to get the details of a used product
        UsedProduct usedProduct = usedProductRepository.findById(productId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USED_PRODUCT_NOT_FOUND));

        return usedProduct.toDto();
    }

    public void cancelAddUsedProduct(UUID productId) {
        // Logic to cancel a used product registration
        UsedProduct usedProduct = usedProductRepository.findById(productId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USED_PRODUCT_NOT_FOUND));

        // soft delete
        usedProductRepository.delete(usedProduct);
    }

    public List<UsedProductDTO> getUsedProductsByStatus(String status) {
        // Logic to get used products by status
        List<UsedProduct> products = usedProductRepository.findAllByState(status);

        return products.stream().map(UsedProduct::toDto).toList();
    }

    public List<UsedProductDTO> getUsedProductsByUser(UUID userId) {
        // Logic to get used products by user
        User user = userService.findUser(userId);

        List<UsedProduct> products = usedProductRepository.findAllByUserId(user.getUserId());

        return products.stream().map(UsedProduct::toDto).toList();
    }

    UsedProduct findUsedProduct(UUID productId) {
        return usedProductRepository.findById(productId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USED_PRODUCT_NOT_FOUND));
    }

}
