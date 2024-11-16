package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.CategoryDTO;
import com.checkhouse.core.entity.Category;
import com.checkhouse.core.repository.mysql.CategoryRepository;
import com.checkhouse.core.request.CategoryRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    CategoryDTO addCategory(CategoryRequest.AddCategoryRequest req) {
        //이미 존재하는 카테고리 확인
        categoryRepository.findCategoryByName(req.getName()).ifPresent(category -> {
            throw new GeneralException(ErrorStatus._CATEGORY_ALREADY_EXIST);
        });
        Category savedCategory = categoryRepository.save(
                Category.builder()
                        .categoryId(UUID.randomUUID())
                        .name(req.getName())
                        .build()
        );

        return savedCategory.toDTO();
    }

    CategoryDTO getCategory(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._CATEGORY_ID_NOT_FOUND));
        return category.toDTO();
    }

    CategoryDTO updateCategoryById(CategoryRequest.UpdateCategoryByIdRequest req) {
        //ID가 존재하는지 확인
        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._CATEGORY_ID_NOT_FOUND));
        //이미 존재하는 카테고리 확인
        categoryRepository.findCategoryByName(req.getName()).ifPresent((a) -> {
            throw new GeneralException(ErrorStatus._CATEGORY_ALREADY_EXIST);
        });
        category.updateName(req.getName());
        return category.toDTO();
    }


    void deleteCategory(CategoryRequest.DeleteCategoryRequest req) {
        //ID가 존재하는지 확인
        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._CATEGORY_ID_NOT_FOUND));

        category.setDeleted();
    }

    List<CategoryDTO> getCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(Category::toDTO)
                .toList();
    }
}
