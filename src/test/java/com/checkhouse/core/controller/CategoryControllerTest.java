package com.checkhouse.core.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import com.checkhouse.core.integration.BaseIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import com.checkhouse.core.dto.CategoryDTO;
import com.checkhouse.core.dto.request.CategoryRequest;
import com.checkhouse.core.repository.mysql.CategoryRepository;
import com.checkhouse.core.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class CategoryControllerTest extends BaseIntegrationTest {
    private static final String baseUrl = "/api/v1/category";

    @Autowired
    private CategoryController categoryController;
    @Autowired
    private CategoryRepository categoryRepository;

//    @AfterEach
//    void cleanUp() {
//        categoryRepository.deleteAll();
//    }

    @Test
    @DisplayName("카테고리 등록 성공")
    void addCategory_success() throws Exception {
        CategoryRequest.AddCategoryRequest request = CategoryRequest.AddCategoryRequest.builder()
                .name("테스트 카테고리")
                .build();

        mockMvc.perform(
            MockMvcRequestBuilders.post(baseUrl)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

}
