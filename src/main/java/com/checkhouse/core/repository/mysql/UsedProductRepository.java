package com.checkhouse.core.repository.mysql;

import com.checkhouse.core.entity.FavoriteOrigin;
import com.checkhouse.core.entity.UsedProduct;
import com.checkhouse.core.entity.enums.UsedProductState;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsedProductRepository extends JpaRepository<UsedProduct, UUID> {
    
    @Query("select up from UsedProduct up where up.state = :state and up.deletedDate is null")
    List<UsedProduct> findAllByState(UsedProductState state);

    @Query("select up from UsedProduct up where up.user.userId = :userId and up.deletedDate is null")
    List<UsedProduct> findAllByUserId(UUID userId);

    @Query("select up from UsedProduct up where (up.title like %:titleQuery% or up.description like %:descQuery%) and up.deletedDate is null")
    List<UsedProduct> findByTitleContainingOrDescriptionContaining(String titleQuery, String descQuery);

    @Query("select up from UsedProduct up where up.user.nickname like %:query% and up.deletedDate is null")
    List<UsedProduct> findByUserNicknameContaining(String query);

    @Query("select up from UsedProduct up where up.user.userId = :userId and up.title like %:query% and up.deletedDate is null")
    List<UsedProduct> findByUserIdAndTitleContaining(UUID userId, String query);

}
