package com.checkhouse.core.repository.mysql;

import com.checkhouse.core.entity.FavoriteOrigin;
import com.checkhouse.core.entity.FavoriteUsed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FavoriteUsedRepository extends JpaRepository<FavoriteUsed, UUID> {
    @Query("select fu from FavoriteUsed fu where fu.user.userId =:userId")
    List<FavoriteUsed> findAllByUserId(UUID userId);

    Boolean existsByUsedProductUsedProductIdAndUserUserId(UUID originId, UUID userId);


    int countByUsedProductUsedProductId(UUID usedProductId);
    void deleteByUsedProductUsedProductIdAndUserUserId(UUID originId, UUID userId);
}

