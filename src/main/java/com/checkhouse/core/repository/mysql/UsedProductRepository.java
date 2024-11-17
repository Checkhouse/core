package com.checkhouse.core.repository.mysql;

import com.checkhouse.core.entity.FavoriteOrigin;
import com.checkhouse.core.entity.UsedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsedProductRepository extends JpaRepository<UsedProduct, UUID> {
    @Query("select up from UsedProduct up where up.state = :state")
    List<UsedProduct> findAllByState(String state);

    @Query("select up from UsedProduct up where up.user.userId = :userId")
    List<UsedProduct> findAllByUserId(UUID userId);

}
