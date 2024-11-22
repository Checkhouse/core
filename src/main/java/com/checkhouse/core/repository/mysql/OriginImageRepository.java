package com.checkhouse.core.repository.mysql;

import com.checkhouse.core.entity.OriginImage;
import com.checkhouse.core.entity.OriginProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OriginImageRepository extends JpaRepository<OriginImage, UUID> {
    List<OriginImage> findOriginImagesByOriginProductOriginProductId(UUID originProductId);
}
