package com.checkhouse.core.repository.mysql;

import com.checkhouse.core.entity.FavoriteOrigin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FavoriteOriginRepository  extends JpaRepository<FavoriteOrigin, UUID> {
    @Query("select fo from FavoriteOrigin fo where fo.user.userId =:userId")
    List<FavoriteOrigin> findAllByUserId(UUID userId);

    Boolean existsByOriginProductOriginProductIdAndUserUserId(UUID originId, UUID userId);

    void deleteByOriginProductOriginProductIdAndUserUserId(UUID originId, UUID userId);
}
