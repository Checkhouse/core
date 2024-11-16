package com.checkhouse.core.service;

import com.checkhouse.core.entity.Category;
import com.checkhouse.core.repository.mysql.CategoryRepository;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

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
    void SUCCESS_addCategory() {}

    @DisplayName("카테고리 수정")
    @Test
    void SUCCESS_updateCategory() {}

    @DisplayName("카테고리 삭제")
    @Test
    void SUCCESS_deleteCategory() {}

    @DisplayName("카테고리 리스트 조회")
    @Test
    void SUCCESS_getCategoryList() {}

    @DisplayName("카테고리가 존재하지 않을 때 수정 실패")
    @Test
    void FAIL_updateCategory_not_found() {}

    @DisplayName("카테고리가 존재하지 않을 때 삭제 실패")
    @Test
    void FAIL_deleteCategory_not_found() {}

    @DisplayName("카테고리가 존재하지 않을 때 조회 실패")
    @Test
    void FAIL_getCategory_not_found() {}

}
