package com.checkhouse.core.repository.mysql;

import com.checkhouse.core.entity.UsedImage;
import com.checkhouse.core.entity.UsedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UsedImageRepository extends JpaRepository<UsedImage, UUID> {
    List<UsedImage> findUsedImagesByUsedProduct(UsedProduct usedProduct);

}
