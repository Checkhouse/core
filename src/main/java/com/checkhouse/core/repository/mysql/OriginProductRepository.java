package com.checkhouse.core.repository.mysql;

import com.checkhouse.core.entity.OriginProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OriginProductRepository extends JpaRepository<OriginProduct, UUID> {

    Optional<OriginProduct> findByName(String name);

    @Query("select og " +
            "from OriginProduct og " +
            "where og.category.categoryId = :categoryId")
    List<OriginProduct> findByCategoryId(UUID categoryId);

}
