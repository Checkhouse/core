package com.checkhouse.core.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.checkhouse.core.entity.Inspection;
import com.checkhouse.core.entity.UsedProduct;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InspectionRepository extends JpaRepository<Inspection, UUID> {
    @Modifying
    @Query("UPDATE Inspection i SET i.isDone = :isDone WHERE i.inspectionId = :inspectionId")
    void update(@Param("inspectionId") UUID inspectionId, @Param("isDone") boolean isDone);
    List<Inspection> findByUsedProduct_UsedProductId(UUID usedProductId);
    Optional<Inspection> findByUsedProductAndIsDone(UsedProduct usedProduct, boolean isDone);
    Optional<Inspection> findByUsedProduct(UsedProduct usedProduct);
}
