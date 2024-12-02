package com.checkhouse.core.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.checkhouse.core.dto.CategoryDTO;
import com.checkhouse.core.dto.request.CategoryRequest;
import com.checkhouse.core.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private CategoryService categoryService;
    
    private static final String BASE_URL = "/api/v1/category";

    @Test
    @DisplayName("카테고리 등록 성공")
    void addCategory_success() throws Exception {
        // given
        CategoryRequest.AddCategoryRequest request = new CategoryRequest.AddCategoryRequest("테스트 카테고리");
        CategoryDTO response = new CategoryDTO(UUID.randomUUID(), "테스트 카테고리");
        
        when(categoryService.addCategory(any())).thenReturn(response);

        // when & then
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("SUCCESS"))
            .andExpect(jsonPath("$.data.name").value("테스트 카테고리"));
    }

    @Test
    @DisplayName("카테고리 목록 조회 성공")
    void getCategoryList_success() throws Exception {
        // given
        List<CategoryDTO> categories = List.of(
            new CategoryDTO(UUID.randomUUID(), "카테고리1"),
            new CategoryDTO(UUID.randomUUID(), "카테고리2")
        );
        
        when(categoryService.getCategories()).thenReturn(categories);

        // when & then
        mockMvc.perform(get(BASE_URL + "/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("SUCCESS"))
            .andExpect(jsonPath("$.data.length()").value(2));
    }
}
