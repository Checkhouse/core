package com.checkhouse.core.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;
import com.checkhouse.core.entity.Collect;
import com.checkhouse.core.entity.UsedProduct;
import com.checkhouse.core.entity.enums.DeliveryState;

public interface CollectRepository extends JpaRepository<Collect, UUID> {
    //수거 상태 수정
    @Modifying
    @Query("UPDATE Collect c SET c.state = :state WHERE c.collectId = :collectId")
    void updateCollectState(@Param("collectId") UUID collectId, @Param("state") DeliveryState state);
    //수거 등록 조회
    Optional<Collect> findByUsedProduct(UsedProduct usedProduct);
}
