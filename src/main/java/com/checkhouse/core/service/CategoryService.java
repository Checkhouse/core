package com.checkhouse.core.service;

import com.checkhouse.core.dto.CategoryDTO;
import com.checkhouse.core.entity.Category;
import com.checkhouse.core.repository.mysql.CategoryRepository;
import com.checkhouse.core.request.CategoryRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    CategoryDTO addCategory(CategoryRequest.AddCategoryRequest req) {

        Category savedCategory = categoryRepository.save(
                Category.builder()
                        .name(req.getName())
                        .build()
        );

        return savedCategory.toDTO();
    }
}
