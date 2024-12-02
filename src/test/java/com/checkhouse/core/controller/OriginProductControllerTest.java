package com.checkhouse.core.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.checkhouse.core.integration.BaseIntegrationTest;
import com.checkhouse.core.entity.Category;
import com.checkhouse.core.entity.OriginProduct;
import com.checkhouse.core.repository.mysql.CategoryRepository;
import com.checkhouse.core.repository.mysql.OriginProductRepository;
import com.checkhouse.core.dto.request.OriginProductRequest;

import java.util.UUID;

public class OriginProductControllerTest extends BaseIntegrationTest {
    private static String baseUrl = "/api/v1/origin-product";

    @Autowired
    private OriginProductController originProductController;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OriginProductRepository originProductRepository;

    private Category savedCategory;
    private OriginProduct savedOriginProduct;

    @BeforeEach
    void setup() {
        Category category = Category.builder()
            .name("태블릿")
            .build();
		savedCategory = categoryRepository.saveAndFlush(category);

        OriginProduct originProduct1 = OriginProduct.builder()
                .name("아이패드")
                .company("애플")
                .category(savedCategory)
                .build();
        // originProductRepository.hardDeleteByNameIfSoftDeleted(originProduct1.getName());
        savedOriginProduct = originProductRepository.saveAndFlush(originProduct1);
    }

    @AfterEach
    void cleanUp() {
        originProductRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    //원본 상품 등록 성공
    @Test
    @DisplayName("원본 상품 등록 성공")
    public void addOriginProductSuccess() throws Exception {
        OriginProductRequest.AddOriginProductRequest request = OriginProductRequest.AddOriginProductRequest.builder()
            .name("아이폰")
            .company("애플")
            .categoryId(savedCategory.getCategoryId())
            .build();

        mockMvc.perform(
            MockMvcRequestBuilders.post(baseUrl)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
    //원본 상품 정보 수정 성공
    @Test
    @DisplayName("원본 상품 정보 수정 성공")
    public void updateOriginProductInfoSuccess() throws Exception {
        OriginProductRequest.UpdateOriginProductInfo request = OriginProductRequest.UpdateOriginProductInfo.builder()
            .originProductId(savedOriginProduct.getOriginProductId())
            .name("갤럭시탭")
            .company("삼성")
            .build();

        mockMvc.perform(
            MockMvcRequestBuilders.patch(baseUrl)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
    //원본 상품 정보 조회 성공
    @Test
    @DisplayName("원본 상품 정보 조회 성공")
    public void getOriginProductInfoSuccess() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.get(baseUrl + "/origin?originProductId=" + savedOriginProduct.getOriginProductId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
    //원본 상품 목록 조회 성공
    @Test
    @DisplayName("원본 상품 목록 조회 성공")
    public void getOriginProductsSuccess() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.get(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
    //카테고리별 원본 상품 목록 조회 성공
    @Test
    @DisplayName("카테고리별 원본 상품 목록 조회 성공")
    public void getOriginProductsWithCategorySuccess() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.get(baseUrl + "/category?categoryId=" + savedCategory.getCategoryId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
    //원본 상품 검색 성공
    @Test
    @DisplayName("원본 상품 검색 성공")
    public void searchOriginProductsSuccess() throws Exception {
        mockMvc.perform(
            //쿼리로 검색
            MockMvcRequestBuilders.get(baseUrl + "/search?query=" + "아이패드")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
    //원본 상품 삭제 성공
    @Test
    @DisplayName("원본 상품 삭제 성공")
    public void deleteOriginProductSuccess() throws Exception {
        OriginProductRequest.DeleteOriginProduct request = OriginProductRequest.DeleteOriginProduct.builder()
            .originProductId(savedOriginProduct.getOriginProductId())
            .build();

        mockMvc.perform(
            MockMvcRequestBuilders.delete(baseUrl)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
}
