package com.checkhouse.core.service;

import com.checkhouse.core.apiPayload.code.status.ErrorStatus;
import com.checkhouse.core.apiPayload.exception.GeneralException;
import com.checkhouse.core.dto.CategoryDTO;
import com.checkhouse.core.entity.Category;
import com.checkhouse.core.repository.mysql.CategoryRepository;
import com.checkhouse.core.dto.request.CategoryRequest;
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

    public CategoryDTO addCategory(CategoryRequest.AddCategoryRequest req) {
        //이미 존재하는 카테고리 확인
        categoryRepository.findCategoryByName(req.name()).ifPresent(category -> {
            throw new GeneralException(ErrorStatus._CATEGORY_ALREADY_EXIST);
        });
        Category savedCategory = categoryRepository.save(
                Category.builder()
                        .categoryId(UUID.randomUUID())
                        .name(req.name())
                        .build()
        );

        return savedCategory.toDto();
    }

    public CategoryDTO getCategory(CategoryRequest.GetCategoryByIdRequest req) {
        Category category = categoryRepository.findById(req.categoryId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._CATEGORY_ID_NOT_FOUND));
        return category.toDto();
    }

    public CategoryDTO updateCategoryById(CategoryRequest.UpdateCategoryByIdRequest req) {
        //ID가 존재하는지 확인
        Category category = categoryRepository.findById(req.categoryId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._CATEGORY_ID_NOT_FOUND));
        //이미 존재하는 카테고리 확인
        categoryRepository.findCategoryByName(req.name()).ifPresent((a) -> {
            throw new GeneralException(ErrorStatus._CATEGORY_ALREADY_EXIST);
        });
        category.updateName(req.name());
        return category.toDto();
    }


    public void deleteCategory(CategoryRequest.DeleteCategoryRequest req) {
        //ID가 존재하는지 확인
        Category category = categoryRepository.findById(req.categoryId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._CATEGORY_ID_NOT_FOUND));

        categoryRepository.delete(category);
    }

    public List<CategoryDTO> getCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(Category::toDto)
                .toList();
    }
}
