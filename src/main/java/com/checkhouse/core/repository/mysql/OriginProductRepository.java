package com.checkhouse.core.repository.mysql;

import com.checkhouse.core.entity.OriginProduct;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM origin_product op WHERE op.name = :name AND op.deleted_at IS NOT NULL", nativeQuery = true)
    void hardDeleteByNameIfSoftDeleted(String name);
}
