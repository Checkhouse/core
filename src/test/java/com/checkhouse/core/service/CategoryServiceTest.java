package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.CategoryDTO;
import com.checkhouse.core.entity.Category;
import com.checkhouse.core.repository.mysql.CategoryRepository;
import com.checkhouse.core.dto.request.CategoryRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category phone;
    private Category tablet;
    private Category laptop;


    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {
        phone = Category.builder()
                .categoryId(UUID.randomUUID())
                .name("phone")
                .build();
        tablet = Category.builder()
                .categoryId(UUID.randomUUID())
                .name("tablet")
                .build();
        laptop = Category.builder()
                .categoryId(UUID.randomUUID())
                .name("laptop")
                .build();
    }

    @DisplayName("카테고리 등록")
    @Test
    void SUCCESS_addCategory() {
        //카테고리 정보
        CategoryRequest.AddCategoryRequest req = CategoryRequest.AddCategoryRequest.builder()
                .name("phone")
                .build();

        // given
        when(categoryRepository.findCategoryByName(any())).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(phone);

        // when
        CategoryDTO result = categoryService.addCategory(req);

        //then
        assertNotNull(result);
        assertEquals("phone", result.name());
        verify(categoryRepository, times(1)).findCategoryByName("phone");
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @DisplayName("카테고리 ID로 수정")
    @Test
    void SUCCESS_updateCategoryById() {
        //카테고리 정보
        UUID categoryId = phone.getCategoryId();

        // given
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(phone));

        // when
        CategoryRequest.UpdateCategoryByIdRequest req = new CategoryRequest.UpdateCategoryByIdRequest(categoryId, "smartphone");

        CategoryDTO result = categoryService.updateCategoryById(req);

        // then
        assertNotNull(result);
        assertEquals("smartphone", result.name());
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).findCategoryByName(req.name());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @DisplayName("카테고리 삭제")
    @Test
    void SUCCESS_deleteCategory() {
        //카테고리 정보
        UUID categoryId = phone.getCategoryId();

        // given
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(phone));

        // when
        CategoryRequest.DeleteCategoryRequest req = new CategoryRequest.DeleteCategoryRequest(categoryId);

        categoryService.deleteCategory(req);

        // then
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @DisplayName("카테고리 리스트 조회")
    @Test
    void SUCCESS_getCategoryList() {
        // given
        when(categoryRepository.findAll()).thenReturn(List.of(phone, tablet, laptop));

        // when
        List<CategoryDTO> categories = categoryService.getCategories();

        // then
        assertNotNull(categories);
        assertEquals(3, categories.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @DisplayName("카테고리 ID로 수정 - 존재하지 않는 카테고리 ID")
    @Test
    void FAIL_updateCategoryById_not_found() {
        //카테고리 정보
        UUID categoryId = UUID.randomUUID();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // given, when, then
        CategoryRequest.UpdateCategoryByIdRequest req = new CategoryRequest.UpdateCategoryByIdRequest(categoryId, "smartphone");
        GeneralException exception = assertThrows(GeneralException.class, () -> categoryService.updateCategoryById(req));

        assertEquals(ErrorStatus._CATEGORY_ID_NOT_FOUND, exception.getCode());
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @DisplayName("카테고리 ID로 수정 - 이미 존재하는 카테고리로 변경 실패")
    @Test
    void FAIL_updateCategoryById_exist() {
        //카테고리 정보
        UUID categoryId = phone.getCategoryId();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(phone));
        when(categoryRepository.findCategoryByName("tablet")).thenReturn(Optional.of(tablet));

        // given, when, then
        CategoryRequest.UpdateCategoryByIdRequest req = new CategoryRequest.UpdateCategoryByIdRequest(categoryId, "tablet");
        GeneralException exception = assertThrows(GeneralException.class, () -> categoryService.updateCategoryById(req));

        assertEquals(ErrorStatus._CATEGORY_ALREADY_EXIST, exception.getCode());
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).findCategoryByName("tablet");
    }

    @DisplayName("카테고리 삭제 - 카테고리가 존재하지 않을 때 삭제 실패")
    @Test
    void FAIL_deleteCategoryByName_exist() {
        //카테고리 정보
        UUID categoryId = UUID.randomUUID();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // given, when, then
        CategoryRequest.DeleteCategoryRequest req = new CategoryRequest.DeleteCategoryRequest(categoryId);
        GeneralException exception = assertThrows(GeneralException.class, () -> categoryService.deleteCategory(req));

        assertEquals(ErrorStatus._CATEGORY_ID_NOT_FOUND, exception.getCode());
        verify(categoryRepository, times(1)).findById(categoryId);
    }
    @DisplayName("카테고리 조회 - 카테고리가 존재하지 않을 때 조회 실패")
    @Test
    void FAIL_getCategory_not_found() {
        //카테고리 정보
        UUID categoryId = UUID.randomUUID();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        CategoryRequest.GetCategoryByIdRequest req = new CategoryRequest.GetCategoryByIdRequest(categoryId);

        // given, when, then
        GeneralException exception = assertThrows(GeneralException.class, () -> categoryService.getCategory(req));

        assertEquals(ErrorStatus._CATEGORY_ID_NOT_FOUND, exception.getCode());
        verify(categoryRepository, times(1)).findById(categoryId);
    }

}
