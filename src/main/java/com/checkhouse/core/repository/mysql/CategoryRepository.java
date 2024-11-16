package com.checkhouse.core.repository.mysql;

import com.checkhouse.core.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Optional<Category> findCategoryByCategoryId(UUID id);

}
